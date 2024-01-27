package com.t.logic.service;

import com.t.logic.entity.PdfDescription;
import com.baomidou.mybatisplus.extension.service.IService;
import com.t.logic.entity.Vo.PdfDescVo;

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
