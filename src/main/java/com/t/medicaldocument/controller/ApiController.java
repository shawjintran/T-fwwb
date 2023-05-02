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





        String replyStr = chartGPTService.send("请只回复相关医学名词以及医学药品,一一列举,不要说其他任何修饰词:"+gptRequest);


        return replyStr;
    }

}
