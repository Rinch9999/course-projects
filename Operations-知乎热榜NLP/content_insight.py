# -*- coding: utf-8 -*-
"""运营视角：知乎热榜文本挖掘 → 内容选题 + 运营节奏建议。

完整流程：采集 → 清洗 → 分词/热词 → 情感分析 → 热度趋势可视化
         → 负面情感与热度峰值相关性验证 → 报告 + 持久化入库。

难点突破：
1. 皮尔逊相关系数验证「负面情感比例与热度峰值正相关」。
2. 把文本挖掘结果转化为可执行的运营动作（选题 + 发布节奏）。
"""
import base64
import math
import os
import re
import sqlite3
from datetime import datetime
from html import escape

import requests
from bs4 import BeautifulSoup
import jieba
import jieba.analyse
from snownlp import SnowNLP

# 可视化依赖可选：缺失时跳过图表生成，其余流程不受影响
try:
    import matplotlib
    matplotlib.use("Agg")
    import matplotlib.pyplot as plt
    plt.rcParams["font.sans-serif"] = ["SimHei", "Microsoft YaHei", "Arial Unicode MS"]
    plt.rcParams["axes.unicode_minus"] = False
    HAS_MPL = True
except Exception:
    HAS_MPL = False

HEADERS = {
    "User-Agent": ("Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                   "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120 Safari/537.36")
}

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
DB_PATH = os.path.join(BASE_DIR, "insight.db")
PNG_PATH = os.path.join(BASE_DIR, "heat_trend.png")
REPORT_PATH = os.path.join(BASE_DIR, "insight_report.html")

SAMPLE_TITLES = [
    "如何看待某公司裁员", "新款手机发布会盘点", "高考志愿填报建议",
    "某城市房价走势分析", "AI 大模型最新进展", "大学生就业形势讨论",
    "某明星事件引发热议", "食品安全问题调查", "新能源车补贴政策",
    "某电影口碑与票房", "程序员 35 岁危机", "考研报名人数变化",
    "某地极端天气预警", "旅游旺季出行攻略", "某科技公司财报解读",
]

_METRICS_RE = re.compile(r"([\d.]+)\s*万?")


def load_items(top_n=50):
    """采集热榜 (标题, 热度)；解析失败按排名回退；整体失败回退示例数据。"""
    try:
        resp = requests.get("https://www.zhihu.com/hot", headers=HEADERS, timeout=10)
        resp.raise_for_status()
        soup = BeautifulSoup(resp.text, "html.parser")
        items = []
        for i, card in enumerate(soup.select(".HotItem")):
            node = card.select_one(".HotItem-title")
            if node:
                items.append((node.get_text(strip=True), _parse_heat(card, i)))
        if len(items) >= 10:
            return items[:top_n], "真实抓取"
    except Exception as e:
        print(f"   真实抓取失败（{e}），回退示例数据")
    return _sample_with_heat(), "示例数据"


def _parse_heat(card, index):
    """从 metrics 文本解析热度；失败时用榜单排名近似回退。"""
    metrics = card.select_one(".HotItem-metrics")
    if metrics:
        m = _METRICS_RE.search(metrics.get_text())
        if m:
            val = float(m.group(1))
            return val * 10000 if "万" in metrics.get_text() else val
    return max(10000, 5000000 - index * 300000)


def _sample_with_heat():
    """示例数据 + 确定性递减热度（真实抓取被反爬时保证流程可完整运行）。"""
    return [(t, max(10000, 5000000 - i * 300000))
            for i, t in enumerate(SAMPLE_TITLES)]


def clean(items):
    """清洗：去重、去空、过滤短文本；保留热度。"""
    cleaned, seen = [], set()
    for title, heat in items:
        t = title.strip()
        if not t or len(t) < 2 or t in seen:
            continue
        seen.add(t)
        cleaned.append((t, heat))
    return cleaned


def hot_words(titles, top_k=10):
    """jieba 分词 + TF-IDF 热词提取"""
    return jieba.analyse.extract_tags("\n".join(titles), topK=top_k, withWeight=True)


def analyze(items):
    """SnowNLP 情感分析，返回 [(title, heat, score, label)]"""
    rows = []
    for title, heat in items:
        score = SnowNLP(title).sentiments
        rows.append((title, heat, score, "正面" if score >= 0.5 else "负面"))
    return rows


def negative_ratio(rows):
    """负面情感占比"""
    if not rows:
        return 0.0
    return sum(1 for r in rows if r[3] == "负面") / len(rows)


def pearson(xs, ys):
    """皮尔逊相关系数（不依赖 numpy）"""
    if len(xs) < 2:
        return 0.0
    mx, my = sum(xs) / len(xs), sum(ys) / len(ys)
    num = sum((x - mx) * (y - my) for x, y in zip(xs, ys))
    dx = math.sqrt(sum((x - mx) ** 2 for x in xs))
    dy = math.sqrt(sum((y - my) ** 2 for y in ys))
    if dx == 0 or dy == 0:
        return 0.0
    return num / (dx * dy)


def correlation(rows):
    """负面情感(1-score) 与 热度的相关性（难点突破：验证正相关假设）"""
    heat = [r[1] for r in rows]
    neg = [1.0 - r[2] for r in rows]
    return pearson(heat, neg)


def content_advice(rows):
    """内容选题建议（按热度降序）"""
    tips = []
    for title, heat, score, label in rows:
        if label == "负面" and score < 0.3:
            tips.append(("避坑/科普", f"{title}（负面情绪强，热度 {_fmt_heat(heat)}）", heat))
        elif label == "负面":
            tips.append(("谨慎跟进", f"{title}（偏负面，建议客观中立角度）", heat))
        else:
            tips.append(("可追热点", f"{title}（正面，适合内容选题）", heat))
    tips.sort(key=lambda x: -x[2])
    return tips


def rhythm_advice(rows):
    """运营节奏建议：按热度分档给出发布优先级与窗口"""
    if not rows:
        return []
    heats = sorted(r[1] for r in rows)
    p75 = heats[int(len(heats) * 0.75)] if len(heats) >= 4 else heats[-1]
    p25 = heats[int(len(heats) * 0.25)] if len(heats) >= 4 else heats[0]
    advice = []
    for title, heat, _score, _label in rows:
        if heat >= p75:
            advice.append(f"第一时间跟进「{title}」，抢占流量峰值窗口（热度 {_fmt_heat(heat)}）")
        elif heat >= p25:
            advice.append(f"常规排期「{title}」，观察 24h 趋势再决定加推（热度 {_fmt_heat(heat)}）")
        else:
            advice.append(f"长尾观望「{title}」，低热度可沉淀为长效选题（热度 {_fmt_heat(heat)}）")
    return advice


def _fmt_heat(heat):
    return f"{heat / 10000:.1f}万" if heat >= 10000 else f"{heat:.0f}"


def plot_trend(rows):
    """热度趋势 + 情感分布可视化；无 matplotlib 时跳过。"""
    if not HAS_MPL:
        return None
    rows = sorted(rows, key=lambda r: -r[1])
    ranks = list(range(1, len(rows) + 1))
    heats = [r[1] for r in rows]
    scores = [r[2] for r in rows]
    colors = ["#dc3545" if r[3] == "负面" else "#28a745" for r in rows]

    fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(12, 4.5))
    ax1.bar(ranks, heats, color=colors)
    ax1.set_title("热度趋势（按榜单排序）")
    ax1.set_xlabel("排名")
    ax1.set_ylabel("热度")

    ax2.scatter(heats, [1.0 - s for s in scores], c=colors, alpha=0.7)
    ax2.set_title("负面情感 vs 热度（相关性验证）")
    ax2.set_xlabel("热度")
    ax2.set_ylabel("负面程度(1-score)")

    fig.tight_layout()
    fig.savefig(PNG_PATH, dpi=120)
    plt.close(fig)
    return PNG_PATH


