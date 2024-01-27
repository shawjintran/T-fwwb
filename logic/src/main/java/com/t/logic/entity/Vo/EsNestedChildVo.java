package com.t.logic.entity.Vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsNestedChildVo implements Cloneable{
    private Integer page;
    private String estype;
    private String esvalue;
}
