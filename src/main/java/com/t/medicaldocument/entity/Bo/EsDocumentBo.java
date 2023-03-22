package com.t.medicaldocument.entity.Bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
//从es来的
public class EsDocumentBo {


    /**
     * 该段对应的pdf的id
     */
    private Long pdfId;

    /**
     * 该段的页码
     */
    private Integer pdfPage;

    /**
     * pdf文件page页的文本json结构
     * all(是指所有)
     */
    private String all;

    /**
     *图片yrl??
     */
    private String pdfpicurl;


    /**
     * 创建时间
     */
    private String createtime;


    /**
     * 正文
     */
    private String text;


    /**
     * 标题
     */

    private String title;

    /**
     *图片
     */
    private String figure;


    /**
     *图片的标题
     */

    private String figure_caption;

    /**
     * 图表
     */
    private String table;

    /**
     * 图表的标题
     */

    private String table_caption;


    /**
     * 页头
     */
    private String header;


    /**
     * 页脚
     */

    private String footer;

    /**
     * 引用,参考
     */

    private String reference;

    /**
     * 公式
     */
    private String equation;


    public void setAll() {
        this.all =this.text+","+this.title+","+this.figure+","+this.figure_caption+","+this.table+","+this.table_caption+","+this.header+","+this.footer+","+this.reference+","+this.equation;
    }
}
