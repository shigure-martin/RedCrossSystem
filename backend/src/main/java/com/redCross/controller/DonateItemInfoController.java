package com.redCross.controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.redCross.constants.ItemConfirmStatus;
import com.redCross.constants.RoleType;
import com.redCross.entity.DonateItemInfo;
import com.redCross.entity.ItemInfo;
import com.redCross.entity.PersonInfo;
import com.redCross.repository.DonateItemInfoRepository;
import com.redCross.request.OrderRequest;
import com.redCross.response.BaseResponse;
import com.redCross.response.ErrorResponse;
import com.redCross.response.PageResponse;
import com.redCross.response.SuccessResponse;
import com.redCross.service.DonateItemInfoService;
import com.redCross.service.ItemInfoService;
import com.redCross.service.PersonInfoService;
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
@RequestMapping("/api/donateitem")
@Api(value = "/api/donateitem",tags = "捐助物品信息相关接口")
public class DonateItemInfoController extends BaseController {
    @Autowired
    private DonateItemInfoService donateItemInfoService;

    @Autowired
    private DonateItemInfoRepository donateItemInfoRepository;

    @Autowired
    PersonInfoService personInfoService;

    @Autowired
    ItemInfoService itemInfoService;

    @PostMapping
    @ApiOperation(value = "新建捐助物品信息")
    public BaseResponse create(@RequestBody DonateItemInfo donateItemInfo) {
        ItemInfo itemInfo = itemInfoService.getById(donateItemInfo.getItemId());
        if(!itemInfo.getItemConfirmStatus().equals(ItemConfirmStatus.confirm_sucess)){
            return new ErrorResponse("添加加的物品是未通过审核的物品");
        }
        return new SuccessResponse<>(donateItemInfoService.saveOrUpdate(donateItemInfo));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据Id获取捐助物品信息")
    public BaseResponse getOne(@PathVariable Long id) {
        DonateItemInfo donateItemInfo = donateItemInfoService.getById(id);
        return new SuccessResponse<>(donateItemInfo);
    }

    @GetMapping("/list")
    @ApiOperation(value = "分页获取捐助物品信息")
    public BaseResponse getList(@RequestParam(required = false, defaultValue = "0") int page,
                                @RequestParam(required = false, defaultValue = Integer_MAX_VALUE) int size,
                                @RequestParam(required = false) String searchCondition
    ) {
        List<OrderRequest> order = null;
        Pageable pageable = new PageRequest(page,size);
        List<DonateItemInfo> donateItemInfos = donateItemInfoService.getDonateItemInfos(page,size,order);
        if (!Strings.isNullOrEmpty(searchCondition)) {
            donateItemInfos = donateItemInfos.stream().filter(donateItemInfo -> donateItemInfo.toString().contains(searchCondition)).collect(Collectors.toList());
        }
        return new SuccessResponse<>(PageResponse.build(donateItemInfos, pageable));
    }

    @PutMapping
    @ApiOperation(value = "更新捐助物品信息")
    public BaseResponse update(@RequestBody DonateItemInfo donateItemInfo) {
        DonateItemInfo old = donateItemInfoService.getById(donateItemInfo.getId());
        Preconditions.checkNotNull(old);
        return new SuccessResponse<>(donateItemInfoService.saveOrUpdate(donateItemInfo));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据Id删除捐助物品信息")
    public BaseResponse delete(@PathVariable Long id) {
        return new SuccessResponse<>(donateItemInfoService.deleteEntity(id));
    }
}