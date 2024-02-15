package com.t.logic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}