package com.t.kafka;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
//@MapperScan("com.t.logic.mapper")
@EnableSwagger2
public class kfkApplication {

  public static void main(String[] args) {
    SpringApplication.run(kfkApplication.class,args);
  }
}
