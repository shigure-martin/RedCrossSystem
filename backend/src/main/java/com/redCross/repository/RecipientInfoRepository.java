package com.redCross.repository;

import com.redCross.entity.RecipientInfo;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author lli.chen
 */
public interface RecipientInfoRepository extends PagingAndSortingRepository<RecipientInfo, Long> {
    List<RecipientInfo> findByDeleted(boolean deleted);
    List<RecipientInfo> findByDeleted(boolean deleted, Sort sort);
}
