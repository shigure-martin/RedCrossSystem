package com.redCross.service;

import com.redCross.entity.RecipientInfo;
import com.redCross.repository.RecipientInfoRepository;
import com.redCross.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lli.chen
 */
@Service
public class RecipientInfoService extends BasicService<RecipientInfo, Long> {

    private RecipientInfoRepository recipientInfoRepository;

    @Autowired
    public RecipientInfoService(RecipientInfoRepository recipientInfoRepository) {
        super(recipientInfoRepository);
        this.recipientInfoRepository = recipientInfoRepository;
    }

    public List<RecipientInfo> getRecipientInfos(int page, int size, List<OrderRequest> order) {
        Sort sort = getSortBy(order, new RecipientInfo());
        List<RecipientInfo> result = recipientInfoRepository.findByDeleted(false,sort);
        return result;
    }
}
