# 数据库初始化
create database if not exists springinit;

use springinit;

-- 用户表
create table user
(
    userName     varchar(256)                           null comment '用户昵称',
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           null comment '账号',
    avatarUrl    varchar(1024)                          null comment '用户头像',
    userPassword varchar(512)                           not null comment '密码',
    phone        varchar(128)                           null comment '电话',
    userRole     varchar(256) default 'user'            not null comment '用户角色 user - 普通用户 admin - 管理员 ban - 禁号',
    tags         varchar(1024)                          null comment '标签 json 列表',
    createTime   datetime     default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete     tinyint      default 0                 not null comment '是否删除'
) comment '用户';