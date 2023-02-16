package com.t.medicaldocument.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @TableName pdf_file
 */
@TableName(value ="pdf_file")
@Data
public class PdfFile implements Serializable {
    /**
     * pdf文件id
     */
    @TableId
    private Long pdfId;
    /**
     * pdf文件id
     */
    private Long userId;
    /**
     * pdf文件id
     */
    private Long docId;

    /**
     * pdf文件名
     */
    private String pdfFileName;

    /**
     * pdf文件标题
     */
    private String pdfTitle;

    /**
     * pdf文件作者
     */
    private String pdfAuthor;

    /**
     * pdf文件页数
     */
    private Integer pdfPagecount;

    /**
     * pdf文件状态
     */
    private Integer pdfStatus;

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
