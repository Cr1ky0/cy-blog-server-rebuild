package com.criiky0.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @TableName image
 */
@TableName(value ="image")
@Data
public class Image implements Serializable {
    @TableId
    private Long imageId;

    @NotBlank
    private String endpoint;

    @NotBlank
    private String bucket;

    private Date uploadAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date photoTime;

    @Version
    private Integer version;

    private Integer deleted;

    private Long userId;

    private static final long serialVersionUID = 1L;
}