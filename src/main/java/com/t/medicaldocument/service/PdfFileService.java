package com.t.medicaldocument.service;

import com.t.medicaldocument.entity.PdfFile;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

/**
* @author sky
* @description 针对表【pdf_file】的数据库操作Service
* @createDate 2023-02-02 20:23:28
*/
public interface PdfFileService extends IService<PdfFile> {
	HashMap<String, Object> uploadPdfFile(MultipartFile file, PdfFile pdf) throws IOException;
	Integer dividePDF(String filename) throws IOException;
}
