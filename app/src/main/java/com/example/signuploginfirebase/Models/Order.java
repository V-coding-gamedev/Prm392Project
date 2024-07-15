package com.example.signuploginfirebase.Models;

import androidx.annotation.NonNull;

public class Order {
    public int order_id;
    public String orderDate;
    public String status;
    public float totalAmount;
    public String startDate;
    public String endDate;
    public String shipAddress;
    public int user_id;

    public int getOrder_id() {
        return order_id;
    }

    @NonNull
    @Override
    public String toString() {
        return "Order{" +
                "order_id=" + order_id +
                ", orderDate='" + orderDate + '\'' +
                ", status='" + status + '\'' +
                ", totalAmount=" + totalAmount +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", shipAddress='" + shipAddress + '\'' +
                ", user_id=" + user_id +
                '}';
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
