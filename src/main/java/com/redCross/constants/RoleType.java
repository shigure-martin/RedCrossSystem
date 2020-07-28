package com.redCross.constants;

public enum RoleType {
    donor("捐助者"), recipient("受捐者"),superAdmin("管理员");
    public String role;

    RoleType(String r) {
        this.role = r;
    }
}
