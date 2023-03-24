package com.t.medicaldocument.ES;


import com.alibaba.fastjson.JSON;
import com.t.medicaldocument.service.impl.SearchServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
@MapperScan("com.t.medical.medicaldocument.mapper")
@Slf4j
public class EsSearch {

    @Autowired
    SearchServiceImpl searchService;


    @Test
    public void serachPage(){
        try {
            System.out.println("结果:"+ JSON.toJSONString(searchService.searchPage("内容", 0, 10,1l)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void getDocTest(){
        try {
            System.out.println(searchService.getdoc(120l, "图片"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
