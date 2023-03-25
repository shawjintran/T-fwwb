package com.t.medicaldocument.entity.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsSearchVo {
    /**
     * 根据pdfId对应的文献标题
     */
    private String title;
    /**
     * pdf的id
     */
    private Long pdfId;
    /**
     * 命中页数的字符串
     */
    private String pdfPages;

    /**
     * 分数
     */
    private Float score;
    /**
     * 创建时间
     */
    private String createtime;
}
