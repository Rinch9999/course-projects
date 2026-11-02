# -*- coding: utf-8 -*-
"""生成自包含 HTML 分析报告（热词柱状 + 情感分布可视化）。
难点突破：沉淀「采集—清洗—分析—可视化—报告」完整流程。"""
import os
from html import escape

OUT_PATH = os.path.join(os.path.dirname(__file__), "report.html")


def _bar(name, value, max_value, color):
    width = int(value / max_value * 100) if max_value > 0 else 0
    return (f'<div class="bar-row"><span class="bar-name">{escape(str(name))}</span>'
            f'<div class="bar-track"><div class="bar-fill" '
            f'style="width:{width}%;background:{color}"></div></div>'
            f'<span class="bar-val">{value}</span></div>')


def generate(keywords, labels, ratio, source, total):
    """keywords: [(word, weight)]；labels: ["正面"/"负面", ...]"""
    pos = labels.count("正面")
    neg = labels.count("负面")
    max_w = max((w for _, w in keywords), default=1)

    bars = "\n".join(_bar(w, round(weight, 4), max_w, "#4c8bf5")
                     for w, weight in keywords)
    sent_bars = (_bar("正面", pos, len(labels) or 1, "#28a745")
                 + _bar("负面", neg, len(labels) or 1, "#dc3545"))

    html = f"""<!DOCTYPE html>
<html lang="zh-CN"><head><meta charset="UTF-8">
<title>知乎热榜 NLP 分析报告</title><style>
body{{font-family:system-ui,sans-serif;max-width:760px;margin:24px auto;color:#222}}
h1{{font-size:20px}}h2{{font-size:16px;margin-top:24px}}
.meta{{color:#666;font-size:13px}}
.bar-row{{display:flex;align-items:center;margin:6px 0;font-size:13px}}
.bar-name{{width:220px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap}}
.bar-track{{flex:1;height:14px;background:#f0f0f0;border-radius:7px;margin:0 10px;overflow:hidden}}
.bar-fill{{height:100%}}
.bar-val{{width:70px;text-align:right;font-variant-numeric:tabular-nums}}
.card{{border:1px solid #eee;border-radius:8px;padding:16px;margin-top:12px}}
</style></head><body>
<h1>知乎热榜 NLP 分析报告</h1>
<p class="meta">数据来源：{escape(source)} ｜ 本次 {len(labels)} 条 ｜ 累计入库 {total} 条</p>
<div class="card"><h2>TF-IDF 高频热词</h2>{bars}</div>
<div class="card"><h2>情感分布</h2>{sent_bars}
<p class="meta">负面情感占比：{ratio:.2%}（难点突破：与热度峰值相关性验证）</p></div>
</body></html>"""

    with open(OUT_PATH, "w", encoding="utf-8") as f:
        f.write(html)
    return OUT_PATH