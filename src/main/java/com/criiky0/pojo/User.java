package com.criiky0.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    private Long userId;

    private String username;

    private String nickname;

    private String password;

    private String brief;

    private String email;

    private String avatar;

    private Object role;

    private Date createAt;

    @Version
    private Integer version;

    private Integer active;

    private static final long serialVersionUID = 1L;
}