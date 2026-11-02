package com.su.flight;

import java.util.List;

/**
 * 演示：构建索引 → 哈希查询 → 二分查找 → 价格区间 → 航线查询 → 时间/价格排序。
 */
public class Main {
    public static void main(String[] args) {
        List<Flight> flights = List.of(
                new Flight("CA1234", "北京", "上海", "08:00", 850),
                new Flight("CA1857", "北京", "上海", "15:30", 780),
                new Flight("MU5678", "北京", "广州", "09:30", 1200),
                new Flight("CZ9012", "上海", "深圳", "11:00", 900),
                new Flight("HU3456", "北京", "成都", "14:20", 1100),
                new Flight("CA4102", "上海", "深圳", "18:50", 620)
        );

        FlightIndex index = new FlightIndex();
        index.build(flights);

        System.out.println("1. 哈希查询 MU5678 (O(1))：" + index.queryByHash("MU5678"));
        System.out.println("2. 二分查找 CZ9012 (O(log n))：" + index.queryByBinarySearch("CZ9012"));

        System.out.println("3. 价格区间 [700, 1000]：" + index.queryByPriceRange(700, 1000));
        System.out.println("4. 航线 北京→上海：" + index.queryByRoute("北京", "上海"));
        System.out.println("5. 按出发时间排序：" + index.sortByDeparture());
        System.out.println("6. 按价格排序：" + index.sortByPrice());
    }
}