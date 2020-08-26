package com.redCross.controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.redCross.entity.RecipientInfo;
import com.redCross.repository.RecipientInfoRepository;
import com.redCross.request.OrderRequest;
import com.redCross.response.BaseResponse;
import com.redCross.response.PageResponse;
import com.redCross.response.SuccessResponse;
import com.redCross.service.RecipientInfoService;
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
@RequestMapping("/api/recipient")
@Api(value = "/api/recipient",tags = "受捐者信息相关接口")
public class RecipientInfoController extends BaseController {
    @Autowired
    private RecipientInfoService recipientInfoService;

    @Autowired
    private RecipientInfoRepository recipientInfoRepository;

    @GetMapping("/{id}")
    @ApiOperation(value = "根据Id获取受捐者信息")
    public BaseResponse getOne(@PathVariable Long id) {
        RecipientInfo recipientInfo = recipientInfoService.getById(id);
        return new SuccessResponse<>(recipientInfo);
    }

    @GetMapping("/list")
    @ApiOperation(value = "分页获取受捐者信息")
    public BaseResponse getList(@RequestParam(required = false, defaultValue = "0") int page,
                                @RequestParam(required = false, defaultValue = Integer_MAX_VALUE) int size,
                                @RequestParam(required = false) String searchCondition
    ) {
        List<OrderRequest> order = null;
        Pageable pageable = new PageRequest(page,size);
        List<RecipientInfo> recipientInfos = recipientInfoService.getRecipientInfos(page,size,order);
        if (!Strings.isNullOrEmpty(searchCondition)) {
            recipientInfos = recipientInfos.stream().filter(recipientInfo -> recipientInfo.toString().contains(searchCondition)).collect(Collectors.toList());
        }
        return new SuccessResponse<>(PageResponse.build(recipientInfos, pageable));
    }

    @PutMapping
    @ApiOperation(value = "更新受捐者信息")
    public BaseResponse update(@RequestBody RecipientInfo recipientInfo) {
        RecipientInfo old = recipientInfoService.getById(recipientInfo.getId());
        Preconditions.checkNotNull(old);
        return new SuccessResponse<>(recipientInfoService.updateRecipientInfo(recipientInfo));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据Id删除受捐者信息")
    public BaseResponse delete(@PathVariable Long id) {
        return new SuccessResponse<>(recipientInfoService.deleteEntity(id));
    }
}
