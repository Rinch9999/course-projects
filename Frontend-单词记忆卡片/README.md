# 单词记忆卡片（Frontend-单词记忆卡片）

对应简历「前端.pdf」项目 2。纯前端：HTML + CSS + JavaScript + localStorage。

## 目录结构
```
├── index.html        # 卡片翻转 + 单词列表 + 背诵进度 + 生词收藏
├── css/style.css     # 难点：3D 翻转动画（Transform + Transition）
└── js/app.js         # localStorage 数据存储 + 交互逻辑
```

## 对应简历要点
- **技术栈**：HTML + CSS + JavaScript + localStorage
- **核心开发**：卡片翻转、单词列表、背诵进度、生词收藏
- **难点突破**：`style.css` 用 `transform-style: preserve-3d` + `rotateY` 实现 3D 翻转；`app.js` 用 localStorage 做本地数据持久化。

## 运行
直接用浏览器打开 `index.html` 即可（无需构建）。
