package com.t.medicaldocument.controller;

import com.t.medicaldocument.service.impl.ChartGPTServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@Slf4j
@RestController
@RequestMapping("/chatGpt")
public class ApiController {

    @Resource
    private ChartGPTServiceImpl chartGPTService;

    /**
     * openAI GPT-3
     *
     * @param gptRequest 条件对象
     * @return 出参对象
     */




    @GetMapping("/ask/{gptRequest}")
    public Object askAi(@PathVariable String gptRequest) {

        String replyStr = chartGPTService.send("请只回复相关医学专业名词以及医学药品,一一列举,最多10个,回复里不要有其他多余的字词,"
            + "不要写出序列,只用逗号连接,如果没有相关的名词或药物就回答:对不起,无相关推荐词"+gptRequest);


        return replyStr;
    }

}
