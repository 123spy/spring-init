package com.spy.usercenter.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户更新请求(自己)
 *
 * @TableName user
 */
@Data
public class UserUpdateMyRequest implements Serializable {
    /**
     * 用户昵称
     */
    private String userName;

    private static final long serialVersionUID = 1L;
}