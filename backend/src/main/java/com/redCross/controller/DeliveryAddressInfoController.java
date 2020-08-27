package com.redCross.controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.redCross.entity.DeliveryAddressInfo;
import com.redCross.repository.DeliveryAddressInfoRepository;
import com.redCross.request.OrderRequest;
import com.redCross.response.BaseResponse;
import com.redCross.response.PageResponse;
import com.redCross.response.SuccessResponse;
import com.redCross.service.DeliveryAddressInfoService;
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
@RequestMapping("/api/deliveryaddress")
@Api(value = "/api/deliveryaddress",tags = "所在地址信息相关接口")
public class DeliveryAddressInfoController extends BaseController {
    @Autowired
    private DeliveryAddressInfoService deliveryAddressInfoService;

    @Autowired
    private DeliveryAddressInfoRepository deliveryAddressInfoRepository;

    @PostMapping
    @ApiOperation(value = "新建所在地址信息")
    public BaseResponse create(@RequestBody DeliveryAddressInfo deliveryAddressInfo) {
        return new SuccessResponse<>(deliveryAddressInfoService.saveOrUpdate(deliveryAddressInfo));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据Id获取所在地址信息")
    public BaseResponse getOne(@PathVariable Long id) {
        DeliveryAddressInfo deliveryAddressInfo = deliveryAddressInfoService.getById(id);
        return new SuccessResponse<>(deliveryAddressInfo);
    }

    @GetMapping("/list")
    @ApiOperation(value = "分页获取所在地址信息")
    public BaseResponse getList(@RequestParam(required = false, defaultValue = "0") int page,
                                @RequestParam(required = false, defaultValue = Integer_MAX_VALUE) int size,
                                @RequestParam(required = false) String searchCondition
    ) {
        List<OrderRequest> order = null;
        Pageable pageable = new PageRequest(page,size);
        List<DeliveryAddressInfo> deliveryAddressInfos = deliveryAddressInfoService.getDeliveryAddressInfos(page,size,order);
        if (!Strings.isNullOrEmpty(searchCondition)) {
            deliveryAddressInfos = deliveryAddressInfos.stream().filter(deliveryAddressInfo -> deliveryAddressInfo.toString().contains(searchCondition)).collect(Collectors.toList());
        }
        return new SuccessResponse<>(PageResponse.build(deliveryAddressInfos, pageable));
    }

    @PutMapping
    @ApiOperation(value = "更新所在地址信息")
    public BaseResponse update(@RequestBody DeliveryAddressInfo deliveryAddressInfo) {
        DeliveryAddressInfo old = deliveryAddressInfoService.getById(deliveryAddressInfo.getId());
        Preconditions.checkNotNull(old);
        return new SuccessResponse<>(deliveryAddressInfoService.saveOrUpdate(deliveryAddressInfo));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据Id删除所在地址信息")
    public BaseResponse delete(@PathVariable Long id) {
        return new SuccessResponse<>(deliveryAddressInfoService.deleteEntity(id));
    }
}
