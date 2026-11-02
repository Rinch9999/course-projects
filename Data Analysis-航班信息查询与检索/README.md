# 航班信息查询与检索（Data Analysis-航班信息查询与检索）

对应简历「数据分析.pdf」项目 1。Java + 排序/查找/哈希等数据结构。

## 目录结构
```
└── src/main/java/com/su/flight
    ├── Flight.java        # 航班实体
    ├── FlightIndex.java   # 难点：哈希索引(O(1)) + 二分查找(O(log n)) + 排序
    └── Main.java          # 演示入口
```

## 对应简历要点
- **技术栈**：Java + 排序/查找/哈希等数据结构
- **核心开发**：用排序、查找、哈希实现航班信息高效查询与检索
- **难点突破**：`FlightIndex` 用哈希索引把按航班号查询降到 O(1)、二分查找降到 O(log n)，针对大规模数据降低检索时间复杂度。

## 运行
纯 JDK，无第三方依赖。源码为 UTF-8 编码，编译时需指定编码：

`javac -encoding UTF-8 -d out src/main/java/com/su/flight/*.java`
`java -cp out com.su.flight.Main`
