package com.redCross.entity;

import com.redCross.constants.ItemType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

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

}
