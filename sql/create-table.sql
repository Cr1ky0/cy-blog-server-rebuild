CREATE database if not exists `criik-blog-local`;
use `criik-blog-local`;

create table if not exists `user` (
	`user_id` bigint(20) not null comment '用户id',
	`username` varchar(20) not null comment 'username',
	`nickname` varchar(20) not null comment '昵称',
	`password` varchar(50) not null comment '用户密码',
	`brief` varchar(50) default '该用户没有个人简介！' comment '用户简介',
	`email` varchar(50) not null comment '用户邮箱',
	`avatar` varchar(50) null comment '用户头像',
	`role` enum('visitor','user','admin') default 'user' comment '用户权限',
	`create_at` datetime default now() comment '创建日期',
	`version` INT DEFAULT 1 COMMENT '乐观锁',
    `deleted` INT DEFAULT 0 COMMENT '1 删除，0 未删除',
    constraint pk_user_id primary key (`user_id`),
    unique index `username_unique` (`username`),
    unique index `email_unique` (`email`)
);

create table if not exists `blog` (
	`blog_id` bigint(20) not null comment '文章id',
	`title` varchar(30) null comment '文章标题',
	`content` text null comment '文章内容',
	`likes` int unsigned default 0 comment '文章点赞',
	`views` int unsigned default 0 comment '文章浏览',
	`create_at` datetime default now() comment '创建时间',
	`update_at` datetime default now() comment '更新时间',
	`sort` int unsigned default 0 comment '排序字段',
	`version` INT DEFAULT 1 COMMENT '乐观锁',
    `deleted` INT DEFAULT 0 COMMENT '1 删除，0 未删除',
    `user_id` bigint(20) not null comment '文章所属用户id',
    `menu_id` bigint(20) not null comment '文章所属类别id',
    constraint pk_blog_id primary key (`blog_id`)
);

create table if not exists `comment` (
	`comment_id` bigint(20) not null comment '评论id',
	`content` varchar(500) not null comment '评论内容',
	`likes` int unsigned default 0 comment '评论点赞',
	`username` varchar(20) null comment '评论附带用户',
	`brief` varchar(50) null comment '评论附带简介',
	`create_at` datetime default now() comment '评论创建日期',
	`version` INT DEFAULT 1 COMMENT '乐观锁',
    `deleted` INT DEFAULT 0 COMMENT '1 删除，0 未删除',
	`belong_comment_id` bigint(20) null comment '评论所属父评论id',
	`user_id` bigint(20) null comment '评论所属id',
	`blog_id` bigint(20) not null comment '评论所属文章id',
	constraint pk_comment_id primary key (`comment_id`)
);

create table if not exists `menu` (
	`menu_id` bigint(20) not null comment '菜单id',
	`title` varchar(30) not null comment '菜单title',
	`icon` varchar(30) not null comment '菜单icon',
	`color` varchar(30) not null comment '菜单color',
	`level` int unsigned default 1 comment '菜单level',
	`sort` int unsigned default 0 comment '排序字段',
	`create_at` datetime default now() comment '创建日期',
	`version` INT DEFAULT 1 COMMENT '乐观锁',
    `deleted` INT DEFAULT 0 COMMENT '1 删除，0 未删除',
    `belong_menu_id` bigint(20) null comment '所属菜单id',
    `user_id` bigint(20) not null comment '所属用户id',
    primary key (`menu_id`)
);

create table if not exists `oss_config` (
	`id` bigint(20) not null,
	`endpoint` varchar(50) not null,
	`bucket` varchar(64) not null,
	`access_key_id` varchar(30) not null,
	`access_key_secret` varchar(50) not null,
	`version` INT DEFAULT 1 COMMENT '乐观锁',
    `deleted` INT DEFAULT 0 COMMENT '1 删除，0 未删除',
    primary key (`id`)
);