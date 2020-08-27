package com.redCross.service;

import com.redCross.entity.Account;
import com.redCross.entity.PersonInfo;
import com.redCross.repository.PersonInfoRepository;
import com.redCross.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<PersonInfo> getPersonInfos(int page, int size, List<OrderRequest> order) {
        Sort sort = getSortBy(order, new PersonInfo());
        List<PersonInfo> result = personInfoRepository.findByDeleted(false,sort);
        return result;
    }

    public PersonInfo createPersonInfo(Account account){
        PersonInfo personInfo = new PersonInfo();
        personInfo.setAccount(account.getId());
        return this.saveOrUpdate(personInfo);
    }

    public PersonInfo updatePersonInfo(PersonInfo personInfo){
        PersonInfo old = this.getById(personInfo.getId());
        old.setPersonName(personInfo.getPersonName());
        old.setAddressId(personInfo.getAddressId());
        old.setGenderType(personInfo.getGenderType());
        old.setEmail(personInfo.getEmail());
        old.setPersonAge(personInfo.getPersonAge());
        old.setPhoneNum(personInfo.getPhoneNum());
        PersonInfo personInfoNew = this.saveOrUpdate(old);
        return this.saveOrUpdate(personInfoNew);
    }
}
