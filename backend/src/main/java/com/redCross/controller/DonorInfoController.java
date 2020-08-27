package com.redCross.controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.redCross.entity.DonorInfo;
import com.redCross.repository.DonorInfoRepository;
import com.redCross.request.OrderRequest;
import com.redCross.response.BaseResponse;
import com.redCross.response.PageResponse;
import com.redCross.response.SuccessResponse;
import com.redCross.service.DonorInfoService;
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
@RequestMapping("/api/donor")
@Api(value = "/api/donor",tags = "捐助者信息相关接口")
public class DonorInfoController extends BaseController {
    @Autowired
    private DonorInfoService donorInfoService;

    @Autowired
    private DonorInfoRepository donorInfoRepository;

    @PostMapping
    @ApiOperation(value = "新建捐助者信息")
    public BaseResponse create(@RequestBody DonorInfo donorInfo) {
        return new SuccessResponse<>(donorInfoService.saveOrUpdate(donorInfo));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据Id获取捐助者信息")
    public BaseResponse getOne(@PathVariable Long id) {
        DonorInfo donorInfo = donorInfoService.getById(id);
        return new SuccessResponse<>(donorInfo);
    }

    @GetMapping("/list")
    @ApiOperation(value = "分页获取捐助者信息")
    public BaseResponse getList(@RequestParam(required = false, defaultValue = "0") int page,
                                @RequestParam(required = false, defaultValue = Integer_MAX_VALUE) int size,
                                @RequestParam(required = false) String searchCondition
    ) {
        List<OrderRequest> order = null;
        Pageable pageable = new PageRequest(page,size);
        List<DonorInfo> donorInfos = donorInfoService.getDonorInfos(page,size,order);
        if (!Strings.isNullOrEmpty(searchCondition)) {
            donorInfos = donorInfos.stream().filter(donorInfo -> donorInfo.toString().contains(searchCondition)).collect(Collectors.toList());
        }
        return new SuccessResponse<>(PageResponse.build(donorInfos, pageable));
    }

    @PutMapping
    @ApiOperation(value = "更新捐助者信息")
    public BaseResponse update(@RequestBody DonorInfo donorInfo) {
        DonorInfo old = donorInfoService.getById(donorInfo.getId());
        Preconditions.checkNotNull(old);
        return new SuccessResponse<>(donorInfoService.saveOrUpdate(donorInfo));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据Id删除捐助者信息")
    public BaseResponse delete(@PathVariable Long id) {
        return new SuccessResponse<>(donorInfoService.deleteEntity(id));
    }
}
