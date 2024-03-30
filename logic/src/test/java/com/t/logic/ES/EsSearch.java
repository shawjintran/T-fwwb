package com.t.logic.ES;


import com.alibaba.fastjson.JSON;
import com.t.logic.service.impl.SearchServiceImpl;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
                                    .searchPageByScore("肿瘤", 0,10,4l,0l)));
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
            System.out.println(JSON.toJSONString(searchService.getdoc(19l, "肿瘤")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
