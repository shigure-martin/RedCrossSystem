package com.redCross.controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.redCross.entity.PersonInfo;
import com.redCross.repository.PersonInfoRepository;
import com.redCross.request.OrderRequest;
import com.redCross.response.BaseResponse;
import com.redCross.response.PageResponse;
import com.redCross.response.SuccessResponse;
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
@RequestMapping("/api/person")
@Api(value = "/api/person",tags = "个人信息相关接口")
public class PersonInfoController extends BaseController {
    @Autowired
    private PersonInfoService personInfoService;

    @Autowired
    private PersonInfoRepository personInfoRepository;

    @GetMapping("/{id}")
    @ApiOperation(value = "根据Id获取个人信息")
    public BaseResponse getOne(@PathVariable Long id) {
        PersonInfo personInfo = personInfoService.getById(id);
        return new SuccessResponse<>(personInfo);
    }

    @GetMapping("/list")
    @ApiOperation(value = "分页获取个人信息")
    public BaseResponse getList(@RequestParam(required = false, defaultValue = "0") int page,
                                @RequestParam(required = false, defaultValue = Integer_MAX_VALUE) int size,
                                @RequestParam(required = false) String searchCondition
    ) {
        List<OrderRequest> order = null;
        Pageable pageable = new PageRequest(page,size);
        List<PersonInfo> personInfos = personInfoService.getPersonInfos(page,size,order);
        if (!Strings.isNullOrEmpty(searchCondition)) {
            personInfos = personInfos.stream().filter(personInfo -> personInfo.toString().contains(searchCondition)).collect(Collectors.toList());
        }
        return new SuccessResponse<>(PageResponse.build(personInfos, pageable));
    }

    @PutMapping
    @ApiOperation(value = "更新个人信息")
    public BaseResponse update(@RequestBody PersonInfo personInfo) {
        PersonInfo old = personInfoService.getById(personInfo.getId());
        Preconditions.checkNotNull(old);
        return new SuccessResponse<>(personInfoService.updatePersonInfo(personInfo));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据Id删除个人信息")
    public BaseResponse delete(@PathVariable Long id) {
        return new SuccessResponse<>(personInfoService.deleteEntity(id));
    }
}
