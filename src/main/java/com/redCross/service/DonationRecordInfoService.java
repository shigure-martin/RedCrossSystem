package com.redCross.service;

import com.redCross.entity.*;
import com.redCross.repository.DonationRecordInfoRepository;
import com.redCross.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lli.chen
 */
@Service
public class DonationRecordInfoService extends BasicService<DonationRecordInfo, Long> {

    private DonationRecordInfoRepository donationRecordInfoRepository;

    @Autowired
    private DonateItemInfoService donateItemInfoService;

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

    public List<DonationRecordInfo> getDonationRecordInfos(int page, int size, List<OrderRequest> order) {
        Sort sort = getSortBy(order, new DonationRecordInfo());
        List<DonationRecordInfo> result = donationRecordInfoRepository.findByDeleted(false,sort);
        return result;
    }

    public DonationRecordInfo createDonationRecordInfo(List<Long> donateItemInfoIds, Long donorId, Long recipientId){
        Account donorAccount = userService.getById(donorId);
        Account recipientAccount = userService.getById(recipientId);
        DonorInfo donor = donorInfoService.getById(donorAccount.getDonorId());
        RecipientInfo recipient = recipientInfoService.getById(recipientAccount.getRecipientId());
        int sum = 0;
        DonationRecordInfo donationRecordInfo = new DonationRecordInfo();
        List<DonateItemInfo> donateItemInfos = new ArrayList<>();
        for ( Long id:donateItemInfoIds ){
            DonateItemInfo donateItemInfo = donateItemInfoService.getById(id);
            donateItemInfo.setRecordId(donationRecordInfo.getId());
            donateItemInfos.add(donateItemInfo);
            donateItemInfoService.saveOrUpdate(donateItemInfo);
            sum += donateItemInfo.getItemNum();
        }
        recipient.setTotalNum(recipient.getTotalNum()+sum);
        recipientInfoService.saveOrUpdate(recipient);
        donor.setDonationSum(donor.getDonationSum()+sum);
        donorInfoService.saveOrUpdate(donor);
        donationRecordInfo.setDonateItemInfos(donateItemInfos);
        donationRecordInfo.setDonorId(donorId);
        donationRecordInfo.setRecipientId(recipientId);
        return this.saveOrUpdate(donationRecordInfo);
    }
}
