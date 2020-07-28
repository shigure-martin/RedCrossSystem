package com.redCross.entity;

import com.redCross.constants.RecipitentType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class RecipientInfo extends IEntity {
    @ApiModelProperty(value = "受捐者名称")
    private String recipientName;

    @ApiModelProperty(value = "受捐者类型")
    private RecipitentType recipitentType;

    @ApiModelProperty(value = "网站头像")
    private String recipientImg;

    @ApiModelProperty(value = "联系电话")
    private String telNum;

    @ApiModelProperty(value = "联系邮箱")
    private String email;

    @ApiModelProperty(value = "账户id")
    private Long recipientId;

    @ApiModelProperty(value = "所在省")
    private String province;

    @ApiModelProperty(value = "所在城市")
    private String city;

    @ApiModelProperty(value = "受捐总数")
    private Long totalNum;

}
