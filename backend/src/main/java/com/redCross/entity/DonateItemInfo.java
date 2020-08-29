package com.redCross.entity;

import com.redCross.constants.ItemType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class DonateItemInfo extends IEntity{
    @ApiModelProperty(value = "捐助者id")
    private Long donorId;

    @ApiModelProperty(value = "受捐者id")
    private Long RecipientId;

    @ApiModelProperty(value = "物品id")
    private Long itemId;

    @ApiModelProperty(value = "物品信息")
    @OneToOne
    @JoinColumn(referencedColumnName = "id", name = "itemId", foreignKey = @ForeignKey(name = "null", value = ConstraintMode
            .NO_CONSTRAINT), insertable = false, updatable = false)
    private ItemInfo itemInfo;

    @ApiModelProperty(value = "捐助记录id")
    private Long recordId;

    @ApiModelProperty(value = "物品数量")
    private Integer itemNum;
}
