package com.t.medicaldocument.entity.Bo;

import com.t.medicaldocument.entity.EsNestedChild;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

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
     *图片url??
     */
    private String pdfpicurl;


    /**
     * 创建时间
     */
    private String createtime;


    private List<EsNestedChild> esfathernested = new ArrayList<>();



}
