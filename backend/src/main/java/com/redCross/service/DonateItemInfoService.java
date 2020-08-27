package com.redCross.service;

import com.redCross.entity.DonateItemInfo;
import com.redCross.repository.DonateItemInfoRepository;
import com.redCross.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
    public DonateItemInfoService(DonateItemInfoRepository donateItemInfoRepository) {
        super(donateItemInfoRepository);
        this.donateItemInfoRepository = donateItemInfoRepository;
    }

    public List<DonateItemInfo> getDonateItemInfos(int page, int size, List<OrderRequest> order) {
        Sort sort = getSortBy(order, new DonateItemInfo());
        List<DonateItemInfo> result = donateItemInfoRepository.findByDeleted(false,sort);
        return result;
    }

}