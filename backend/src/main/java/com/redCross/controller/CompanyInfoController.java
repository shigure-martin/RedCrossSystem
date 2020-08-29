package com.redCross.controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.redCross.constants.RecipitentType;
import com.redCross.entity.Account;
import com.redCross.entity.CompanyInfo;
import com.redCross.repository.CompanyInfoRepository;
import com.redCross.request.OrderRequest;
import com.redCross.response.BaseResponse;
import com.redCross.response.ErrorResponse;
import com.redCross.response.PageResponse;
import com.redCross.response.SuccessResponse;
import com.redCross.service.CompanyInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lli.chen
 */
@RestController
@RequestMapping("/api/company")
@Api(value = "/api/company",tags = "单位信息相关接口")
public class CompanyInfoController extends BaseController {
    @Autowired
    private CompanyInfoService companyInfoService;

    @Autowired
    private CompanyInfoRepository companyInfoRepository;

    @PostMapping
    @ApiOperation(value = "新建单位信息（不可用）")
    public BaseResponse create(@RequestBody CompanyInfo companyInfo) {
        Preconditions.checkNotNull(companyInfo.getAccount());
        Account account = userService.getById(companyInfo.getAccount());
        if (account.getRecipitentType() == RecipitentType.individual) {
            return new ErrorResponse("该账号是个人账号");
        }
        return new SuccessResponse<>(companyInfoService.saveOrUpdate(companyInfo));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据Id获取单位信息")
    public BaseResponse getOne(@PathVariable Long id) {
        CompanyInfo companyInfo = companyInfoService.getById(id);
        return new SuccessResponse<>(companyInfo);
    }

    @GetMapping("/list")
    @ApiOperation(value = "分页获取单位信息")
    public BaseResponse getList(@RequestParam(required = false, defaultValue = "0") int page,
                                @RequestParam(required = false, defaultValue = Integer_MAX_VALUE) int size,
                                @RequestParam(required = false) String searchCondition
    ) {
        return new SuccessResponse<>(companyInfoService.getCompanyInfos(page, size, searchCondition, null));
    }

    @PutMapping
    @ApiOperation(value = "更新单位信息")
    public BaseResponse update(@RequestBody CompanyInfo companyInfo) {
        CompanyInfo old = companyInfoService.getById(companyInfo.getId());
        Preconditions.checkNotNull(old);
        return new SuccessResponse<>(companyInfoService.updateCompanyInfo(companyInfo));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据Id删除单位信息")
    public BaseResponse delete(@PathVariable Long id) {
        return new SuccessResponse<>(companyInfoService.deleteEntity(id));
    }
}
