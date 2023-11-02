package com.criiky0.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @TableName oss_config
 */
@TableName(value ="oss_config")
@Data
public class OssConfig implements Serializable {
    @TableId
    private Long id;

    @NotBlank(message = "endpoint不能为空！")
    private String endpoint;

    @NotBlank(message = "bucket不能为空！")
    private String bucket;

    @NotBlank(message = "accessKeyId不能为空！")
    private String accessKeyId;

    @NotBlank(message = "accessKeySecret不能为空！")
    private String accessKeySecret;

    private String dir;

    private String callbackUrl;

    @Version
    private Integer version;

    private Integer deleted;

    private static final long serialVersionUID = 1L;
}