package com.redCross.service;

import com.alibaba.fastjson.JSONObject;
import com.redCross.entity.DonateItemInfo;
import com.redCross.entity.RecipientInfo;
import com.redCross.repository.DonateItemInfoRepository;
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
public class DonateItemInfoService extends BasicService<DonateItemInfo, Long> {

    private DonateItemInfoRepository donateItemInfoRepository;

    @Autowired
    private RecipientInfoService recipientInfoService;

    @Autowired
    public DonateItemInfoService(DonateItemInfoRepository donateItemInfoRepository) {
        super(donateItemInfoRepository);
        this.donateItemInfoRepository = donateItemInfoRepository;
    }

    public Page<DonateItemInfo> getDonateItemInfos(int page, int size, String searchCondition, List<OrderRequest> order) {
        Pageable pageable = getPageableBy(null, new PageRequest(page, size), new DonateItemInfo());
        String searchConditionLike = getLikeBy(searchCondition);
        Page<DonateItemInfo> result = donateItemInfoRepository.findByIndexJsonLikeAndDeleted(searchConditionLike, false, pageable);
        return result;
    }

    public DonateItemInfo createDonateItemInfo(DonateItemInfo donateItemInfo) {
        DonateItemInfo donateItemInfoNew = this.saveOrUpdate(donateItemInfo);
        setDonateItemInfoIndexJson(donateItemInfoNew);
        return this.saveOrUpdate(donateItemInfoNew);
    }

    public void setDonateItemInfoIndexJson(DonateItemInfo donateItemInfo) {
        JSONObject indexJson = new JSONObject();
        StringBuffer stringBuffer = new StringBuffer();
        if (donateItemInfo.getItemInfo() != null) {
            connectStringBuffer(donateItemInfo.getItemInfo().getItemName(), stringBuffer);
        }
        if (donateItemInfo.getRecipientId() != null) {
            RecipientInfo recipientInfo = recipientInfoService.getById(donateItemInfo.getRecipientId());
            connectStringBuffer(recipientInfo.getRecipientName(), stringBuffer);
        }
        indexJson.put("searchCondition", stringBuffer.toString());
        donateItemInfo.setIndexJson(indexJson.toString());
    }
}