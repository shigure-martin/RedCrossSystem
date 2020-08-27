package com.redCross.entity;

import com.redCross.constants.GenderType;
import com.redCross.constants.RecipitentType;
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

    @ApiModelProperty(value = "捐赠总数")
    private Integer donationSum;
}
