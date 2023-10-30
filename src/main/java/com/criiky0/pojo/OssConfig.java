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

    @NotBlank
    private String endpoint;

    @NotBlank
    private String bucket;

    @NotBlank
    private String accessKeyId;

    @NotBlank
    private String accessKeySecret;

    @Version
    private Integer version;

    private Integer deleted;

    private static final long serialVersionUID = 1L;
}