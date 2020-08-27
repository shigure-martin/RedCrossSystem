package com.redCross.service;

import com.redCross.entity.CompanyInfo;
import com.redCross.repository.CompanyInfoRepository;
import com.redCross.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lli.chen
 */
@Service
public class CompanyInfoService extends BasicService<CompanyInfo, Long> {

    private CompanyInfoRepository companyInfoRepository;

    @Autowired
    public CompanyInfoService(CompanyInfoRepository companyInfoRepository) {
        super(companyInfoRepository);
        this.companyInfoRepository = companyInfoRepository;
    }

    public List<CompanyInfo> getCompanyInfos(int page, int size, List<OrderRequest> order) {
        Sort sort = getSortBy(order, new CompanyInfo());
        List<CompanyInfo> result = companyInfoRepository.findByDeleted(false,sort);
        return result;
    }
}
