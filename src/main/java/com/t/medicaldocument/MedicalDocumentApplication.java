package com.t.medicaldocument;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.t.medicaldocument.mapper")
@EnableSwagger2
public class MedicalDocumentApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicalDocumentApplication.class, args);
	}

}
