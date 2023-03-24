package com.t.medicaldocument;



import com.alibaba.fastjson2.JSONObject;
import com.t.medicaldocument.entity.EsNestedChild;
import com.t.medicaldocument.entity.User;
import com.t.medicaldocument.service.PdfFileService;
import com.t.medicaldocument.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

@SpringBootTest
@MapperScan("com.t.medical.medicaldocument.mapper")
@Slf4j
class MedicalDocumentApplicationTests {

	@Autowired
	UserService userService;

	@Autowired
	PlatformTransactionManager platformTransaction;
	@Autowired
	// 事务定义:事务的一些基础信息，如超时时间、隔离级别、传播属性等
	TransactionDefinition transactionDefinition;
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
	@Test
	void Arr(){
		int[] a= new int[2];
		System.out.println(Arrays.toString(a));
	}
	@Test
	void Tran(){
		TransactionStatus transaction = platformTransaction.
				getTransaction(transactionDefinition);
		for (int i = 0; i < 5; i++) {

		}
		System.out.println("yes");
		platformTransaction.rollback(transaction);
	}
	@Test
	void User(){
		// System.out.println(userService.updateById(new User().setUserId(1L).setUserPoints(3)));
		System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));

	}
	@Test
	void op() throws CloneNotSupportedException {
		ArrayList<EsNestedChild> esNestedChildren = new ArrayList<>();
		long start = System.currentTimeMillis();
		EsNestedChild child = new EsNestedChild();

		for (int i = 0; i < Math.pow(10,10); i++) {

		}
		long finish = System.currentTimeMillis();
		long timeElapsed = finish - start;
		System.out.println(timeElapsed);


	}
}
