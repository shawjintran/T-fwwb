package com.t.medicaldocument.service;

import com.t.medicaldocument.entity.PdfFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.t.medicaldocument.entity.Vo.PdfFileVo;
import com.t.medicaldocument.entity.Vo.PdfFileVo2;
import com.t.medicaldocument.entity.Vo.PdfFileVo3;
import org.springframework.web.bind.annotation.PathVariable;
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
	String uploadPdfFile(PdfFile pdf) throws IOException;
	 boolean fileMove(List<Long> ids,Long userId,Long newDocId);
	Integer dividePDF(String filename) throws IOException;
	boolean removeFile(List<Long> ids, Long docId, Long userId);
	List<PdfFileVo> fileSearchPageById( Integer page, Integer size, Integer total,PdfFileVo3 vo);
	Integer fileCount(Long docId,Long userId);
	boolean fileDelete(List<Long> ids, Long docId, Long userId);
	boolean statusUpdate(Long pdfId,Integer status);
	boolean fileUpdate(PdfFileVo2 pdf);
	@Deprecated
	boolean placeFile(List<Long> ids, Long docId, Long userId);
	void downloadPdfFile(HttpServletResponse response, String filename) throws IOException;

	boolean deleteFileByUser(Long userId);

	PdfFileVo fileEcho(Long userId, Long pdfId);
	PdfFileVo fileExist(Long userId, Long pdfId);


	String upload(MultipartFile file);

	List<PdfFileVo> fileGetLats10(Long userId, Integer status);
}
