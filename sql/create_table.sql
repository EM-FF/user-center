-- auto-generated definition
create table user
(
    id           bigint auto_increment
        primary key,
    username     varchar(128)                       null comment '昵称',
    userAccount  varchar(128)                       null comment '登录账号',
    avatarUrl    varchar(1024)                      null comment '头像',
    gender       tinyint                            null comment '性别',
    userPassword varchar(256)                       not null comment '密码',
    phone        varchar(128)                       null comment '电话',
    email        varchar(128)                       null comment '邮箱',
    userStatus   int      default 0                 not null comment '状态 0正常',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除',
    userRole     int      default 0                 null comment '用户权限 0-普通用户 1-管理员',
    planetCode   varchar(128)                       null comment '星球编号'
)
    comment '用户';

