package com.redCross.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.redCross.constants.RecipitentType;
import com.redCross.constants.RoleType;
import com.redCross.security.JPACryptoConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Account extends IEntity {
    @ApiModelProperty(value = "登陆名")
    @Column(unique = true, nullable = false)
    private String loginName;

    @ApiModelProperty(value = "密码")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Convert(converter = JPACryptoConverter.class)
    private String password = "123456";

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "账户类型:单位、个人")
    private RecipitentType recipitentType;

    
    @ApiModelProperty(value = "单位id")
    private Long companyId;

    @ApiModelProperty(value = "商家信息")
    @OneToOne
    @JoinColumn(referencedColumnName = "id", name = "companyId", foreignKey = @ForeignKey(name = "null", value = ConstraintMode
            .NO_CONSTRAINT), insertable = false, updatable = false)
    private CompanyInfo companyInfo;

    @ApiModelProperty(value = "个人id")
    private Long personId;

    @ApiModelProperty(value = "个人信息")
    @OneToOne
    @JoinColumn(referencedColumnName = "id", name = "personId", foreignKey = @ForeignKey(name = "null", value = ConstraintMode
            .NO_CONSTRAINT), insertable = false, updatable = false)
    private PersonInfo personInfo;

    @ApiModelProperty(value = "捐赠者Id")
    private long donorId;

    @OneToOne
    @JoinColumn(referencedColumnName = "id", name = "donorId", foreignKey = @ForeignKey(name = "null", value = ConstraintMode
            .NO_CONSTRAINT), insertable = false, updatable = false)
    @ApiModelProperty(value = "捐助者")
    private DonorInfo donorInfo;

    @ApiModelProperty(value = "受捐者Id")
    private long recipientId;

    @OneToOne
    @JoinColumn(referencedColumnName = "id", name = "recipientId", foreignKey = @ForeignKey(name = "null", value = ConstraintMode
            .NO_CONSTRAINT), insertable = false, updatable = false)
    @ApiModelProperty(value = "受捐者信息")
    private RecipientInfo recipientInfo;

    @ApiModelProperty(value = "密码是否设定")
    private boolean isPasswordSet = false;

    @ApiModelProperty(value = "手机是否绑定")
    private boolean isTelBinding = false;

    @JsonIgnore
    public String getAuthority() {
        return "ROLE_ACCOUNT";
    }
}
