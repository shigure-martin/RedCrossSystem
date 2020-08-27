package com.redCross.service;

import com.redCross.entity.DonationRecordInfo;
import com.redCross.repository.DonationRecordInfoRepository;
import com.redCross.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lli.chen
 */
@Service
public class DonationRecordInfoService extends BasicService<DonationRecordInfo, Long> {

    private DonationRecordInfoRepository donationRecordInfoRepository;

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
}
