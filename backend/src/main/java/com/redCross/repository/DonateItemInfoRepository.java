package com.redCross.repository;

import com.redCross.entity.DonateItemInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author lli.chen
 */
public interface DonateItemInfoRepository extends PagingAndSortingRepository<DonateItemInfo, Long> {
    List<DonateItemInfo> findByDeleted(boolean deleted);
    List<DonateItemInfo> findByDeleted(boolean deleted, Sort sort);
    List<DonateItemInfo> findByDonorIdAndIdInAnAndDeleted(Long donorId, List<Long> ids, boolean deleted);

    Page<DonateItemInfo> findByIndexJsonLikeAndDeleted(String searchCondition, boolean deleted, Pageable pageable);
}
