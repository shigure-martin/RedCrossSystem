package com.redCross.controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.redCross.entity.DonationRecordInfo;
import com.redCross.repository.DonationRecordInfoRepository;
import com.redCross.request.OrderRequest;
import com.redCross.response.BaseResponse;
import com.redCross.response.ErrorResponse;
import com.redCross.response.PageResponse;
import com.redCross.response.SuccessResponse;
import com.redCross.service.DonationRecordInfoService;
import com.redCross.service.UserService;
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
    private UserService userService;

    @Autowired
    private DonationRecordInfoRepository donationRecordInfoRepository;

    @PostMapping
    @ApiOperation(value = "新建捐助记录信息")
    public BaseResponse create(@RequestParam(required = false) List<Long> donateItemInfoIds,
                               @RequestParam(required = false) Long donorAccount) {
        if(userService.getById(donorAccount) == null){
            return new ErrorResponse("捐助者不存在");
        }
        return new SuccessResponse<>(donationRecordInfoService.createDonationRecordInfo(donateItemInfoIds, donorAccount));
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
        return new SuccessResponse<>(donationRecordInfoService.getDonationRecordInfos(page, size, searchCondition, null));
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