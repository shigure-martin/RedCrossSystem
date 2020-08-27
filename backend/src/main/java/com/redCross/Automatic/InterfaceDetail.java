package com.redCross.Automatic;

import lombok.Data;

@Data
public class InterfaceDetail {
    // 应用场景
    private String iterfaceScene;

    // 接口路径
    private String iterfacePath;

    // 接口路径
    private String iterfaceMethod;

    // 输入
    private String inputJson;

    // 输入
    private String inputParam;

    // 输入备注
    private String inputRemarks;

    // 输出
    private String output;

    // 输出备注
    private String outputRemarks;
}