def _init(conn):
    conn.execute("""
        CREATE TABLE IF NOT EXISTS content_insight (
            id         INTEGER PRIMARY KEY AUTOINCREMENT,
            title      TEXT NOT NULL,
            heat       REAL,
            sentiment  REAL,
            label      TEXT,
            crawled_at TEXT
        )
    """)


def save_records(rows):
    """持久化本轮结果；默认 SQLite（零配置可运行），生产可换 MySQL。"""
    conn = sqlite3.connect(DB_PATH)
    try:
        _init(conn)
        now = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        conn.executemany(
            "INSERT INTO content_insight(title, heat, sentiment, label, crawled_at) "
            "VALUES (?, ?, ?, ?, ?)",
            [(t, float(h), float(s), l, now) for t, h, s, l in rows])
        conn.commit()
        return len(rows)
    finally:
        conn.close()


def total_count():
    """累计已入库条数"""
    conn = sqlite3.connect(DB_PATH)
    try:
        _init(conn)
        return conn.execute("SELECT COUNT(*) FROM content_insight").fetchone()[0]
    finally:
        conn.close()


def _bar(name, value, max_value, color):
    width = int(value / max_value * 100) if max_value > 0 else 0
    return (f'<div class="bar-row"><span class="bar-name">{escape(str(name))}</span>'
            f'<div class="bar-track"><div class="bar-fill" '
            f'style="width:{width}%;background:{color}"></div></div>'
            f'<span class="bar-val">{value}</span></div>')


