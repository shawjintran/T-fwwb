package com.t.medicaldocument.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsSearch1 {
    /**
     * pdf的id
     */
    private Long pdfId;
    /**
     * 页码
     */
    private Integer pdfPage;
    /**
     * 创建时间
     */
    private String createtime;
    /**
     * 分数
     */
    private Float score;
}
