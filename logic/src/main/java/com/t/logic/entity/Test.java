package com.t.logic.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @TableName test
 */
@TableName(value ="test")
@Data
@AllArgsConstructor
public class Test implements Serializable {
    /**
     *
     */
    @TableId
    private Integer a;

    /**
     *
     */
    private Integer b;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
