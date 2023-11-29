package com.t.medicaldocument;



import com.alibaba.fastjson2.JSONObject;
import com.t.medicaldocument.Task.TestCallable;
import com.t.medicaldocument.entity.EsNestedChild;
import com.t.medicaldocument.entity.User;
import com.t.medicaldocument.service.PdfFileService;
import com.t.medicaldocument.service.TestService;
import com.t.medicaldocument.service.UserService;
import com.t.medicaldocument.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@SpringBootTest
@MapperScan("com.t.medical.medicaldocument.mapper")
//@Transactional(rollbackFor = Exception.class)
@Slf4j
//@EnableTransactionManagement
class MedicalDocumentApplicationTests {

	@Autowired
	UserService userService;

	@Autowired
	PlatformTransactionManager platformTransaction;
	@Autowired
	// 事务定义:事务的一些基础信息，如超时时间、隔离级别、传播属性等
	TransactionDefinition transactionDefinition;
	@Autowired
	TestService testService;

	@Test
	void contextLoads() {
//		String end=System.getProperty("user.dir")+"fileName";
//		System.out.println(end);
		System.out.println(System.currentTimeMillis());
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
//	@Transactional
	public void Tran(){

		TransactionStatus transaction = platformTransaction.
				getTransaction(new DefaultTransactionDefinition(3));
		System.out.println(transaction);
		try
		{
			we();
		}catch (Exception e){

		}
		String a= String.valueOf(System.currentTimeMillis());
		System.out.println(a.substring(6,9));

		testService.save(new com.t.medicaldocument.entity.Test(Integer.parseInt(a.substring(6,9)),221));
		platformTransaction.rollback(transaction);
		TransactionStatus transaction1 = platformTransaction
				.getTransaction(new DefaultTransactionDefinition(0));
		testService.save(new com.t.medicaldocument.entity.Test(Integer.parseInt(a.substring(6,9)+1),221));
		platformTransaction.commit(transaction1);
	}
//	@Transactional
	public void we(){
//		TransactionStatus transaction = platformTransaction.
//				getTransaction(new DefaultTransactionDefinition(3));
//		System.out.println(transaction);
	String a= String.valueOf(System.currentTimeMillis());
	System.out.println(a.substring(6,9));
	testService.save(new com.t.medicaldocument.entity.Test(Integer.parseInt(a.substring(6,9)+32),221));
//	platformTransaction.commit(transaction);
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
	@Test
	@Transactional
	public void test2() throws ExecutionException, InterruptedException {

		ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
		//设置核心线程数，默认为1
		threadPool.setCorePoolSize(6);
		// 当核心线程都在跑任务，还有多余的任务会存到此处。
		threadPool.setQueueCapacity(100);
		//最大线程数，默认为Integer.MAX_VALUE，如果queueCapacity存满了，还有任务就会启动更多的线程，直到线程数达到maxPoolSize。如果还有任务，则根据拒绝策略进行处理。
		threadPool.setMaxPoolSize(12);
		threadPool.setWaitForTasksToCompleteOnShutdown(true);
		threadPool.setAwaitTerminationSeconds(60 * 80);
		threadPool.setThreadNamePrefix("MyAsync-");
		threadPool.initialize();
		Object savePoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
		List<Future<String>> futures = new ArrayList<>();
		for (Integer i = 0; i < 5; i++) {
			futures.add(threadPool.submit(new TestCallable(i, i + 2, testService)));
		}
		ArrayList<String> tests = new ArrayList<>();
		try {
			for (Future<String> future : futures) {
				if (future.get() != null) {
					tests.add(future.get());
					continue;
				}
				throw new Exception();
			}
		} catch (Exception e)
		{
			TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savePoint);
		}

	}
	@Test
	public void test_jwt(){
		String s = JwtUtils.generateToken(1);
		System.out.println(s);
		Claims claimsByToken = JwtUtils.getClaimsByToken(s);

	}
}
