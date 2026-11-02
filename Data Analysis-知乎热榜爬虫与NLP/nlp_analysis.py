# -*- coding: utf-8 -*-
"""难点突破：Jieba 分词 + TF-IDF 关键词 + SnowNLP 情感分析。"""
import jieba
import jieba.analyse
from snownlp import SnowNLP


def tokenize(texts):
    """Jieba 分词"""
    return [list(jieba.cut(t)) for t in texts]


def extract_keywords(texts, top_k=10):
    """TF-IDF 提取高频热词"""
    text = "\n".join(texts)
    return jieba.analyse.extract_tags(text, topK=top_k, withWeight=True)


def sentiment_score(text):
    """SnowNLP 情感得分，[0,1] 越接近 1 越正面"""
    return SnowNLP(text).sentiments


def analyze_sentiment(texts):
    """返回每条文本的情感得分与正负标签（>=0.5 正面）"""
    scores = [sentiment_score(t) for t in texts]
    labels = ["正面" if s >= 0.5 else "负面" for s in scores]
    return scores, labels


def negative_ratio(labels):
    """负面情感占比"""
    if not labels:
        return 0.0
    return labels.count("负面") / len(labels)
