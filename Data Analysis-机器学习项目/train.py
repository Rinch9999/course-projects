# -*- coding: utf-8 -*-
"""难点突破：机器学习建模全流程
（数据概览 → 特征工程 → 基线训练 → GridSearchCV 调参 → 完整评估 → 可视化 → 保存模型）。"""
import numpy as np
import pandas as pd
import matplotlib
matplotlib.use("Agg")  # 无界面环境也能出图
import matplotlib.pyplot as plt
import joblib

from sklearn.datasets import make_classification
from sklearn.model_selection import train_test_split, cross_val_score, GridSearchCV
from sklearn.preprocessing import StandardScaler
from sklearn.ensemble import RandomForestClassifier
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score, classification_report, confusion_matrix


def load_data():
    """示例数据（真实场景换成自己的数据集）"""
    X, y = make_classification(n_samples=2000, n_features=12, n_informative=9,
                               n_redundant=3, random_state=42)
    df = pd.DataFrame(X, columns=[f"f{i}" for i in range(X.shape[1])])
    df["label"] = y
    return df


def main():
    # 1. 数据概览（Pandas）
    data = load_data()
    print("1. 数据概览（Pandas describe）")
    print(data.describe().round(2))

    X = data.drop(columns=["label"]).values
    y = data["label"].values

    # 2. 特征缩放（难点：解决量纲不一致）
    scaler = StandardScaler()
    X = scaler.fit_transform(X)

    X_train, X_test, y_train, y_test = train_test_split(
        X, y, test_size=0.2, random_state=42)

    # 3. 基线模型对比 + 交叉验证
    print("\n2. 基线模型评估（准确率 + 5 折交叉验证）")
    models = {
        "LogisticRegression": LogisticRegression(max_iter=1000),
        "RandomForest": RandomForestClassifier(n_estimators=100, random_state=42),
    }
    results = {}
    for name, model in models.items():
        model.fit(X_train, y_train)
        acc = accuracy_score(y_test, model.predict(X_test))
        cv = cross_val_score(model, X, y, cv=5).mean()
        results[name] = acc
        print(f"   {name}: 测试准确率 {acc:.4f}，5 折 CV {cv:.4f}")

    # 4. 难点：GridSearchCV 调参
    print("\n3. GridSearchCV 对 RandomForest 调参")
    param_grid = {"n_estimators": [50, 100, 200], "max_depth": [None, 8, 16]}
    best_rf = GridSearchCV(RandomForestClassifier(random_state=42),
                           param_grid, cv=3, n_jobs=-1)
    best_rf.fit(X_train, y_train)
    print(f"   最佳参数：{best_rf.best_params_}")
    best_acc = accuracy_score(y_test, best_rf.predict(X_test))
    results["RandomForest+调参"] = best_acc
    print(f"   调参后测试准确率：{best_acc:.4f}")

    # 5. 完整评估报告
    print("\n4. 最佳模型评估")
    y_pred = best_rf.predict(X_test)
    print(classification_report(y_test, y_pred))
    print("混淆矩阵：\n", confusion_matrix(y_test, y_pred))

    # 6. 可视化 + 保存
    _plot(results)
    joblib.dump(best_rf.best_estimator_, "best_model.pkl")
    print("\n5. 已保存：模型 best_model.pkl，可视化 result_compare.png")


def _plot(results):
    names = list(results.keys())
    accs = [results[n] for n in names]
    plt.figure(figsize=(8, 5))
    bars = plt.bar(names, accs, color=["#4c8bf5", "#28a745", "#dc3545"])
    plt.ylim(0, 1.0)
    plt.ylabel("Accuracy")
    plt.title("Model Comparison")
    for b, a in zip(bars, accs):
        plt.text(b.get_x() + b.get_width() / 2, a + 0.01, f"{a:.3f}", ha="center")
    plt.xticks(rotation=15)
    plt.tight_layout()
    plt.savefig("result_compare.png", dpi=120)
    plt.close()


if __name__ == "__main__":
    main()