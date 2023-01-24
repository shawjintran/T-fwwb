package com.t.medicaldocument;

import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@MapperScan("com.t.medical.medicaldocument.mapper")
class MedicalDocumentApplicationTests {

	@Test
	void contextLoads() {
		String end=System.getProperty("user.dir")+"fileName";
		System.out.println(end);
	}
	@Test
	void test(){
		String end=System.getProperty("user.dir")+"\\"+"fileName";
		System.out.println(end);

	}

}
