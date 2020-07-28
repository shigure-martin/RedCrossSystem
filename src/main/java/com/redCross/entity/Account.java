package com.redCross.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redCross.constants.RoleType;
import com.redCross.security.JPACryptoConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Account extends IEntity {
    @ApiModelProperty(value = "手机号")
    @Column(unique = true, nullable = false)
    private String phoneNum;

    @ApiModelProperty(value = "密码")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Convert(converter = JPACryptoConverter.class)
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @ApiModelProperty(value = "捐赠者Id")
    private long donorId;

    @OneToOne
    @JoinColumn(referencedColumnName = "id", name = "donorId", foreignKey = @ForeignKey(name = "null", value = ConstraintMode
            .NO_CONSTRAINT), insertable = false, updatable = false)
    @ApiModelProperty(value = "捐助者")
    private DonorInfo donorInfo;

    @ApiModelProperty(value = "受捐者Id")
    private long recipientId;

    @ApiModelProperty(value = "密码是否设定")
    private boolean isPasswordSet = false;

    @ApiModelProperty(value = "手机是否绑定")
    private boolean isTelBinding = false;
}
