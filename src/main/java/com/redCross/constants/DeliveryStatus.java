package com.redCross.constants;

public enum DeliveryStatus {
    cancel("已取消"),
    received("已收货"),
    delivered("已发货"),
    un_delivered("未发货");

    public String status;

    DeliveryStatus(String t) {
        this.status = t;
    }
}
