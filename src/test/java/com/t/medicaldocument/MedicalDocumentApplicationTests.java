package com.t.medicaldocument;



import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

@SpringBootTest
@MapperScan("com.t.medical.medicaldocument.mapper")
@Slf4j
class MedicalDocumentApplicationTests {

	@Test
	void contextLoads() {
		String end=System.getProperty("user.dir")+"fileName";
		System.out.println(end);
	}
	@Test
	void test(){
		HashMap<String,Object> map = new HashMap<>();
		ArrayList<StringBuilder> ints = new ArrayList<>();
		ints.add(new StringBuilder("sss"));
		System.out.println(ints.toString());
		map.put("ss",ints);

		System.out.println(JSONObject.toJSONString(map));
	}
	void modi(HashMap<Integer, Integer> map){
		for (Integer integer : map.keySet()) {
			map.put(integer,5-integer);
		}
	}
	// @Test
	// void callpyton(){
	// 	BufferedReader bufferReader = null;
	// 	try {
	// 		//创建子进程，调用命令行启动Python程序并传参传递参数
	// 		Process process = Runtime.getRuntime().exec(Cmd.create().toString("D:\\CodeOfJava\\Medical-Document\\pic\\2ee320bcb7eb41e28744b9c39348b5b0\\00.jpg"));
	// 		// 读取Python程序的输出
	// 		bufferReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	// 		String buffer = null;
	// 		while ((buffer = bufferReader.readLine()) != null) {
	// 			log.debug(buffer);
	// 		}
	// 		//当前进程等待子进程执行完毕
	// 		process.waitFor();
	// 	} catch (InterruptedException e) {
	// 		e.printStackTrace();
	// 	} catch (IOException e) {
	// 		e.printStackTrace();
	// 	} finally {
	// 		// try {
	// 		// 	bufferReader.close();
	// 		// } catch (IOException e) {
	// 		// 	e.printStackTrace();
	// 		// }
	// 	}
	// }
	@Test
	void r() throws IOException {
		File file = new File("D:\\CodeOfJava\\Medical-Document\\pdf\\53a042b1.pdf");
		File file1 = new File("53a042b1.pdf");

		FileOutputStream outputStream = new FileOutputStream(file1);
		FileInputStream inputStream = new FileInputStream(file);
		IOUtils.copy(inputStream, outputStream);
	}
}
