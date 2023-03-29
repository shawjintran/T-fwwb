package com.t.medicaldocument.common.Job;

import com.alibaba.fastjson2.JSONObject;
import com.t.medicaldocument.common.BeanContext;
import com.t.medicaldocument.entity.PdfDescription;
import com.t.medicaldocument.service.PdfDescriptionService;
import com.t.medicaldocument.utils.Cmd;
import com.t.medicaldocument.utils.PdfDataUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

@AllArgsConstructor
@Slf4j
public class DescCallable implements Callable<HashMap> {
	Long id;
	String file_name;
	Integer page;

	@Override
	public HashMap call() throws Exception {
		Process process = Runtime.getRuntime()
				.exec(Cmd.create().toString(file_name,page));
		process.waitFor();
		log.info(file_name+"_"+page+" predict success");
		// D:\CodeOfJava\Medical-Document\res\2ee320bcb7eb41e28744b9c39348b5b0\structure\0
		String pic_path="D:\\CodeOfJava\\Medical-Document\\res\\"
				+ file_name + "\\structure\\" + page;
		//路径映射
		HashMap<String, ArrayList> map = PdfDataUtils.PdfStructure2(pic_path);
		PdfDescription desc = new PdfDescription();
		desc.setPdfTextStructure(JSONObject.toJSONString(map));
		desc.setPdfId(id);
		desc.setPdfPage(page);
		desc.setPdfPicUrl(file_name + "\\" + page+".jpg");
		PdfDescriptionService bean = BeanContext.getBean(PdfDescriptionService.class);
		boolean save= bean.save(desc);
		log.info(file_name+"_"+page+" save success");
		if (save){
			Long pdfId = desc.getPdfId();
			HashMap<String, Object> pdfDesc = new HashMap<>(3);
			pdfDesc.put("pdfId",pdfId);
			pdfDesc.put("page",page);
			pdfDesc.put("desc",map);
			return pdfDesc;
		}
		return null;
	}
}
