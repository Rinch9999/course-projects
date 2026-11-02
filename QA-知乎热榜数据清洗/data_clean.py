# -*- coding: utf-8 -*-
"""难点突破：数据清洗与质量校验（端到端脚本）。

对知乎热榜文本执行：去重、空值填充、特殊字符过滤、短文本剔除，
再依据校验规则保证入库数据完整一致；最后输出统计报告并把结果写回 CSV。
"""
import csv
import os
import random
import re

SPECIAL_CHARS = re.compile(r"[^一-龥a-zA-Z0-9\s]")
OUT_PATH = os.path.join(os.path.dirname(__file__), "cleaned_topics.csv")


def dedupe(rows):
    """去重（保持原顺序）"""
    seen, result = set(), []
    for r in rows:
        if r not in seen:
            seen.add(r)
            result.append(r)
    return result


def fill_missing(rows, default="(无标题)"):
    """空值填充"""
    return [r if r and r.strip() else default for r in rows]


def filter_special_chars(text):
    """特殊字符过滤"""
    return SPECIAL_CHARS.sub("", text).strip()


def drop_short(rows, min_len=2):
    """短文本剔除"""
    return [r for r in rows if len(r) >= min_len]


def validate(rows):
    """数据校验规则：非空、非重复、长度合法，返回校验报告"""
    report = {
        "total": len(rows),
        "empty": sum(1 for r in rows if not r),
        "duplicated": len(rows) - len(set(rows)),
        "too_short": sum(1 for r in rows if len(r) < 2),
    }
    valid = report["empty"] == 0 and report["duplicated"] == 0 and report["too_short"] == 0
    return valid, report


def clean(rows):
    """完整清洗流水线"""
    rows = fill_missing(rows)
    rows = [filter_special_chars(r) for r in rows]
    rows = drop_short(rows)
    rows = dedupe(rows)
    return rows


def _sample_rows(n=2000):
    """生成含脏数据的模拟样本（真实场景替换为抓取数据）"""
    words = ["AI大模型", "房价走势", "大学生就业", "食品安全", "新能源车",
             "裁员", "考研", "极端天气", "旅游攻略", "财报解读"]
    rows = []
    for i in range(n):
        if i % 13 == 0:
            rows.append("   ")                       # 空值
        elif i % 11 == 0:
            rows.append("短")                        # 短文本
        elif i % 9 == 0:
            rows.append("重复标题")                   # 重复
        else:
            rows.append(f"{random.choice(words)} 话题 {i} @@ 特殊字符")
    return rows


def load(path=None):
    """从本地 CSV 读取；不存在则返回模拟样本"""
    if path and os.path.exists(path):
        with open(path, encoding="utf-8") as f:
            return [row[0] for row in csv.reader(f) if row]
    return _sample_rows()


def save_cleaned(rows, path=OUT_PATH):
    """把清洗结果写回 CSV"""
    with open(path, "w", encoding="utf-8", newline="") as f:
        csv.writer(f).writerows([[r] for r in rows])
    return path


def main():
    rows = load(None)
    print(f"原始数据：{len(rows)} 条")

    cleaned = clean(rows)
    print(f"清洗后：{len(cleaned)} 条（去除 {len(rows) - len(cleaned)} 条）")

    valid, report = validate(cleaned)
    print("校验通过：", valid)
    print("校验报告：", report)

    out = save_cleaned(cleaned)
    print(f"已写回结构化数据：{out}")


if __name__ == "__main__":
    main()