# 知乎热榜 NLP 与内容运营支持（Operations-知乎热榜NLP）

对应简历「运营.pdf」项目 2。运营视角：把文本挖掘结果转化为内容选题与运营节奏建议。

## 目录结构
```
├── requirements.txt      # 依赖
└── content_insight.py    # 难点：情感分析 → 内容选题/运营节奏建议（端到端）
                          # 运行后生成 insight_report.html / heat_trend.png / insight.db
```

## 对应简历要点
- **技术栈**：Python + Requests + SnowNLP + MySQL
- **核心开发**：抓取热榜 Top50、清洗、jieba 分词、TF-IDF 热词、情感分析，输出热度趋势与情感占比
- **难点突破**：皮尔逊相关系数验证「负面情感比例与热度峰值正相关」，沉淀「采集—清洗—分析—可视化—报告」流程，输出内容选题与运营节奏建议。

## 运行
`pip install -r requirements.txt` 后 `python content_insight.py`。
生成 `insight_report.html`（自包含报告）、`heat_trend.png`（热度趋势图）、`insight.db`（SQLite 持久化，生产可换 MySQL）。