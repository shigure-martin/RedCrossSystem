package com.redCross.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
@ApiModel(value = "收货地址")
public class DeliveryAddressInfo extends IEntity {

    @ApiModelProperty(value = "客户id")
    private Long customerId;

    @ApiModelProperty(value = "所在省")
    private String deliveryProvince;

    @ApiModelProperty(value = "所在城市")
    private String deliveryCity;

    @ApiModelProperty(value = "详细地址")
    private String deliveryDetail;

    @ApiModelProperty(value = "收件人")
    private String deliveryName;

    @ApiModelProperty(value = "收货电话")
    private String deliveryPhone;

    @ApiModelProperty(value = "是否默认")
    private boolean isdefaultAddress = false;
}
