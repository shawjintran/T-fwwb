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
    public void serachPageByScore(){
        try {
            System.out.println("结果:"+
                    JSON.toJSONString(
                            searchService
                                    .searchPageByScore("图片", 0,10,null,null)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void serachPageByTime(){
        try {
            System.out.println("结果:"+
                    JSON.toJSONString(
                            searchService.
                                    searchPageByTime("双位数", 0,10,120l,null)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void getDocTest(){
        try {
            System.out.println(JSON.toJSONString(searchService.getdoc(1636548881315807200l, "中华")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
