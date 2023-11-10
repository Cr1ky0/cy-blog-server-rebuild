-- `criik-blog-local`.blog definition

CREATE TABLE `blog`
(
    `blog_id`   bigint NOT NULL COMMENT '文章id',
    `title`     varchar(30)  DEFAULT NULL COMMENT '文章标题',
    `content`   text COMMENT '文章内容',
    `likes`     int unsigned DEFAULT '0' COMMENT '文章点赞',
    `views`     int unsigned DEFAULT '0' COMMENT '文章浏览',
    `create_at` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_at` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `sort`      int unsigned DEFAULT '0' COMMENT '排序字段',
    `version`   int          DEFAULT '1' COMMENT '乐观锁',
    `deleted`   int          DEFAULT '0' COMMENT '1 删除，0 未删除',
    `user_id`   bigint NOT NULL COMMENT '文章所属用户id',
    `menu_id`   bigint NOT NULL COMMENT '文章所属类别id',
    `collected` tinyint(1)   DEFAULT '0' COMMENT '是否被收藏',
    PRIMARY KEY (`blog_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


-- `criik-blog-local`.comment definition

CREATE TABLE `comment`
(
    `comment_id`        bigint       NOT NULL COMMENT '评论id',
    `content`           varchar(500) NOT NULL COMMENT '评论内容',
    `likes`             int unsigned DEFAULT '0' COMMENT '评论点赞',
    `username`          varchar(20)  DEFAULT NULL COMMENT '评论附带用户',
    `brief`             varchar(50)  DEFAULT NULL COMMENT '评论附带简介',
    `create_at`         datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '评论创建日期',
    `version`           int          DEFAULT '1' COMMENT '乐观锁',
    `deleted`           int          DEFAULT '0' COMMENT '1 删除，0 未删除',
    `belong_comment_id` bigint       DEFAULT NULL COMMENT '评论所属父评论id',
    `user_id`           bigint       DEFAULT NULL COMMENT '评论所属id',
    `blog_id`           bigint       NOT NULL COMMENT '评论所属文章id',
    PRIMARY KEY (`comment_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


-- `criik-blog-local`.image definition

CREATE TABLE `image`
(
    `image_id`   bigint      NOT NULL COMMENT 'image_id',
    `endpoint`   varchar(50) NOT NULL COMMENT 'endpoint',
    `bucket`     varchar(64) NOT NULL COMMENT 'bucket',
    `upload_at`  datetime DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    `photo_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '照片时间',
    `version`    int      DEFAULT '1' COMMENT '乐观锁',
    `deleted`    int      DEFAULT '0' COMMENT '1 删除，0 未删除',
    `user_id`    bigint      NOT NULL COMMENT '所属用户id',
    `file_name`  varchar(50) NOT NULL COMMENT '文件名',
    PRIMARY KEY (`image_id`),
    UNIQUE KEY `file_name_unique_cons` (`file_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


-- `criik-blog-local`.menu definition

CREATE TABLE `menu`
(
    `menu_id`        bigint      NOT NULL COMMENT '菜单id',
    `title`          varchar(30) NOT NULL COMMENT '菜单title',
    `icon`           varchar(30) NOT NULL COMMENT '菜单icon',
    `color`          varchar(30) NOT NULL COMMENT '菜单color',
    `level`          int unsigned DEFAULT '1' COMMENT '菜单level',
    `sort`           int unsigned DEFAULT '0' COMMENT '排序字段',
    `version`        int          DEFAULT '1' COMMENT '乐观锁',
    `deleted`        int          DEFAULT '0' COMMENT '1 删除，0 未删除',
    `belong_menu_id` bigint       DEFAULT NULL COMMENT '所属菜单id',
    `user_id`        bigint      NOT NULL COMMENT '所属用户id',
    `create_at`      datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
    PRIMARY KEY (`menu_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


-- `criik-blog-local`.oss_config definition

CREATE TABLE `oss_config`
(
    `id`                bigint      NOT NULL,
    `endpoint`          varchar(50) NOT NULL,
    `bucket`            varchar(64) NOT NULL,
    `access_key_id`     varchar(30) NOT NULL,
    `access_key_secret` varchar(50) NOT NULL,
    `version`           int         DEFAULT '1' COMMENT '乐观锁',
    `deleted`           int         DEFAULT '0' COMMENT '1 删除，0 未删除',
    `dir`               varchar(30) DEFAULT '' COMMENT '上传文件夹',
    `callback_url`      varchar(50) DEFAULT '' COMMENT '回调url',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;


-- `criik-blog-local`.`user` definition

CREATE TABLE `user`
(
    `user_id`   bigint      NOT NULL COMMENT '用户id',
    `username`  varchar(20) NOT NULL COMMENT 'username',
    `nickname`  varchar(20) NOT NULL COMMENT '昵称',
    `password`  varchar(50) NOT NULL COMMENT '用户密码',
    `brief`     varchar(50)                     DEFAULT '该用户没有个人简介！' COMMENT '用户简介',
    `email`     varchar(50) NOT NULL COMMENT '用户邮箱',
    `avatar`    varchar(50)                     DEFAULT NULL,
    `role`      enum ('visitor','user','admin') DEFAULT 'user' COMMENT '用户权限',
    `create_at` datetime                        DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
    `version`   int                             DEFAULT '1' COMMENT '乐观锁',
    `deleted`   int                             DEFAULT '0' COMMENT '1 删除，0 未删除',
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `username_unique` (`username`),
    UNIQUE KEY `email_unique` (`email`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;