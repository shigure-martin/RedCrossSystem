package com.redCross.entity;

import com.redCross.constants.GenderType;
import com.redCross.constants.RoleType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class PersonInfo extends IEntity {
    @ApiModelProperty(value = "个人姓名")
    private String personName;

    @ApiModelProperty(value = "性别")
    @Enumerated(EnumType.STRING)
    private GenderType genderType;

    @ApiModelProperty(value = "地址id")
    private Long addressId;

    @ApiModelProperty(value = "地址信息")
    @OneToOne
    @JoinColumn(referencedColumnName = "id", name = "addressId", foreignKey = @ForeignKey(name = "null", value = ConstraintMode
            .NO_CONSTRAINT), insertable = false, updatable = false)
    private DeliveryAddressInfo deliveryAddressInfo;

    @ApiModelProperty(value = "年龄")
    private Integer personAge;

    @ApiModelProperty(value = "联系电话")
    private String phoneNum;

    @ApiModelProperty(value = "联系邮箱")
    private String email;

    @ApiModelProperty(value = "用户account")
    private Long account;

    @ApiModelProperty(value = "主要类型")
    @Enumerated(EnumType.STRING)
    private RoleType roleType = RoleType.customer;
}
