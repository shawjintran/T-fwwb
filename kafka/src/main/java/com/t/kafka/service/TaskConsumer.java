package com.t.kafka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TaskConsumer {
  @Autowired
  KafkaTemplate<String,Object> kafkaTemplate;
  @KafkaListener(id="MTIzNDU2Nzg5MGFiY2RlZg",topics = "predict-task")
  void listenTask(String object){
    System.out.println(object);
  }
}
