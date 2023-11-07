package com.criiky0.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @TableName image
 */
@TableName(value ="image")
@Data
public class Image implements Serializable {
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long imageId;

    @NotBlank(message = "fileName不能为空！")
    private String fileName;

    private String endpoint;

    private String bucket;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date photoTime;

    @Version
    private Integer version;

    private Integer deleted;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    private static final long serialVersionUID = 1L;
}