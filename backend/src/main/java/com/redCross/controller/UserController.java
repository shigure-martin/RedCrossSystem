package com.redCross.controller;

import com.google.common.base.Preconditions;
import com.redCross.constants.RecipitentType;
import com.redCross.constants.RoleType;
import com.redCross.entity.*;
import com.redCross.repository.AccountRepository;
import com.redCross.request.LoginRequest;
import com.redCross.request.OrderRequest;
import com.redCross.response.BaseResponse;
import com.redCross.response.ErrorResponse;
import com.redCross.response.SuccessResponse;
import com.redCross.security.UserAuthenticationProvider;
import com.redCross.service.*;
import com.redCross.utils.EntityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@Api(value = "/api/users", tags = "用户相关接口")
public class UserController extends BaseController {
    @Autowired
    private UserAuthenticationProvider authenticationProvider;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PersonInfoService personInfoService;

    @Autowired
    private DonorInfoService donorInfoService;

    @Autowired
    private RecipientInfoService recipientInfoService;

    @Autowired
    private CompanyInfoService companyInfoService;

    @Autowired
    private UserService userService;
    /**
     * 登陆？
     */
    @PostMapping("/login")
    @ApiOperation(value = "登陆")
    public void login(@RequestBody LoginRequest request) {
        Preconditions.checkNotNull(request.getUsername(), "登陆账号不能为空。");
        Preconditions.checkNotNull(request.getPassword(), "密码不能为空。");
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication authentication = authenticationProvider.authenticate(authRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @PostMapping("create/account")
    @ApiOperation(value = "用户名类型密码注册")
    public BaseResponse createAccount(@RequestParam String loginName, @RequestParam RecipitentType recipitentType,
                                      @RequestParam String password){
        Account old = userService.getByLoginName(loginName);
        if( old != null){
            return new ErrorResponse("用户名不能重复");
        }
        Account account = new Account();
        account.setLoginName(loginName);
        account.setPassword(password);
        account.setPasswordSet(true);
        account.setRecipitentType(recipitentType);
        account.setRoleType(RoleType.customer);
        Account accountNew = userService.saveOrUpdate(account);
        if( account.getRecipitentType().equals(RecipitentType.individual)) {
            PersonInfo personInfo = personInfoService.createPersonInfo(accountNew);
            accountNew.setPersonId(personInfo.getId());
        } else if( account.getRecipitentType().equals(RecipitentType.company)){
            CompanyInfo companyInfo = companyInfoService.createCompanyInfo(accountNew);
            accountNew.setCompanyId(companyInfo.getId());
        }
        DonorInfo donorInfo = donorInfoService.createDonorInfo(accountNew);
        accountNew.setDonorId(donorInfo.getId());
        RecipientInfo recipientInfo = recipientInfoService.createRecipientInfo(accountNew);
        accountNew.setRecipientId(recipientInfo.getId());
        return new SuccessResponse<>(userService.saveOrUpdate(accountNew));
    }

    @GetMapping("/me")
    @ApiOperation(value = "获取个人信息相关")
    public BaseResponse me() {
        Account account = getCurrentAccount();
        if (account.isDeleted()) {
            return new ErrorResponse("该账户已被停用。");
        }
        return new SuccessResponse<>(account);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据 id 获取用户")
    public BaseResponse getOne(@PathVariable Long id) {
        Account account = userService.getById(id);
        return new SuccessResponse<>(account);
    }

    @GetMapping("/list")
    @ApiOperation(value = "分页获取用户")
    public BaseResponse getList(@RequestParam(required = false, defaultValue = "0") int page,
                                @RequestParam(required = false, defaultValue = Integer_MAX_VALUE) int size,
                                @RequestParam(required = false) String searchCondition,
                                @RequestParam(required = false) RoleType roleType) {
        List<OrderRequest> order = null;
        Page<Account> customerInfos = userService.getAccountInfos(searchCondition, roleType, page, size, order);
        return new SuccessResponse<>(customerInfos);
    }

    @PutMapping("/password")
    @ApiOperation(value = "修改密码")
    public BaseResponse changePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        Account account = getCurrentAccount();
        if (oldPassword.equals(account.getPassword())) {
            account.setPassword(newPassword);
            return new SuccessResponse<>(userService.saveOrUpdate(account));
        } else {
            return new ErrorResponse<>("旧密码输入错误。");
        }
    }

    @PutMapping
    @ApiOperation(value = "更新用户信息")
    public BaseResponse updateInfo(@RequestBody Account account) {
        Account currentAccount = userService.getById(account.getId());
        Account old = accountRepository.findByLoginName(account.getLoginName());
        if (old != null && old.getId() != account.getId()) {
            return new ErrorResponse("登陆账号不能重复。");
        }
        EntityUtils.copyProperties(account, currentAccount);

        return new SuccessResponse<>(userService.saveOrUpdate(currentAccount));
    }

    @PutMapping("/reset_password")
    @ApiOperation(value = "重置密码")
    public BaseResponse updatePassword(@RequestParam Long id) {
        Account old = userService.getById(id);
        Preconditions.checkNotNull(old);
        old.setPassword("123456");
        return new SuccessResponse<>(userService.saveOrUpdate(old));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据 id 删除用户")
    public BaseResponse deleteOne(@PathVariable Long id) {
        userService.deleteEntity(id);
        return new SuccessResponse<>();
    }
}