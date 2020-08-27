package com.redCross.service;

import com.redCross.entity.Account;
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

    public CompanyInfo createCompanyInfo(Account account){
        CompanyInfo companyInfo = new CompanyInfo();
        companyInfo.setAccount(account.getId());
        return this.saveOrUpdate(companyInfo);
    }

    public CompanyInfo updateCompanyInfo(CompanyInfo companyInfo){
        CompanyInfo old = this.getById(companyInfo.getId());
        old.setAddressId(companyInfo.getAddressId());
        old.setCompanyName(companyInfo.getCompanyName());
        old.setContact(companyInfo.getContact());
        old.setContactTel(companyInfo.getContactTel());
        old.setLegalPersonName(companyInfo.getLegalPersonName());
        old.setLegalPersonTel(companyInfo.getLegalPersonTel());
        CompanyInfo companyInfoNew = this.saveOrUpdate(old);
        return this.saveOrUpdate(companyInfoNew);
    }
}
