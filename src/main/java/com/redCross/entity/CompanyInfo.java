package com.redCross.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class CompanyInfo extends IEntity {

    @ApiModelProperty(value = "单位名称")
    private String companyName;

    @ApiModelProperty(value = "地址id")
    private Long addressId;

    @ApiModelProperty(value = "地址信息")
    @OneToOne
    @JoinColumn(referencedColumnName = "id", name = "addressId", foreignKey = @ForeignKey(name = "null", value = ConstraintMode
            .NO_CONSTRAINT), insertable = false, updatable = false)
    private DeliveryAddressInfo deliveryAddressInfo;

    @ApiModelProperty(value = "单位法人")
    private String LegalPersonName;

    @ApiModelProperty(value = "法人联系电话")
    private String LegalPersonTel;

    @ApiModelProperty(value = "单位联系人")
    private String contact;

    @ApiModelProperty(value = "联系人电话")
    private String contactTel;
}
