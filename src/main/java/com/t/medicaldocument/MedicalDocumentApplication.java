package com.t.medicaldocument;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.t.medicaldocument.mapper")
@EnableSwagger2
@EnableAsync
public class MedicalDocumentApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicalDocumentApplication.class, args);
	}

}
