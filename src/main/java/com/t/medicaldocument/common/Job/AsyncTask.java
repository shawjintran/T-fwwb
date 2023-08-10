package com.t.medicaldocument.common.Job;

import com.alibaba.fastjson2.JSONObject;
import com.t.medicaldocument.common.BeanContext;
import com.t.medicaldocument.config.AsyncConfig;
import com.t.medicaldocument.entity.Bo.EsDocumentBo;
import com.t.medicaldocument.entity.PdfDescription;
import com.t.medicaldocument.service.PdfDescriptionService;
import com.t.medicaldocument.service.PdfFileService;
import com.t.medicaldocument.service.impl.SearchServiceImpl;
import com.t.medicaldocument.utils.FileUtils;
import com.t.medicaldocument.utils.PdfDataUtils;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.springframework.transaction.support.DefaultTransactionDefinition;

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
	PdfFileService pdfFileService;
	@Autowired
	PlatformTransactionManager platformTransaction;
	@Autowired
	// 事务定义:事务的一些基础信息，如超时时间、隔离级别、传播属性等
	TransactionDefinition transactionDefinition;

	//es实现类
	@Autowired
	SearchServiceImpl searchService;

	@Async
	public void predictByPython(Long pdfId,Long userId,String filename, Integer count)
			throws InterruptedException, ExecutionException, IOException {
		//  TODO: 2023/3/25 多线程事务问题 出现 难题 (DONE)
		//  2023/8/9  将子线程事务抽离到主线程中
		//	2023/8/10 其他类中 事务抽离
		TransactionStatus transaction = platformTransaction.
				getTransaction(new DefaultTransactionDefinition(3));

		List<Future<Boolean>> futures = new ArrayList<>();
		AsyncConfig asyncConfig = BeanContext.getBean(AsyncConfig.class);
		ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) asyncConfig.getAsyncExecutor();
		for (Integer i = 0; i < count; i++) {
			futures.add(executor.submit(new DescCallable(pdfId, filename, i)));
		}
		for (Future<Boolean> future : futures) {
			if (future.get())
				continue;
			pdfFileService.statusUpdate(pdfId, 2);
			platformTransaction.commit(transaction);
			return;
		}
		ArrayList<HashMap<String, Object>> pdfEs = batchSave(pdfId, filename, count);
		//当前子方法事务结束，外部存在一事务进行提交结束---------
		if (pdfEs==null)
		{
			pdfFileService.statusUpdate(pdfId,3);
			platformTransaction.commit(transaction);
			return;
		}
		//当前进程事务无任何提交以及回滚，直接使用当前事务

		boolean update = pdfFileService.statusUpdate(pdfId, 1);
		//结合ES 存入
		ArrayList<EsDocumentBo> objects = PdfDataUtils.parseList(pdfEs,userId);
		//将要存储进Es的对象先收集起
		//储存到es,返回boolean
		boolean save = searchService.save2ES(objects);
		log.info("ES save status:" + save);
		//通过es批量存储
		if (!update || !save){
			platformTransaction.rollback(transaction);
			pdfFileService.statusUpdate(pdfId, 2);
			return;
		}
		platformTransaction.commit(transaction);
		return;
	}
	private ArrayList<HashMap<String, Object>> batchSave(Long id, String file_name,Integer count) throws IOException {
		//		设置事务传播隔离级别
		TransactionStatus transaction = platformTransaction.
				getTransaction(new DefaultTransactionDefinition(3));
		ArrayList<HashMap<String, Object>> pdfEs = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			HashMap<String, Object> pdfDesc = dbSaveDesc(id, file_name, i);
			if (pdfDesc==null)
			{
				platformTransaction.rollback(transaction);
				log.error("batch save values error, rollback PdfDesc save");
				return null;
			}
			pdfEs.add(pdfDesc);
		}
		platformTransaction.commit(transaction);
		return pdfEs;
	}
	private HashMap<String, Object> dbSaveDesc(Long id, String file_name,Integer page) throws IOException {
		// D:\CodeOfJava\Medical-Document\res\2ee320bcb7eb41e28744b9c39348b5b0\structure\0
			String pic_path= FileUtils.res_location
					+ file_name + "\\structure\\" + page;
			//路径映射
			HashMap<String, ArrayList> map = PdfDataUtils.PdfStructure2(pic_path);
			PdfDescription desc = new PdfDescription();
			desc.setPdfTextStructure(JSONObject.toJSONString(map));
			desc.setPdfId(id);
			desc.setPdfPage(page);
			desc.setPdfPicUrl(file_name + "\\" + page+".jpg");
			boolean save= descriptionService.save(desc);
			if (!save){
				log.info(file_name+"_"+page+" save failed");
				return null;
			}
			log.info(file_name+"_"+page+" save success");
			HashMap<String, Object> pdfDesc = new HashMap<>(3);
			pdfDesc.put("pdfId",id);
			pdfDesc.put("page",page);
			pdfDesc.put("desc",map);
			return pdfDesc;
	}
}
