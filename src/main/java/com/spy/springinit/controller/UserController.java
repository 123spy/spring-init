package com.spy.springinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.spy.springinit.annotation.AuthCheck;
import com.spy.springinit.common.BaseResponse;
import com.spy.springinit.common.DeleteRequest;
import com.spy.springinit.common.ErrorCode;
import com.spy.springinit.common.ResultUtils;
import com.spy.springinit.constant.UserConstant;
import com.spy.springinit.exception.BusinessException;
import com.spy.springinit.model.domain.User;
import com.spy.springinit.model.dto.UserQueryRequest;
import com.spy.springinit.model.request.*;
import com.spy.springinit.model.vo.UserVO;
import com.spy.springinit.service.UserService;
import com.spy.springinit.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest, HttpServletRequest request) {
        // 校验
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<UserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        // 校验
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        UserVO userVO = userService.getUserVO(user);
        return ResultUtils.success(userVO);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/get/login")
    public BaseResponse<UserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.getUserVO(user));
    }

    // todo 管理员接口
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        // 校验
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String username = userAddRequest.getUserName();
        String userAccount = userAddRequest.getUserAccount();
        if (StringUtils.isBlank(userAccount)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号为空");
        }
        Long id = userService.addUser(username, userAccount);
        return ResultUtils.success(id);
    }

    // todo 管理员接口
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        // 1. 校验
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2. 删除
        boolean result = userService.removeById(deleteRequest.getId());
        if (!result) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(true);
    }

    // todo 管理员接口
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        if (userUpdateRequest == null || userUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<String> tagList = userUpdateRequest.getTags();
        Gson gson = new Gson();
        String tagListStr = gson.toJson(tagList);
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        // 更新标签
        user.setTags(tagListStr);
        String userPassword = userUpdateRequest.getUserPassword();
        if (!AccountUtils.checkUserPassword(userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码非法");
        }
        String encryptPassword = AccountUtils.getEncryptPassword(userPassword);
        user.setUserPassword(encryptPassword);
        boolean result = userService.updateById(user);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(true);
    }

    // todo 管理员接口
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(user);
    }

    // todo 管理员接口
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        return ResultUtils.success(userService.getUserVO(user));
    }

    // todo 管理员接口
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest, HttpServletRequest request) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int current = userQueryRequest.getCurrent();
        int pageSize = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, pageSize), userService.getQueryWrapper(userQueryRequest));
        return ResultUtils.success(userPage);
    }

    // todo 管理员接口
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest, HttpServletRequest request) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int current = userQueryRequest.getCurrent();
        int pageSize = userQueryRequest.getPageSize();
        // todo 爬虫限制
        Page<User> userPage = userService.page(new Page<>(current, pageSize), userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVOPage = new Page<>(current, pageSize, userPage.getTotal());
        List<UserVO> userVoList = userService.getUserVO(userPage.getRecords());
        userVOPage.setRecords(userVoList);
        return ResultUtils.success(userVOPage);
    }

    // todo 管理员接口
    @PostMapping("/update/my")
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest,
                                              HttpServletRequest request) {
        if (userUpdateMyRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        User user = new User();
        BeanUtils.copyProperties(userUpdateMyRequest, user);
        user.setId(loginUser.getId());
        boolean result = userService.updateById(user);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return ResultUtils.success(true);
    }
}
