package com.t.medicaldocument.service;

import com.t.medicaldocument.entity.PdfFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.t.medicaldocument.entity.Vo.PdfFileVo;
import com.t.medicaldocument.entity.Vo.PdfFileVo2;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
* @author sky
* @description 针对表【pdf_file】的数据库操作Service
* @createDate 2023-02-02 20:23:28
*/
public interface PdfFileService extends IService<PdfFile> {
	HashMap<String, Object> uploadPdfFile(MultipartFile file, PdfFile pdf) throws IOException;
	Integer dividePDF(String filename) throws IOException;
	boolean removeFile(List<Long> ids, Long docId, Long userId);
	List<PdfFileVo> fileSearchByDocId(Long docId,Long userId);
	boolean fileDelete(List<Long> ids, Long docId,Long userId);
	boolean statusUpdate(Long pdfId,Integer status);
	boolean fileUpdate(PdfFileVo2 pdf);
	boolean placeFile(List<Long> ids, Long docId, Long userId);
	void downloadPdfFile(HttpServletResponse response, String filename) throws IOException;
}
