package com.t.logic.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @TableName document
 */
@TableName(value ="document")
@Data
public class Document implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.INPUT)
    private Long docId;

    /**
     *
     */
    private Long ownId;

    /**
     *
     */
    private String docName;

    /**
     *
     */

    private Integer docCapacity;

    /**
     *
     */

    private Integer docAuth;
    /**
     *
     */
    private Long docSize;

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
