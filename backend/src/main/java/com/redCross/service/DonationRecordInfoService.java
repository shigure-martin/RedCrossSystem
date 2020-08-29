package com.redCross.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.redCross.constants.DeliveryStatus;
import com.redCross.entity.*;
import com.redCross.repository.DeliveryAddressInfoRepository;
import com.redCross.repository.DonateItemInfoRepository;
import com.redCross.repository.DonationRecordInfoRepository;
import com.redCross.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author lli.chen
 */
@Service
public class DonationRecordInfoService extends BasicService<DonationRecordInfo, Long> {

    private DonationRecordInfoRepository donationRecordInfoRepository;

    @Autowired
    private DonateItemInfoService donateItemInfoService;

    @Autowired
    private DeliveryAddressInfoRepository deliveryAddressInfoRepository;

    @Autowired
    private DonateItemInfoRepository donateItemInfoRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DonorInfoService donorInfoService;

    @Autowired
    private RecipientInfoService recipientInfoService;

    @Autowired
    public DonationRecordInfoService(DonationRecordInfoRepository donationRecordInfoRepository) {
        super(donationRecordInfoRepository);
        this.donationRecordInfoRepository = donationRecordInfoRepository;
    }

    public Page<DonationRecordInfo> getDonationRecordInfos(int page, int size, String searchCondition, List<OrderRequest> order) {
        Pageable pageable = getPageableBy(null, new PageRequest(page, size), new DonationRecordInfo());
        String searchConditionLike = getLikeBy(searchCondition);
        Page<DonationRecordInfo> result = donationRecordInfoRepository.findByIndexJsonLikeAndDeleted(searchConditionLike, false, pageable);
        return result;
    }

    public List<DonationRecordInfo> createDonationRecordInfo(List<Long> donateItemInfoIds, Long donorId){
        List<DonateItemInfo> donateItemInfos = donateItemInfoRepository.findByDonorIdAndIdInAndDeleted(donorId, donateItemInfoIds, false);
        Set<Long> set = new HashSet<>();
        for (DonateItemInfo donateItemInfo : donateItemInfos) {
            set.add(donateItemInfo.getRecipientId());
        }
        List<Long> recipientList = new ArrayList<>();
        recipientList.addAll(set);
        Map<Long, List<DonateItemInfo>> maps = new LinkedHashMap<>();
        for (Long recipient : recipientList) {
            List<DonateItemInfo> subList = new ArrayList<>();
            for (DonateItemInfo donateItemInfo : donateItemInfos) {
                if (donateItemInfo.getRecipientId() == recipient) {
                    subList.add(donateItemInfo);
                }
            }
            maps.put(recipient, subList);
        }
        List<DonationRecordInfo> result = new ArrayList<>();
        for (Map.Entry<Long, List<DonateItemInfo>> entry : maps.entrySet()) {
            DonationRecordInfo donationRecordInfo = new DonationRecordInfo();
            donationRecordInfo.setRecipientId(entry.getKey());
            donationRecordInfo.setDonorId(entry.getValue().get(0).getDonorId());

            List<DeliveryAddressInfo> deliveryAddressInfos = deliveryAddressInfoRepository.findByCustomerIdAndIsdefaultAddressAndDeleted(entry.getValue().get(0).getRecipientId(), true, false);
            donationRecordInfo.setAddressId(deliveryAddressInfos.get(0).getId());
            donationRecordInfo.setDeliveryStatus(DeliveryStatus.un_delivered);
            result.add(donationRecordInfo);
            for (DonateItemInfo donateItemInfo : entry.getValue()) {
                donateItemInfoService.deleteEntity(donateItemInfo.getId());
            }
        }
        List<DonationRecordInfo> donationRecordInfosNew = Lists.newArrayList(this.saveOrUpdateAll(result));
        for (DonationRecordInfo donationRecordInfo : donationRecordInfosNew) {
            setDonationRecordInfoIndexJson(donationRecordInfo);
        }
        return Lists.newArrayList(this.saveOrUpdateAll(donationRecordInfosNew));
    }

    public void setDonationRecordInfoIndexJson(DonationRecordInfo donationRecordInfo) {
        JSONObject indexJson = new JSONObject();
        StringBuffer stringBuffer = new StringBuffer();
        if (donationRecordInfo.getLogisticsCom() != null) {
            connectStringBuffer(donationRecordInfo.getLogisticsCom(), stringBuffer);
        }
        if (donationRecordInfo.getLogisticsNum() != null) {
            connectStringBuffer(donationRecordInfo.getLogisticsNum(), stringBuffer);
        }
        if (donationRecordInfo.getDeliveryStatus() != null) {
            connectStringBuffer(donationRecordInfo.getDeliveryStatus().toString(), stringBuffer);
        }
        indexJson.put("searchCondition", stringBuffer.toString());
        donationRecordInfo.setIndexJson(indexJson.toString());
    }
}