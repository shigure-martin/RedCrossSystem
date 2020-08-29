package com.redCross.service;

import com.alibaba.fastjson.JSONObject;
import com.redCross.entity.Account;
import com.redCross.entity.PersonInfo;
import com.redCross.repository.PersonInfoRepository;
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
public class PersonInfoService extends BasicService<PersonInfo, Long> {

    private PersonInfoRepository personInfoRepository;

    @Autowired
    public PersonInfoService(PersonInfoRepository personInfoRepository) {
        super(personInfoRepository);
        this.personInfoRepository = personInfoRepository;
    }

    public Page<PersonInfo> getPersonInfos(int page, int size, String searchCondition, List<OrderRequest> order) {
        Pageable pageable = getPageableBy(order, new PageRequest(page, size), new PersonInfo());
        String searchConditionLike = getLikeBy(searchCondition);
        Page<PersonInfo> result = personInfoRepository.findByIndexJsonLikeAndDeleted(searchConditionLike, false, pageable);
        return result;
    }

    public PersonInfo createPersonInfo(Account account){
        PersonInfo personInfo = new PersonInfo();
        personInfo.setAccount(account.getId());
        setPersonInfoIndexJson(personInfo);
        return this.saveOrUpdate(personInfo);
    }

    public PersonInfo updatePersonInfo(PersonInfo personInfo) {
        PersonInfo personInfoNew = this.saveOrUpdate(personInfo);
        setPersonInfoIndexJson(personInfoNew);
        return this.saveOrUpdate(personInfoNew);
    }

    public void setPersonInfoIndexJson(PersonInfo personInfo) {
        JSONObject indexJson = new JSONObject();
        StringBuffer stringBuffer = new StringBuffer();
        if (personInfo.getPersonName() != null) {
            connectStringBuffer(personInfo.getPersonName(), stringBuffer);
        }
        if (personInfo.getGenderType() != null) {
            connectStringBuffer(personInfo.getGenderType().toString(), stringBuffer);
        }
        if (personInfo.getDeliveryAddressInfo() != null) {
            connectStringBuffer(personInfo.getDeliveryAddressInfo().getDeliveryProvince(), stringBuffer);
            connectStringBuffer(personInfo.getDeliveryAddressInfo().getDeliveryCity(), stringBuffer);
        }
        indexJson.put("searchCondition", stringBuffer.toString());
        personInfo.setIndexJson(indexJson.toString());
    }
}