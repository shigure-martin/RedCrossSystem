package com.redCross.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class RecipientInfo extends IEntity {
    @ApiModelProperty(value = "网站昵称")
    private String recipientName;

    @ApiModelProperty(value = "网站头像")
    private String recipientImg;

    @ApiModelProperty(value = "账户id")
    private Long recipientId;

    @ApiModelProperty(value = "受捐总数")
    private Integer totalNum;

}
