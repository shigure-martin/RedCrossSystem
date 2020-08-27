package com.redCross.service;

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
}