def generate_report(rows, keywords, ratio, corr, advices, rhythms, source, total):
    pos = sum(1 for r in rows if r[3] == "正面")
    neg = len(rows) - pos
    max_w = max((w for _, w in keywords), default=1)

    kw_bars = "\n".join(_bar(w, round(weight, 4), max_w, "#4c8bf5")
                        for w, weight in keywords)
    sent_bars = (_bar("正面", pos, len(rows) or 1, "#28a745")
                 + _bar("负面", neg, len(rows) or 1, "#dc3545"))

    img_tag = ""
    if os.path.exists(PNG_PATH):
        with open(PNG_PATH, "rb") as f:
            b64 = base64.b64encode(f.read()).decode("ascii")
        img_tag = f'<img style="width:100%;margin-top:12px" src="data:image/png;base64,{b64}">'

    adv_html = "\n".join(f"<li><b>{escape(kind)}</b>：{escape(text)}</li>"
                         for kind, text, _ in advices)
    rhy_html = "\n".join(f"<li>{escape(text)}</li>" for text in rhythms)

    sign = "正相关" if corr > 0 else ("负相关" if corr < 0 else "无相关")
    html = f"""<!DOCTYPE html>
<html lang="zh-CN"><head><meta charset="UTF-8">
<title>知乎热榜运营洞察报告</title><style>
body{{font-family:system-ui,sans-serif;max-width:820px;margin:24px auto;color:#222}}
h1{{font-size:20px}}h2{{font-size:16px;margin-top:24px}}
.meta{{color:#666;font-size:13px}}
.bar-row{{display:flex;align-items:center;margin:6px 0;font-size:13px}}
.bar-name{{width:220px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap}}
.bar-track{{flex:1;height:14px;background:#f0f0f0;border-radius:7px;margin:0 10px;overflow:hidden}}
.bar-fill{{height:100%}}
.bar-val{{width:70px;text-align:right;font-variant-numeric:tabular-nums}}
.card{{border:1px solid #eee;border-radius:8px;padding:16px;margin-top:12px}}
ul{{margin:8px 0;padding-left:20px}}li{{margin:4px 0;font-size:14px}}
</style></head><body>
<h1>知乎热榜运营洞察报告</h1>
<p class="meta">数据来源：{escape(source)} ｜ 本次 {len(rows)} 条 ｜ 累计入库 {total} 条</p>

<div class="card"><h2>TF-IDF 高频热词</h2>{kw_bars}</div>

<div class="card"><h2>热度趋势与情感</h2>{img_tag}
{sent_bars}<p class="meta">负面情感占比：{ratio:.2%} ｜ 与热度峰值相关性 r={corr:.3f}（{sign}）</p></div>

<div class="card"><h2>内容选题建议</h2><ul>{adv_html}</ul></div>

<div class="card"><h2>运营节奏建议</h2><ul>{rhy_html}</ul></div>
</body></html>"""

    with open(REPORT_PATH, "w", encoding="utf-8") as f:
        f.write(html)
    return REPORT_PATH


def _sign(corr):
    return "正相关" if corr > 0 else ("负相关" if corr < 0 else "无相关")


def main():
    print("1. 采集热榜 Top50...")
    items, source = load_items(50)
    print(f"   数据来源：{source}，共 {len(items)} 条")

    print("2. 数据清洗（去重/去空/去短文本）...")
    cleaned = clean(items)
    print(f"   清洗后 {len(cleaned)} 条")

    print("3. jieba 分词 + TF-IDF 热词...")
    keywords = hot_words([t for t, _ in cleaned], top_k=10)
    for w, weight in keywords:
        print(f"   {w}: {weight:.4f}")

    print("4. SnowNLP 情感分析...")
    rows = analyze(cleaned)
    ratio = negative_ratio(rows)
    print(f"   负面情感占比：{ratio:.2%}")

    print("5. 相关性验证（负面情感 vs 热度峰值）...")
    corr = correlation(rows)
    print(f"   相关系数 r = {corr:.3f}（{_sign(corr)}）")

    print("6. 热度趋势可视化...")
    png = plot_trend(rows)
    print(f"   图表生成：{png}" if png else "   未安装 matplotlib，跳过图表")

    print("7. 持久化入库...")
    saved = save_records(rows)
    total = total_count()
    print(f"   本次写入 {saved} 条，累计 {total} 条")

    print("8. 生成运营洞察报告...")
    advices = content_advice(rows)
    rhythms = rhythm_advice(rows)
    path = generate_report(rows, keywords, ratio, corr, advices, rhythms, source, total)
    print(f"   报告已生成：{path}")


if __name__ == "__main__":
    main()