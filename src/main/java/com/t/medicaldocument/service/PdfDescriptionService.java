package com.t.medicaldocument.service;

import com.t.medicaldocument.entity.PdfDescription;
import com.baomidou.mybatisplus.extension.service.IService;
import com.t.medicaldocument.entity.Vo.PdfDescVo;

import java.io.IOException;
import java.util.List;

/**
* @author sky
* @description 针对表【pdf_description】的数据库操作Service
* @createDate 2023-02-02 20:41:15
*/
public interface PdfDescriptionService extends IService<PdfDescription> {
	boolean deleteByPdfIds(List<Long> ids);
	List<PdfDescVo> descSearchByPdfId(Long pdfId);
}
