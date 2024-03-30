package com.t.logic.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @TableName user
 */
@TableName(value ="user")
@Data
@Accessors(chain = true)
public class User implements Serializable {
    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Long userId;

    /**
     * 用户电话
     */
    @ApiParam(required = true)
    private String userPhone;

    /**
     * 用户密码
     */
    @ApiParam(required = true)
    private String userPwd;


    /**
     * 用户积分
     */
    private Integer userPoints;


    private Integer userCapacity;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     *
     */
    @TableLogic//用于逻辑删除
    @TableField(fill = FieldFill.INSERT)//添加这个注解是为了在后面设置初始值
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
