package com.spy.springinit.service;

import com.spy.springinit.model.domain.PostThumb;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spy.springinit.model.domain.User;

/**
 * @author spy
 * @description 针对表【post_thumb(帖子点赞)】的数据库操作Service
 * @createDate 2024-03-07 20:59:09
 */
public interface PostThumbService extends IService<PostThumb> {

    /**
     * 帖子点赞
     *
     * @param postId
     * @param loginUser
     * @return
     */
    int doPostThumb(Long postId, User loginUser);

    /**
     * 帖子点赞（内部事务）
     *
     * @param userId
     * @param postId
     * @return
     */
    int doPostThumbInner(long userId, Long postId);
}
