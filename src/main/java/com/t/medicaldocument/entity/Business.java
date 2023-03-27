package com.t.medicaldocument.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 *
 * @TableName business
 */
@TableName(value ="business")
@Data
public class Business implements Serializable {
    /**
     * 业务编号ID
     */
    @TableId
    private Long bizId;

    /**
     * 业务价格
     */
    @ApiParam(required = true)
    private BigDecimal bizPrice;

    /**
     * 业务附带积分
     */
    @ApiParam(required = true)
    private Long bizPoint;

    /**
     * 业务状态
     */
    private Integer bizStatus;

    /**
     * 业务创建时间
     */
    private Date createTime;

    /**
     * 业务更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
