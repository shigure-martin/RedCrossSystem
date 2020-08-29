package com.redCross.repository;

import com.redCross.entity.CompanyInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author lli.chen
 */
public interface CompanyInfoRepository extends PagingAndSortingRepository<CompanyInfo, Long> {
    List<CompanyInfo> findByDeleted(boolean deleted);
    List<CompanyInfo> findByDeleted(boolean deleted, Sort sort);

    Page<CompanyInfo> findByIndexJsonLikeAndDeleted(String searchCondition, boolean deleted, Pageable pageable);
}
