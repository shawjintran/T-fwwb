package com.t.medicaldocument.entity.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
//搜索展示的对象,
public class SearchShow {
    /**
     * 标题
     */
    private String title;
    /**
     * 大约50字的内容
     */
    private String text;
    /**
     * 图片展示
     */
    private String imgUrl;
    /**
     * pdf的id
     */
    private Long pdfId;
    /**
     * pdf的存储url
     */
    private String pdfUrl;
    /**
     * pdf的命中页数
     */
    //直接在java里拼接
    private String pageString;
    /**
     * pdf的命中分数
     */
    private Float score;




}
