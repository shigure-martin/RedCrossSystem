package com.redCross.repository;

import com.redCross.entity.DonationRecordInfo;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author lli.chen
 */
public interface DonationRecordInfoRepository extends PagingAndSortingRepository<DonationRecordInfo, Long> {
    List<DonationRecordInfo> findByDeleted(boolean deleted);
    List<DonationRecordInfo> findByDeleted(boolean deleted, Sort sort);
}
