package com.t.logic;



//import cn.caohd.seata.async.util.SeataAsyncUtil;
import com.alibaba.fastjson2.JSONObject;
import com.mongodb.client.result.UpdateResult;
import com.t.logic.entity.EsNestedChild;
import com.t.logic.entity.Likes;
import com.t.logic.service.LikesService;
import com.t.logic.service.TestService;
import com.t.logic.service.UserService;
import com.t.logic.service.impl.LikeServiceImpl;
import com.t.logic.utils.DOIParse;
import com.t.logic.utils.JwtUtils;
import com.t.logic.Task.TestCallable;
import com.t.logic.utils.WeChatParse;
import io.jsonwebtoken.Claims;

import io.seata.spring.annotation.GlobalTransactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@SpringBootTest
//@EnableAutoConfiguration(exclude= DataSourceAutoConfiguration.class)
@MapperScan("com.t.logic.mapper")
//@Transactional(rollbackFor = Exception.class)
@Slf4j
// 添加这部分
@ComponentScan({"cn.caohd.seata.async.*"})
//@EnableTransactionManagement
class LogicApplicationTests {

	@Autowired
	UserService userService;


	@Autowired
	// 事务定义:事务的一些基础信息，如超时时间、隔离级别、传播属性等
	TransactionDefinition transactionDefinition;
//	@Resource
//	SeataAsyncUtil seataAsyncUtil;
	@Autowired
	PlatformTransactionManager platformTransaction;
	@Autowired
	TestService testService;
	@Autowired
	MongoTemplate mongoTemplate;
	static volatile boolean is_OK=true;
	@Autowired
	LikeServiceImpl likesService;
//	LikesService likesService;

//	@Test
//	void testMongo() {
//		Optional<Likes> byId = likesService.findById(3);
//		if (byId.isPresent()) {
//			Likes likes = byId.get();
//			System.out.println(likes);
//		}
//	}
	@Test
	void testtest(){
//		Boolean aBoolean = likesService.addLike(4L, 1L);
		Boolean aBoolean = likesService.delLike(4L, 1L);
		System.out.println(aBoolean);
	}
	@Test
	void wechattest(){
		 WeChatParse.parse("https://mp.weixin.qq.com/s/7SscEje7zc0HtyqKd_Fmkg");
	}
	@Test
	void doitest(){
		 DOIParse.parse("10.1007/s10032-021-00384-2");
	}

