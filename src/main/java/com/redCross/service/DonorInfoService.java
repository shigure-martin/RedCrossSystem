package com.redCross.service;

import com.redCross.entity.DonorInfo;
import com.redCross.repository.DonorInfoRepository;
import com.redCross.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lli.chen
 */
@Service
public class DonorInfoService extends BasicService<DonorInfo, Long> {

    private DonorInfoRepository donorInfoRepository;

    @Autowired
    public DonorInfoService(DonorInfoRepository donorInfoRepository) {
        super(donorInfoRepository);
        this.donorInfoRepository = donorInfoRepository;
    }

    public List<DonorInfo> getDonorInfos(int page, int size, List<OrderRequest> order) {
        Sort sort = getSortBy(order, new DonorInfo());
        List<DonorInfo> result = donorInfoRepository.findByDeleted(false,sort);
        return result;
    }
}
