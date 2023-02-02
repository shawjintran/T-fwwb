package com.t.medicaldocument.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @TableName pdf_description
 */
@TableName(value ="pdf_description")
@Data
public class PdfDescription implements Serializable {
    /**
     *
     */
    @TableId
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
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
