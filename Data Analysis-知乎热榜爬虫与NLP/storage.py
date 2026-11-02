# -*- coding: utf-8 -*-
"""持久化：把每轮采集的标题 / 情感结果入库。
默认用 Python 内置 SQLite（零配置可运行、可累计 2000+ 条），
生产环境可替换为 MySQL（pymysql 已在 requirements.txt 中）。"""
import os
import sqlite3
from datetime import datetime

DB_PATH = os.path.join(os.path.dirname(__file__), "hot_topics.db")


def _init(conn):
    conn.execute("""
        CREATE TABLE IF NOT EXISTS hot_topic (
            id         INTEGER PRIMARY KEY AUTOINCREMENT,
            title      TEXT NOT NULL,
            sentiment  REAL,
            label      TEXT,
            crawled_at TEXT
        )
    """)


def save_records(titles, scores, labels):
    """保存一轮采集结果，返回本次写入条数。"""
    conn = sqlite3.connect(DB_PATH)
    try:
        _init(conn)
        now = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        rows = [(t, s, l, now) for t, s, l in zip(titles, scores, labels)]
        conn.executemany(
            "INSERT INTO hot_topic(title, sentiment, label, crawled_at) "
            "VALUES (?, ?, ?, ?)", rows)
        conn.commit()
        return len(rows)
    finally:
        conn.close()


def total_count():
    """累计已入库条数（支撑简历里「2000+ 条文本采集」的积累）。"""
    conn = sqlite3.connect(DB_PATH)
    try:
        _init(conn)
        return conn.execute("SELECT COUNT(*) FROM hot_topic").fetchone()[0]
    finally:
        conn.close()


def latest_negative_ratio(limit=100):
    """最近 N 条的负面情感占比（用于与热度峰值做相关性验证）。"""
    conn = sqlite3.connect(DB_PATH)
    try:
        _init(conn)
        rows = conn.execute(
            "SELECT label FROM hot_topic ORDER BY id DESC LIMIT ?",
            (limit,)).fetchall()
        if not rows:
            return 0.0
        neg = sum(1 for (label,) in rows if label == "负面")
        return neg / len(rows)
    finally:
        conn.close()