package com.redCross.repository;

import com.redCross.entity.DeliveryAddressInfo;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author lli.chen
 */
public interface DeliveryAddressInfoRepository extends PagingAndSortingRepository<DeliveryAddressInfo, Long> {
    List<DeliveryAddressInfo> findByDeleted(boolean deleted);
    List<DeliveryAddressInfo> findByDeleted(boolean deleted, Sort sort);
}
