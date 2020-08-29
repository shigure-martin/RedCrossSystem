package com.redCross.repository;

import com.redCross.constants.RoleType;
import com.redCross.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {
    Page<Account> findByDeleted(boolean deleted, Pageable pageable);
    Page<Account> findByLoginNameLikeAndRoleTypeInAndDeleted(String loginName, List<RoleType> roleTypes, boolean deleted, Pageable pageable);
    Page<Account> findByRoleTypeInAndDeleted(RoleType[] roleTypes,boolean deleted, Pageable pageable);
    Account findByLoginName(String loginName);
}
