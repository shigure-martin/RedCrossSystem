package com.redCross.repository;

import com.redCross.entity.PersonInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author lli.chen
 */
public interface PersonInfoRepository extends PagingAndSortingRepository<PersonInfo, Long> {
    List<PersonInfo> findByDeleted(boolean deleted);
    List<PersonInfo> findByDeleted(boolean deleted, Sort sort);

    Page<PersonInfo> findByIndexJsonLikeAndDeleted(String searchCondition, boolean deleted, Pageable pageable);
}
