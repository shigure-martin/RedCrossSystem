package com.redCross.service;

import com.redCross.entity.ItemInfo;
import com.redCross.repository.ItemInfoRepository;
import com.redCross.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<ItemInfo> getItemInfos(int page, int size, List<OrderRequest> order) {
        Sort sort = getSortBy(order, new ItemInfo());
        List<ItemInfo> result = itemInfoRepository.findByDeleted(false,sort);
        return result;
    }
}
