package com.redCross.entity;

import com.redCross.constants.GenderType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Entity
public class DonorInfo extends IEntity {
    @ApiModelProperty(value = "网页昵称")
    private String donorName;

    @ApiModelProperty(value = "网页头像")
    private String donorImg;

    @ApiModelProperty(value = "账户id")
    private Long accountId;

    @ApiModelProperty(value = "性别")
    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @ApiModelProperty(value = "所在省")
    private String locatedProvince;

    @ApiModelProperty(value = "所在城市")
    private String locatedCity;

    @ApiModelProperty(value = "手机号")
    private String phoneNum;

    @ApiModelProperty(value = "捐赠总数")
    private int donationSum;


}
