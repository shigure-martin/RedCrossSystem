package com.redCross.constants;

public enum ItemConfirmStatus {
    uncheck("未审核"),
    confirm_sucess("审核成功"),confirm_fail("审核不通过");

    public String type;
    ItemConfirmStatus (String temp){this.type=temp;}
}
