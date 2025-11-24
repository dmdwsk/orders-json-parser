package com.orders.entities;

public class Order {
    private String orderId;
    private String customerName;
    private String date;
    private double totalPrice;
    private String products;

    public Order(){

    }

    public Order(String orderId, String customerName, String date, double totalPrice, String products) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.date = date;
        this.totalPrice = totalPrice;
        this.products = products;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getDate() {
        return date;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getProducts() {
        return products;
    }
}
