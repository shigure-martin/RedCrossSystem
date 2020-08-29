package com.redCross.entity;

import com.redCross.constants.ItemConfirmStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.sql.Timestamp;

@Data
@Entity
public class ConfirmInfo extends IEntity {

    @ApiModelProperty(value = "审核状态")
    @Enumerated(EnumType.STRING)
    private ItemConfirmStatus itemConfirmStatus = ItemConfirmStatus.uncheck;

    @ApiModelProperty(value = "审核账号 id")
    private Long auditorId;

    @ApiModelProperty(value = "审核人")
    private String auditName;

    @ApiModelProperty(value = "审核通过时间")
    private Timestamp auditTime;

}
