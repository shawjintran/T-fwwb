package com.t.logic.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @TableName pdf_description
 */
@TableName(value ="pdf_description")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PdfDescription implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long pdfDescId;

    /**
     *
     */
    private Long pdfId;

    /**
     *
     */
    private Integer pdfPage;

    /**
     * pdf文件page页的文本json结构
     */
    private Object pdfTextStructure;

    /**
     *
     */
    private String pdfPicUrl;

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
