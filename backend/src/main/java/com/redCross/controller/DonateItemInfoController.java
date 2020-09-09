package com.redCross.controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.redCross.constants.ItemConfirmStatus;
import com.redCross.constants.RoleType;
import com.redCross.entity.DonateItemInfo;
import com.redCross.entity.DonorInfo;
import com.redCross.entity.ItemInfo;
import com.redCross.entity.PersonInfo;
import com.redCross.repository.DonateItemInfoRepository;
import com.redCross.request.OrderRequest;
import com.redCross.response.BaseResponse;
import com.redCross.response.ErrorResponse;
import com.redCross.response.PageResponse;
import com.redCross.response.SuccessResponse;
import com.redCross.service.DonateItemInfoService;
import com.redCross.service.DonorInfoService;
import com.redCross.service.ItemInfoService;
import com.redCross.service.PersonInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.SpringApplicationEvent;
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
    private DonorInfoService donorInfoService;

    @Autowired
    PersonInfoService personInfoService;

    @Autowired
    ItemInfoService itemInfoService;

    @PostMapping
    @ApiOperation(value = "新建捐助物品信息")
    public BaseResponse create(@RequestBody DonateItemInfo donateItemInfo) {
        Preconditions.checkNotNull(donateItemInfo.getDonorId(), "缺少捐助者id");
        //Preconditions.checkNotNull(donateItemInfo.getRecipientId(), "缺少受捐者id");
        Preconditions.checkNotNull(donateItemInfo.getItemId(), "缺少物品id");
        DonorInfo donorInfo = donorInfoService.getById(donateItemInfo.getDonorId());
        if (donorInfo == null) {
            return new ErrorResponse("不存在该受捐者");
        }
        return new SuccessResponse<>(donateItemInfoService.createDonateItemInfo(donateItemInfo));
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
        return new SuccessResponse<>(donateItemInfoService.getDonateItemInfos(page, size, searchCondition, null));
    }

    @PutMapping
    @ApiOperation(value = "更新捐助物品信息")
    public BaseResponse update(@RequestBody DonateItemInfo donateItemInfo) {
        DonateItemInfo old = donateItemInfoService.getById(donateItemInfo.getId());
        Preconditions.checkNotNull(old);
        return new SuccessResponse<>(donateItemInfoService.createDonateItemInfo(donateItemInfo));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据Id删除捐助物品信息")
    public BaseResponse delete(@PathVariable Long id) {
        return new SuccessResponse<>(donateItemInfoService.deleteEntity(id));
    }
}