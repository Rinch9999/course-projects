# 知乎热榜数据清洗与质量校验（QA-知乎热榜数据清洗）

对应简历「测试.pdf」项目 2。Python + Requests。

## 目录结构
```
├── requirements.txt
└── data_clean.py      # 难点：去重/空值填充/特殊字符过滤/短文本剔除 + 数据校验规则
```

## 对应简历要点
- **技术栈**：Python + Requests
- **核心开发**：对 2000+ 条文本执行去重、空值填充、特殊字符过滤、短文本剔除，形成结构化数据集
- **难点突破**：`data_clean.py` 设计数据校验规则，确保入库数据的完整性与一致性。

## 运行
`pip install -r requirements.txt` 后 `python data_clean.py`。
