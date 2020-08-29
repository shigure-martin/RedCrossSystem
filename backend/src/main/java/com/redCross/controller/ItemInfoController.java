package com.redCross.controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.redCross.constants.ItemConfirmStatus;
import com.redCross.constants.RoleType;
import com.redCross.entity.Account;
import com.redCross.entity.ConfirmInfo;
import com.redCross.entity.ItemInfo;
import com.redCross.entity.PersonInfo;
import com.redCross.repository.ItemInfoRepository;
import com.redCross.request.OrderRequest;
import com.redCross.response.BaseResponse;
import com.redCross.response.ErrorResponse;
import com.redCross.response.PageResponse;
import com.redCross.response.SuccessResponse;
import com.redCross.service.ConfirmInfoService;
import com.redCross.service.ItemInfoService;
import com.redCross.service.PersonInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lli.chen
 */
@RestController
@RequestMapping("/api/item")
@Api(value = "/api/item",tags = "物品信息相关接口")
public class ItemInfoController extends BaseController {
    @Autowired
    private ItemInfoService itemInfoService;

    @Autowired
    private ItemInfoRepository itemInfoRepository;

    @Autowired
    private PersonInfoService personInfoService;

    @Autowired
    private ConfirmInfoService confirmInfoService;

    @PostMapping
    @ApiOperation(value = "新建物品信息")
    public BaseResponse create(@RequestBody ItemInfo itemInfo) {
        Preconditions.checkNotNull(itemInfo.getBatchNum(), "生产批号不能为空");
        Preconditions.checkNotNull(itemInfo.getItemName(), "产品名称不能为空");
        ItemInfo old = itemInfoRepository.findByBatchNumAndDeleted(itemInfo.getBatchNum(), false);
        if (old != null) {
            return new ErrorResponse("此产品已经被上传");
        }
        return new SuccessResponse<>(itemInfoService.creatItemInfo(itemInfo));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据Id获取物品信息")
    public BaseResponse getOne(@PathVariable Long id) {
        ItemInfo itemInfo = itemInfoService.getById(id);
        return new SuccessResponse<>(itemInfo);
    }

    @GetMapping("/list")
    @ApiOperation(value = "分页获取物品信息")
    public BaseResponse getList(@RequestParam(required = false, defaultValue = "0") int page,
                                @RequestParam(required = false, defaultValue = Integer_MAX_VALUE) int size,
                                @RequestParam(required = false) String searchCondition
    ) {
        return new SuccessResponse(itemInfoService.getItemInfos(page, size, searchCondition, null));
    }

    @PutMapping
    @ApiOperation(value = "更新物品信息")
    public BaseResponse update(@RequestBody ItemInfo itemInfo) {
        ItemInfo old = itemInfoService.getById(itemInfo.getId());
        Preconditions.checkNotNull(old);
        return new SuccessResponse<>(itemInfoService.creatItemInfo(itemInfo));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据Id删除物品信息")
    public BaseResponse delete(@PathVariable Long id) {
        return new SuccessResponse<>(itemInfoService.deleteEntity(id));
    }

    @ApiOperation(value = "审核捐助物品")
    @PutMapping("/check")
    public BaseResponse check(@RequestParam Long itemId, @RequestParam Long confirmId, @RequestParam boolean isPassed){
        //PersonInfo confirm = personInfoService.getById(confirmId);
        Account confirm = userService.getById(confirmId);
        if(confirm.getRoleType().equals(RoleType.customer)){
            return new ErrorResponse("只有超管才能审核捐助物品");
        }
        ItemInfo itemInfo = itemInfoService.getById(itemId);
        ConfirmInfo confirmInfo = new ConfirmInfo();
        confirmInfo.setAuditorId(confirmId);
        confirmInfo.setAuditName(confirm.getPersonInfo().getPersonName());
        confirmInfo.setAuditTime(new Timestamp(System.currentTimeMillis()));
        if(isPassed){
            confirmInfo.setItemConfirmStatus(ItemConfirmStatus.confirm_sucess);
        } else{
            confirmInfo.setItemConfirmStatus(ItemConfirmStatus.confirm_fail);
        }
        itemInfo.setConfirmId(confirmInfo.getId());
        confirmInfoService.saveOrUpdate(confirmInfo);
        return new SuccessResponse<>(itemInfoService.saveOrUpdate(itemInfo));
    }
}