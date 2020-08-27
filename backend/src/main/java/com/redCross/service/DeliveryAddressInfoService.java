package com.redCross.service;

import com.redCross.entity.DeliveryAddressInfo;
import com.redCross.repository.DeliveryAddressInfoRepository;
import com.redCross.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lli.chen
 */
@Service
public class DeliveryAddressInfoService extends BasicService<DeliveryAddressInfo, Long> {

    private DeliveryAddressInfoRepository deliveryAddressInfoRepository;

    @Autowired
    public DeliveryAddressInfoService(DeliveryAddressInfoRepository deliveryAddressInfoRepository) {
        super(deliveryAddressInfoRepository);
        this.deliveryAddressInfoRepository = deliveryAddressInfoRepository;
    }

    public List<DeliveryAddressInfo> getDeliveryAddressInfos(int page, int size, List<OrderRequest> order) {
        Sort sort = getSortBy(order, new DeliveryAddressInfo());
        List<DeliveryAddressInfo> result = deliveryAddressInfoRepository.findByDeleted(false,sort);
        return result;
    }
}
