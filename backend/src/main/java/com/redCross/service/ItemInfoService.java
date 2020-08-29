package com.redCross.service;

import com.alibaba.fastjson.JSONObject;
import com.redCross.constants.ItemType;
import com.redCross.entity.ItemInfo;
import com.redCross.repository.ItemInfoRepository;
import com.redCross.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lli.chen
 */
@Service
public class ItemInfoService extends BasicService<ItemInfo, Long> {

    private ItemInfoRepository itemInfoRepository;

    @Autowired
    public ItemInfoService(ItemInfoRepository itemInfoRepository) {
        super(itemInfoRepository);
        this.itemInfoRepository = itemInfoRepository;
    }

    public Page<ItemInfo> getItemInfos(int page, int size, String searchCondition, List<OrderRequest> order) {
        Pageable pageable = getPageableBy(null, new PageRequest(page, size), new ItemInfo());
        String searchConditionLike = getLikeBy(searchCondition);
        Page<ItemInfo> result = itemInfoRepository.findByIndexJsonLikeAndDeleted(searchConditionLike, false, pageable);
        return result;
    }

    public ItemInfo creatItemInfo(ItemInfo itemInfo) {
        ItemInfo itemInfoNew = this.saveOrUpdate(itemInfo);
        setItemInfoIndexJson(itemInfoNew);
        return this.saveOrUpdate(itemInfoNew);
    }

    public void setItemInfoIndexJson(ItemInfo itemInfo) {
        JSONObject indexJson = new JSONObject();
        StringBuffer stringBuffer = new StringBuffer();
        if (itemInfo.getItemName() != null) {
            connectStringBuffer(itemInfo.getItemName(), stringBuffer);
        }
        if (itemInfo.getBatchNum() != null) {
            connectStringBuffer(itemInfo.getBatchNum(), stringBuffer);
        }
        if (itemInfo.getConfirmId() != null) {
            connectStringBuffer(itemInfo.getConfirmInfo().getItemConfirmStatus().toString(), stringBuffer);
        }
        if (itemInfo.getItemDes() != null) {
            connectStringBuffer(itemInfo.getItemDes(), stringBuffer);
        }
        if (itemInfo.getItemPrice() != null) {
            connectStringBuffer(itemInfo.getItemPrice().toString(), stringBuffer);
        }
        indexJson.put("searchCondition", stringBuffer.toString());
        itemInfo.setIndexJson(indexJson.toString());
    }
}
