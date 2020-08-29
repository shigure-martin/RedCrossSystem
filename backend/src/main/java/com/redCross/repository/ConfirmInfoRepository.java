package com.redCross.repository;

import com.redCross.entity.ConfirmInfo;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ConfirmInfoRepository extends PagingAndSortingRepository<ConfirmInfo, Long> {
    List<ConfirmInfo> findByDeleted(boolean deleted);
    List<ConfirmInfo> findByDeleted(boolean deleted, Sort sort);
}
