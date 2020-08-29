package com.redCross.entity;

import com.redCross.constants.ItemConfirmStatus;
import com.redCross.constants.ItemType;
import com.redCross.constants.RoleType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ItemInfo extends IEntity {
    @ApiModelProperty(value = "物品名称")
    private String itemName;

    @ApiModelProperty(value = "生产批号")
    private String batchNum;

    @ApiModelProperty(value = "物品类型")
    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @ApiModelProperty(value = "物品描述")
    private String itemDes;

    @ApiModelProperty(value = "物品单价")
    private Double itemPrice;

    @ApiModelProperty(value = "审核信息id")
    private Long confirmId;

    @ApiModelProperty(value = "审核人信息")
    @OneToOne
    @JoinColumn(referencedColumnName = "id", name = "confirmId", foreignKey = @ForeignKey(name = "null", value = ConstraintMode
            .NO_CONSTRAINT), insertable = false, updatable = false)
    private ConfirmInfo confirmInfo;
}