-- 数据库初始化
create
    database if not exists springinit;

use
    springinit;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userName     varchar(256)                           null comment '用户昵称',
    userAccount  varchar(256)                           null comment '账号',
    avatarUrl    varchar(1024)                          null comment '用户头像',
    userPassword varchar(512)                           not null comment '密码',
    userProfile  varchar(512)                           null comment '电话',
    userRole     varchar(256) default 'user'            not null comment '用户角色 user - 普通用户 admin - 管理员 ban - 禁号',
    createTime   datetime     default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete     tinyint      default 0                 not null comment '是否删除'
) comment '用户';

-- 帖子表
create table if not exists post
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(512)                       null comment '标题',
    content    text                               null comment '内容',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    thumbNum   int      default 0                 not null comment '点赞数',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '帖子';

-- 帖子点赞表（硬删除）
create table if not exists post_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子点赞';