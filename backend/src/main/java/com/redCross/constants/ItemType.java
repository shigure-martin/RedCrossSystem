package com.redCross.constants;

public enum ItemType {
    food("食品"), medical_supplies("医疗用品"),
    daily_necessities("日常用品"), clothing("衣物");
    public String type;
    ItemType(String s) {
        this.type = s;
    }
}
