package com.t.medicaldocument.common.Job;

import com.alibaba.fastjson2.JSONObject;
import com.t.medicaldocument.common.BeanContext;
import com.t.medicaldocument.config.AsyncConfig;
import com.t.medicaldocument.entity.PdfDescription;
import com.t.medicaldocument.service.PdfDescriptionService;
import com.t.medicaldocument.service.PdfFileService;
import com.t.medicaldocument.utils.Cmd;
import com.t.medicaldocument.utils.PdfDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

@Component
@Slf4j
/**
 * 异步任务
 * 调用命令行,通过Python进行推测,同时将返回结果进行处理后,存入到数据库中
 */
public class AsyncTask {
	@Autowired
	PdfDescriptionService descriptionService;
	@Autowired
	PlatformTransactionManager platformTransaction;
	@Autowired
	// 事务定义:事务的一些基础信息，如超时时间、隔离级别、传播属性等
	TransactionDefinition transactionDefinition;

	@Async
	public void predictByPython(Long pdfId,String filename, Integer count) throws IOException, InterruptedException, ExecutionException {
		TransactionStatus transaction = platformTransaction.
				getTransaction(transactionDefinition);
		PdfFileService bean = BeanContext.getBean(PdfFileService.class);
		List<Future<HashMap>> futures = new ArrayList<>();
		AsyncConfig asyncConfig = BeanContext.getBean(AsyncConfig.class);
		ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor)asyncConfig.getAsyncExecutor();
		for (Integer i = 0; i < count; i++) {
			futures.add(executor.submit(new DescCallable(pdfId, filename, i)));
		}
		ArrayList<HashMap<String,Object>> pdfEs=new ArrayList<>();
		//将要存储进Es的先收集起
		for (Future<HashMap> future : futures) {
			if (future.get()!=null)
			{
				pdfEs.add(future.get());
				continue;
			}
			platformTransaction.rollback(transaction);
			//推测出现异常,设置为2
			bean.statusUpdate(pdfId,2);
			return;
		}
		synchronized (this){
			bean.statusUpdate(pdfId,1);
			// todo：修改逻辑，结合ES
		}


		platformTransaction.commit(transaction);

	}
}
