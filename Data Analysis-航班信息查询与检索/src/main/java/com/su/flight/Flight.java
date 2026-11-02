package com.su.flight;

/**
 * 航班实体。
 */
public class Flight {
    private String flightNo;   // 航班号，如 CA1234
    private String from;       // 起飞机场
    private String to;         // 目的机场
    private String departure;  // 起飞时间 HH:mm
    private int price;         // 票价

    public Flight(String flightNo, String from, String to, String departure, int price) {
        this.flightNo = flightNo;
        this.from = from;
        this.to = to;
        this.departure = departure;
        this.price = price;
    }

    public String getFlightNo() { return flightNo; }
    public String getFrom() { return from; }
    public String getTo() { return to; }
    public String getDeparture() { return departure; }
    public int getPrice() { return price; }

    @Override
    public String toString() {
        return flightNo + " " + from + "→" + to + " " + departure + " ¥" + price;
    }
}
