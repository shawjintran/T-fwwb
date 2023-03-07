package com.t.medicaldocument.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.t.medicaldocument.entity.PdfDescription;
import com.t.medicaldocument.service.PdfDescriptionService;
import com.t.medicaldocument.service.PdfFileService;
import com.t.medicaldocument.utils.Cmd;
import com.t.medicaldocument.utils.FileUtils;
import com.t.medicaldocument.utils.PdfDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
@Slf4j
/**
 * 异步任务
 * 调用命令行,通过Python进行推测,同时将返回结果进行处理后,存入到数据库中
 */
public class AsyncTask {
	PdfDataUtils utils=new PdfDataUtils();
	@Autowired
	PdfDescriptionService descriptionService;
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	PlatformTransactionManager platformTransaction;
	// 事务定义:事务的一些基础信息，如超时时间、隔离级别、传播属性等
	@Autowired
	TransactionDefinition transactionDefinition;

	@Async
	public Future<Integer> saveDescription(Long id, String file_name, Integer page) throws IOException, InterruptedException {
		Process process = Runtime.getRuntime()
				.exec(Cmd.create().toString(file_name,page));
		process.waitFor();
		log.info(file_name+"_"+page+" predict success");
		// D:\CodeOfJava\Medical-Document\res\2ee320bcb7eb41e28744b9c39348b5b0\structure\0
		String pic_path="D:\\CodeOfJava\\Medical-Document\\res\\"
				+ file_name + "\\structure\\" + page;
		HashMap<String, Object> map = utils.PdfStructure2(pic_path);
		PdfDescription desc = new PdfDescription();
		// String s = JSON.toJSONString(map);
		desc.setPdfTextStructure(JSONObject.toJSONString(map));
		desc.setPdfId(id);
		desc.setPdfPage(page);
		desc.setPdfPicUrl(pic_path);
		boolean save=false;
		save= descriptionService.save(desc);
		log.info(file_name+"_"+page+" save success");
		if (save)
			return new AsyncResult<>(1);
		return new AsyncResult<>(0);
	}
	@Async
	public void predictByPython(Long pdfId,String filename, Integer count) throws IOException, InterruptedException, ExecutionException {
		TransactionStatus transaction = platformTransaction.getTransaction(transactionDefinition);
		PdfFileService bean = applicationContext.getBean(PdfFileService.class);
		for (Integer i = 0; i < count; i++) {
			Future<Integer> future = applicationContext.getBean(AsyncTask.class)
					.saveDescription(pdfId, filename, i);
			if (future.get()==0)
			{
				platformTransaction.rollback(transaction);
				//推测出现异常,设置
				bean.statusUpdate(pdfId,2);
				return;
			}
		}
		platformTransaction.commit(transaction);
		bean.statusUpdate(pdfId,1);
	}
}