	@Test
	void test1() {
		int size=3-1;
		CountDownLatch child = new CountDownLatch(size);
		ArrayList<Boolean> childResponse = new ArrayList<>();
		CountDownLatch main = new CountDownLatch(1);
		ExecutorService executorService = Executors.newCachedThreadPool();
		final int[] s = {8};

			executorService.execute(()->{
				TransactionStatus transaction = platformTransaction
						.getTransaction(new DefaultTransactionDefinition());
				try{
					testService.save(new com.t.logic.entity.Test(s[0]++, s[0]++));
					child.countDown();
					childResponse.add(Boolean.TRUE);
					System.out.println(Thread.currentThread().getName() + "：准备就绪,等待其他线程结果,判断是否事务提交");
					main.await();
					if (is_OK) {
						platformTransaction.commit(transaction);
						System.out.println(Thread.currentThread().getName() + "：事务提交");
					} else {
						platformTransaction.rollback(transaction);
						System.out.println(Thread.currentThread().getName() + "：事务回滚");
					}
				} catch (Exception e) {
					childResponse.add(Boolean.FALSE);
					child.countDown();
					System.out.println(Thread.currentThread().getName() + "：事务回滚");
					platformTransaction.rollback(transaction);
				}
			});
			executorService.execute(()->{
				TransactionStatus transaction = platformTransaction
						.getTransaction(new DefaultTransactionDefinition());
				try{
					testService.save(new com.t.logic.entity.Test(s[0]++, s[0]++));
					int a=3/0;
					child.countDown();
					childResponse.add(Boolean.TRUE);
					System.out.println(Thread.currentThread().getName() + "：准备就绪,等待其他线程结果,判断是否事务提交");
					main.await();
					if (is_OK) {
						platformTransaction.commit(transaction);
						System.out.println(Thread.currentThread().getName() + "：事务提交");
					} else {
						platformTransaction.rollback(transaction);
						System.out.println(Thread.currentThread().getName() + "：事务回滚");
					}
				} catch (Exception e) {
					childResponse.add(Boolean.FALSE);
					child.countDown();
					System.out.println(Thread.currentThread().getName() + "：事务回滚");
					platformTransaction.rollback(transaction);
				}
			});
		try {
			child.await();
			for (Boolean resp : childResponse) {
				if (!resp) {
					//如果有一个子线程执行失败了，则改变mainResult，让所有子线程回滚
					System.out.println(Thread.currentThread().getName()+":有线程执行失败，标志位设置为false");
					is_OK = false;
					break;
				}
			}
			//主线程获取结果成功，让子线程开始根据主线程的结果执行（提交或回滚）
			main.countDown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//	@Test
//	@GlobalTransactional
//	@Transactional
//	void sea() throws Exception {
//	testService.batchsave();
//		// 如果不关心返回值可以不用在这里get
////		for (SeataAsyncCallInfo<Boolean> asyncInfo : SeataAysncCallContext.getAsyncInfos()) {
////			Boolean aBoolean = asyncInfo.get();
////		}

//	}
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
	@Test
	void ee(){
		System.out.println(
				"==>  Preparing: INSERT INTO test ( a, b ) VALUES ( ?, ? )\n"
						+ "==>  Preparing: INSERT INTO test ( a, b ) VALUES ( ?, ? )\n"
						+ "==> Parameters: 199(Integer), 113(Integer)\n"
						+ "==> Parameters: 199(Integer), 114(Integer)\n" + "<==    Updates: 1\n"
						+ "<==    Updates: 0\n"
						+ "Exception,transactional rollback\n"
						+ "Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@12b3249e]\n"
						+	"Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@52fbee70]\n"
						+	"Closing transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@51eeba13]\n"
						+ "Thread[pool-1-thread-1,4,main]保存\n" + "Thread[pool-1-thread-2,4,main]异常"
		);
	}
	@Test
	void rr() throws ExecutionException, InterruptedException {
		int size=3-1;
		ExecutorService executorService = Executors.newCachedThreadPool();
		final int[] s = {44};
		List<Future<com.t.logic.entity.Test>> futures = new ArrayList<>();
			futures.add(executorService.submit(new Callable<com.t.logic.entity.Test>(){
				@Override
				public com.t.logic.entity.Test call() throws Exception {
					Thread.sleep(100);
					return new com.t.logic.entity.Test(s[0]++,s[0]++);
				}
			}));

		ArrayList<com.t.logic.entity.Test> tests = new ArrayList<>();
		for (Future<com.t.logic.entity.Test> future : futures) {
			if (future.get() == null) {
				System.out.println("异常");
				break;
			}else{
				tests.add(future.get());
			}
		}
		testService.saveBatch(tests);
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

		testService.save(new com.t.logic.entity.Test(Integer.parseInt(a.substring(6,9)),221));
		platformTransaction.rollback(transaction);
		TransactionStatus transaction1 = platformTransaction
				.getTransaction(new DefaultTransactionDefinition(0));
		testService.save(new com.t.logic.entity.Test(Integer.parseInt(a.substring(6,9)+1),221));
		platformTransaction.commit(transaction1);
	}
//	@Transactional
	public void we(){
//		TransactionStatus transaction = platformTransaction.
//				getTransaction(new DefaultTransactionDefinition(3));
//		System.out.println(transaction);
	String a= String.valueOf(System.currentTimeMillis());
	System.out.println(a.substring(6,9));
	testService.save(new com.t.logic.entity.Test(Integer.parseInt(a.substring(6,9)+32),221));
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
