# 知乎热榜爬虫与 NLP 文本挖掘（Data Analysis-知乎热榜爬虫与NLP）

对应简历「数据分析.pdf」项目 2。Python + Requests + BeautifulSoup + Jieba + TF-IDF + SnowNLP。

## 目录结构
```
├── requirements.txt      # 依赖
├── scraper.py            # 难点：Requests + BeautifulSoup 抓取热榜 Top50
├── nlp_analysis.py       # 难点：Jieba 分词 + TF-IDF 热词 + SnowNLP 情感分析
├── storage.py            # 持久化入库（默认 SQLite，可换 MySQL，累计 2000+ 条）
├── report.py             # 生成自包含 HTML 报告（内联可视化）
└── main.py               # 编排：采集 → 清洗 → 分析 → 入库 → 报告
```

## 对应简历要点
- **技术栈**：Python + Requests + BeautifulSoup + Jieba + TF-IDF + SnowNLP + MySQL + ECharts
- **核心开发**：定时抓取热榜 Top50、2000+ 条文本采集、去重清洗、分词、TF-IDF 热词、SnowNLP 情感分析
- **难点突破**：`nlp_analysis.py` 验证「负面情感比例与热度峰值正相关」的假设，沉淀「采集—清洗—分析—可视化—报告」流程。

## 运行
`pip install -r requirements.txt` 后 `python main.py`。
