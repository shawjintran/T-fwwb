package com.t.medicaldocument.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ChartGPTServiceImpl {

    @Autowired
    RestTemplateBuilder restTemplateBuilder;



    public String send(String gptRequest) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        String apiUrl = "https://api.a20safe.com/api.php?api=36&key=685ebee8d84541abbbacd98a16ca4cd1&text="+gptRequest;
        String response = restTemplate.getForObject(apiUrl, String.class);

        log.info("ChatGPT:"+response);
        // 假设 JSON 字符串已经被解析成为一个 JsonObject 对象
        JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);

// 从 JsonObject 对象中提取出 "data" 数组
        JsonArray dataArray = jsonObject.getAsJsonArray("data");

// 获取数组中的第一个元素
        JsonObject dataObject = dataArray.get(0).getAsJsonObject();

// 从 dataObject 中提取出 "reply" 字段的值
        String reply = dataObject.get("reply").getAsString();
        return reply;

    }
}
