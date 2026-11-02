# -*- coding: utf-8 -*-
"""难点突破：Requests + BeautifulSoup 抓取知乎热榜 Top50。"""
import requests
from bs4 import BeautifulSoup

HEADERS = {
    "User-Agent": ("Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                   "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120 Safari/537.36")
}


def fetch_hot_list(top_n=50):
    """抓取知乎热榜标题（骨架：选择器需按实际页面结构调整）"""
    url = "https://www.zhihu.com/hot"
    resp = requests.get(url, headers=HEADERS, timeout=10)
    resp.raise_for_status()
    soup = BeautifulSoup(resp.text, "html.parser")
    titles = []
    for card in soup.select(".HotItem"):
        node = card.select_one(".HotItem-title")
        if node:
            titles.append(node.get_text(strip=True))
    return titles[:top_n]


def clean(titles):
    """数据清洗：去重、去空、过滤短文本"""
    cleaned = []
    seen = set()
    for t in titles:
        t = t.strip()
        if not t or len(t) < 2 or t in seen:
            continue
        seen.add(t)
        cleaned.append(t)
    return cleaned
