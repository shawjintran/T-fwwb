package com.t.medicaldocument.entity.Bo;

import com.t.medicaldocument.entity.PdfDescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//从es来的
public class DocumentBo {

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


    private Float score;


    public DocumentBo(Long pdfDescId, Long pdfId, Integer pdfPage, String pdfTextStructure, String pdfPicUrl,Float score){
        this.pdfDescId=pdfDescId;
        this.pdfId=pdfId;
        this.pdfPage=pdfPage;
        this.pdfTextStructure=pdfTextStructure;
        this.pdfPicUrl=pdfPicUrl;
        this.score=score;

    }

}
