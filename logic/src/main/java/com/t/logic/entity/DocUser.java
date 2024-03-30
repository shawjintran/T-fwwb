package com.t.logic.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 * @TableName doc_user
 */
@TableName(value ="doc_user")
@Data
public class DocUser implements Serializable {
    /**
     *
     */
    private Long docId;

    /**
     *
     */
    private Long userId;

    /**
     *
     */
    private Integer userRole;

    /**
     *
     */
    private Integer jointStatus;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

    @TableLogic//用于逻辑删除
    @TableField(fill = FieldFill.INSERT)//添加这个注解是为了在后面设置初始值
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
