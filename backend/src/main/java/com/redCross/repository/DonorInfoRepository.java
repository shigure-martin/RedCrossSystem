package com.redCross.repository;

import com.redCross.entity.DonorInfo;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author lli.chen
 */
public interface DonorInfoRepository extends PagingAndSortingRepository<DonorInfo, Long> {
    List<DonorInfo> findByDeleted(boolean deleted);
    List<DonorInfo> findByDeleted(boolean deleted, Sort sort);
}
