package com.t.medicaldocument.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 * @TableName pdf_file
 */
@TableName(value ="pdf_file")
@Data
@Accessors(chain = true)
public class PdfFile implements Serializable {
    /**
     * pdf文件id
     */
    @TableId
    private Long pdfId;

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
     *
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

    /**
     *
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
