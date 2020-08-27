package com.redCross.constants;

public enum RoleType {
    customer("用户"), superAdmin("管理员");
    public String role;

    RoleType(String r) {
        this.role = r;
    }
}
