package com.t.logic.controller;

import com.t.logic.service.impl.ChartGPTServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@Slf4j
@RestController
@RequestMapping("/chatGpt")
@CrossOrigin
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
        String replyStr = chartGPTService.send(
                "请只回复关键词相关医学专业名词以及医学药品," +
                        "最好带有关键词,一一列举,按照相关度排序,列出相关度最高的5个," +
                        "回复里不要有其他多余的字词,"
                        + "不要写出序列,只用逗号连接," +
                        "如果没有相关的名词或药物就回答:对不起,无相关推荐词," +
                        "关键词为"+gptRequest);
        return replyStr;
    }

}
