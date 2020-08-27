package com.redCross.controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.redCross.entity.DonationRecordInfo;
import com.redCross.repository.DonationRecordInfoRepository;
import com.redCross.request.OrderRequest;
import com.redCross.response.BaseResponse;
import com.redCross.response.PageResponse;
import com.redCross.response.SuccessResponse;
import com.redCross.service.DonationRecordInfoService;
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
@RequestMapping("/api/donationrecord")
@Api(value = "/api/donationrecord",tags = "捐助记录信息相关接口")
public class DonationRecordInfoController extends BaseController {
    @Autowired
    private DonationRecordInfoService donationRecordInfoService;

    @Autowired
    private DonationRecordInfoRepository donationRecordInfoRepository;

    @PostMapping
    @ApiOperation(value = "新建捐助记录信息")
    public BaseResponse create(@RequestBody DonationRecordInfo donationRecordInfo) {
        return new SuccessResponse<>(donationRecordInfoService.saveOrUpdate(donationRecordInfo));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据Id获取捐助记录信息")
    public BaseResponse getOne(@PathVariable Long id) {
        DonationRecordInfo donationRecordInfo = donationRecordInfoService.getById(id);
        return new SuccessResponse<>(donationRecordInfo);
    }

    @GetMapping("/list")
    @ApiOperation(value = "分页获取捐助记录信息")
    public BaseResponse getList(@RequestParam(required = false, defaultValue = "0") int page,
                                @RequestParam(required = false, defaultValue = Integer_MAX_VALUE) int size,
                                @RequestParam(required = false) String searchCondition
    ) {
        List<OrderRequest> order = null;
        Pageable pageable = new PageRequest(page,size);
        List<DonationRecordInfo> donationRecordInfos = donationRecordInfoService.getDonationRecordInfos(page,size,order);
        if (!Strings.isNullOrEmpty(searchCondition)) {
            donationRecordInfos = donationRecordInfos.stream().filter(donationRecordInfo -> donationRecordInfo.toString().contains(searchCondition)).collect(Collectors.toList());
        }
        return new SuccessResponse<>(PageResponse.build(donationRecordInfos, pageable));
    }

    @PutMapping
    @ApiOperation(value = "更新捐助记录信息")
    public BaseResponse update(@RequestBody DonationRecordInfo donationRecordInfo) {
        DonationRecordInfo old = donationRecordInfoService.getById(donationRecordInfo.getId());
        Preconditions.checkNotNull(old);
        return new SuccessResponse<>(donationRecordInfoService.saveOrUpdate(donationRecordInfo));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据Id删除捐助记录信息")
    public BaseResponse delete(@PathVariable Long id) {
        return new SuccessResponse<>(donationRecordInfoService.deleteEntity(id));
    }
}
