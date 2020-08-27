package com.redCross.repository;

import com.redCross.entity.DonateItemInfo;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author lli.chen
 */
public interface DonateItemInfoRepository extends PagingAndSortingRepository<DonateItemInfo, Long> {
    List<DonateItemInfo> findByDeleted(boolean deleted);
    List<DonateItemInfo> findByDeleted(boolean deleted, Sort sort);
}
