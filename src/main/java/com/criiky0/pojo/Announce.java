package com.criiky0.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @TableName announce
 */
@TableName(value = "announce")
@Data
public class Announce implements Serializable {
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Length(max = 50)
    private String content;

    private Date createAt;

    @Version
    private Integer version;

    private Integer deleted;

    private static final long serialVersionUID = 1L;
}