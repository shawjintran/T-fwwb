package com.t.medicaldocument.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.t.medicaldocument.entity.PdfFile;
import com.t.medicaldocument.service.PdfFileService;
import com.t.medicaldocument.mapper.PdfFileMapper;
import com.t.medicaldocument.utils.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

/**
* @author sky
* @description 针对表【pdf_file】的数据库操作Service实现
* @createDate 2023-02-02 20:23:28
*/
@Service
public class PdfFileServiceImpl extends ServiceImpl<PdfFileMapper, PdfFile>
    implements PdfFileService{
	public HashMap<String, Object> uploadPdfFile(MultipartFile file, PdfFile pdf) throws IOException {
		String filename = UUID.randomUUID()
				.toString()
				.replace("-","");
		pdf.setPdfFileName(filename);
		pdf.setPdfStatus(0);
		FileUtils.savePDF(file,filename+".pdf");
		int insert = baseMapper.insert(pdf);
		if (insert==0)
			return null;
		HashMap<String,Object> map=new HashMap<>(3);
		map.put("id",pdf.getPdfId());
		map.put("filename",filename);
		return map;
	}
	public Integer dividePDF(String filename) throws IOException{
		return FileUtils.dividePDF(filename);
	}
}




