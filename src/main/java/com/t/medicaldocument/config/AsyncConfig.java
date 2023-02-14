package com.t.medicaldocument.config;

import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

@Component
/**
 * 线程池参数
 */
public class AsyncConfig implements AsyncConfigurer {
	// private static final Logger log = LoggerFactory.getLogger(MyAsyncConfigurer.class);

	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
		//设置核心线程数，默认为1
		threadPool.setCorePoolSize(6);
		// 当核心线程都在跑任务，还有多余的任务会存到此处。
		threadPool.setQueueCapacity(100);
		//最大线程数，默认为Integer.MAX_VALUE，如果queueCapacity存满了，还有任务就会启动更多的线程，直到线程数达到maxPoolSize。如果还有任务，则根据拒绝策略进行处理。
		threadPool.setMaxPoolSize(6);
		threadPool.setWaitForTasksToCompleteOnShutdown(true);
		threadPool.setAwaitTerminationSeconds(60 * 15);
		threadPool.setThreadNamePrefix("MyAsync-");
		threadPool.initialize();
		return threadPool;
	}

}
