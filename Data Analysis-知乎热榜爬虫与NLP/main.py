# -*- coding: utf-8 -*-
"""主流程：采集 → 清洗 → 分词 → TF-IDF → 情感分析 → 入库 → 报告。"""
from scraper import fetch_hot_list, clean
from nlp_analysis import extract_keywords, analyze_sentiment, negative_ratio
import storage
import report

# 示例数据：真实抓取被反爬拦截（403）时回退，保证流程可完整演示
SAMPLE_TITLES = [
    "如何看待某公司裁员", "新款手机发布会盘点", "高考志愿填报建议",
    "某城市房价走势分析", "AI 大模型最新进展", "大学生就业形势讨论",
    "某明星事件引发热议", "食品安全问题调查", "新能源车补贴政策",
    "某电影口碑与票房", "程序员 35 岁危机", "考研报名人数变化",
    "某地极端天气预警", "旅游旺季出行攻略", "某科技公司财报解读",
]


def load_titles():
    """采集标题；失败则回退到示例数据"""
    try:
        titles = fetch_hot_list(50)
        if len(titles) >= 10:
            return titles, "真实抓取"
    except Exception as e:
        print(f"   真实抓取失败（{e}），回退示例数据")
    return SAMPLE_TITLES, "示例数据"


def main():
    print("1. 采集热榜 Top50...")
    titles, source = load_titles()
    print(f"   数据来源：{source}，共 {len(titles)} 条")

    print("2. 数据清洗（去重/去空/去短文本）...")
    cleaned = clean(titles)
    print(f"   清洗后 {len(cleaned)} 条")

    print("3. TF-IDF 高频热词...")
    keywords = extract_keywords(cleaned, top_k=10)
    for word, weight in keywords:
        print(f"   {word}: {weight:.4f}")

    print("4. SnowNLP 情感分析...")
    scores, labels = analyze_sentiment(cleaned)
    ratio = negative_ratio(labels)
    print(f"   负面情感占比：{ratio:.2%}")

    print("5. 持久化入库...")
    saved = storage.save_records(cleaned, scores, labels)
    total = storage.total_count()
    print(f"   本次写入 {saved} 条，累计 {total} 条")

    print("6. 生成分析报告...")
    path = report.generate(keywords, labels, ratio, source, total)
    print(f"   报告已生成：{path}")
    print(f"   结论：负面情感占比 {ratio:.2%}，可与热度峰值做相关性验证（难点突破）。")


if __name__ == "__main__":
    main()