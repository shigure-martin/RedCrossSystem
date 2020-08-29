package com.redCross.repository;

import com.redCross.entity.ItemInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author lli.chen
 */
public interface ItemInfoRepository extends PagingAndSortingRepository<ItemInfo, Long> {
    List<ItemInfo> findByDeleted(boolean deleted);
    List<ItemInfo> findByDeleted(boolean deleted, Sort sort);

    Page<ItemInfo> findByIndexJsonLikeAndDeleted(String searchCondition, boolean deleted, Pageable pageable);

    ItemInfo findByBatchNumAndDeleted(String batchNum, boolean deleted);
}
