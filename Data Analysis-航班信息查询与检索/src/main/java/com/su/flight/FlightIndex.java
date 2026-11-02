package com.su.flight;

import java.util.*;

/**
 * 航班索引。难点突破：用不同数据结构把查询时间复杂度降下来。
 *  - 哈希索引：按航班号查询 O(1)
 *  - 二分查找：按航班号查询 O(log n)
 *  - 排序：按价格/时间排序，支撑区间查询
 */
public class FlightIndex {

    private final Map<String, Flight> hashIndex = new HashMap<>();
    private List<Flight> sortedByNo = new ArrayList<>();

    /** 构建索引 */
    public void build(List<Flight> flights) {
        for (Flight f : flights) {
            hashIndex.put(f.getFlightNo(), f);
        }
        sortedByNo = new ArrayList<>(flights);
        sortedByNo.sort(Comparator.comparing(Flight::getFlightNo));
    }

    /** O(1) 哈希查询 */
    public Flight queryByHash(String flightNo) {
        return hashIndex.get(flightNo);
    }

    /** O(log n) 二分查找 */
    public Flight queryByBinarySearch(String flightNo) {
        int lo = 0, hi = sortedByNo.size() - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int cmp = sortedByNo.get(mid).getFlightNo().compareTo(flightNo);
            if (cmp == 0) return sortedByNo.get(mid);
            else if (cmp < 0) lo = mid + 1;
            else hi = mid - 1;
        }
        return null;
    }

    /** 按价格升序排序（支撑区间查询） */
    public List<Flight> sortByPrice() {
        List<Flight> list = new ArrayList<>(hashIndex.values());
        list.sort(Comparator.comparingInt(Flight::getPrice));
        return list;
    }

    /** 按价格区间查询（依赖价格有序列表，超出上界即提前终止） */
    public List<Flight> queryByPriceRange(int min, int max) {
        List<Flight> result = new ArrayList<>();
        for (Flight f : sortByPrice()) {
            if (f.getPrice() < min) {
                continue;
            }
            if (f.getPrice() > max) {
                break;
            }
            result.add(f);
        }
        return result;
    }

    /** 按起终点航线查询，结果按出发时间升序 */
    public List<Flight> queryByRoute(String from, String to) {
        List<Flight> result = new ArrayList<>();
        for (Flight f : hashIndex.values()) {
            if (f.getFrom().equals(from) && f.getTo().equals(to)) {
                result.add(f);
            }
        }
        result.sort(Comparator.comparing(Flight::getDeparture));
        return result;
    }

    /** 按出发时间升序排序 */
    public List<Flight> sortByDeparture() {
        List<Flight> list = new ArrayList<>(hashIndex.values());
        list.sort(Comparator.comparing(Flight::getDeparture));
        return list;
    }
}
