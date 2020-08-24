package com.redCross.entity;

import com.redCross.constants.DeliveryStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
public class DonationRecordInfo extends IEntity {
    @ApiModelProperty(value = "捐助者id")
    private Long donorId;

    @ApiModelProperty(value = "受捐者id")
    private Long recipientId;

    @ApiModelProperty(value = "物流公司")
    private String logisticsCom;

    @ApiModelProperty(value = "物流单号")
    private String logisticsNum;

    @ApiModelProperty(value = "物流状态")
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @ApiModelProperty(value = "成交时间")
    private Timestamp dealTime;

    @ApiModelProperty(value = "发货时间")
    private Timestamp deliveryTime;

    @ApiModelProperty(value = "地址id")
    private Long addressId;

    @ApiModelProperty(value = "地址信息")
    @OneToOne
    @JoinColumn(referencedColumnName = "id", name = "addressId", foreignKey = @ForeignKey(name = "null", value = ConstraintMode
            .NO_CONSTRAINT), insertable = false, updatable = false)
    private DeliveryAddressInfo deliveryAddressInfo;

    @ApiModelProperty(value = "物品列表")
    @OneToMany
    @JoinColumn(name = "recordId", referencedColumnName = "id", updatable = false, insertable = false)
    private List<DonateItemInfo> donateItemInfos;
}
