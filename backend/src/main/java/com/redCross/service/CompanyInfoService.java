package com.redCross.service;

import com.alibaba.fastjson.JSONObject;
import com.redCross.entity.Account;
import com.redCross.entity.CompanyInfo;
import com.redCross.repository.CompanyInfoRepository;
import com.redCross.request.OrderRequest;
import org.checkerframework.checker.units.qual.C;
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
public class CompanyInfoService extends BasicService<CompanyInfo, Long> {

    private CompanyInfoRepository companyInfoRepository;

    @Autowired
    public CompanyInfoService(CompanyInfoRepository companyInfoRepository) {
        super(companyInfoRepository);
        this.companyInfoRepository = companyInfoRepository;
    }

    public Page<CompanyInfo> getCompanyInfos(int page, int size, String searchCondition, List<OrderRequest> order) {
        Pageable pageable = getPageableBy(null, new PageRequest(page, size), new CompanyInfo());
        String searchConditionLike = getLikeBy(searchCondition);
        Page<CompanyInfo> result = companyInfoRepository.findByIndexJsonLikeAndDeleted(searchConditionLike, false, pageable);
        return result;
    }

    public CompanyInfo createCompanyInfo(Account account){
        CompanyInfo companyInfo = new CompanyInfo();
        companyInfo.setAccount(account.getId());
        setCompanyInfoIndexJson(companyInfo);
        return this.saveOrUpdate(companyInfo);
    }

    public CompanyInfo updateCompanyInfo(CompanyInfo companyInfo) {
        CompanyInfo companyInfoNew = this.saveOrUpdate(companyInfo);
        setCompanyInfoIndexJson(companyInfoNew);
        return this.saveOrUpdate(companyInfoNew);
    }

    public void setCompanyInfoIndexJson(CompanyInfo companyInfo) {
        JSONObject indexJson = new JSONObject();
        StringBuffer stringBuffer = new StringBuffer();
        if (companyInfo.getCompanyName() != null) {
            connectStringBuffer(companyInfo.getCompanyName(), stringBuffer);
        }
        if (companyInfo.getLegalPersonName() != null) {
            connectStringBuffer(companyInfo.getLegalPersonName(), stringBuffer);
        }
        if (companyInfo.getContact() != null) {
            connectStringBuffer(companyInfo.getContact(), stringBuffer);
        }
        if (companyInfo.getDeliveryAddressInfo() != null) {
            connectStringBuffer(companyInfo.getDeliveryAddressInfo().getDeliveryProvince(), stringBuffer);
            connectStringBuffer(companyInfo.getDeliveryAddressInfo().getDeliveryCity(), stringBuffer);
        }
        indexJson.put("searchCondition", stringBuffer.toString());
        companyInfo.setIndexJson(indexJson.toString());
    }
}