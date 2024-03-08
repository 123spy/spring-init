package com.spy.springinit.controller;

import com.spy.springinit.common.BaseResponse;
import com.spy.springinit.common.ErrorCode;
import com.spy.springinit.common.ResultUtils;
import com.spy.springinit.exception.BusinessException;
import com.spy.springinit.model.domain.User;
import com.spy.springinit.model.dto.postthumb.PostThumbAddRequest;
import com.spy.springinit.service.PostThumbService;
import com.spy.springinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/post_thumb")
@Slf4j
public class PostThumbController {

    @Resource
    private UserService userService;

    @Resource
    private PostThumbService postThumbService;

    @PostMapping("/")
    public BaseResponse<Integer> doThumb(@RequestBody PostThumbAddRequest postThumbAddRequest, HttpServletRequest request) {
        if(postThumbAddRequest == null || postThumbAddRequest.getPostId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 点赞需要登录
        User loginUser = userService.getLoginUser(request);
        Long postId = postThumbAddRequest.getPostId();
        int result = postThumbService.doPostThumb(postId, loginUser);
        return ResultUtils.success(result);
    }
}
