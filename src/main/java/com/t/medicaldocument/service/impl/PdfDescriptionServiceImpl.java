package com.t.medicaldocument.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.t.medicaldocument.config.AsyncTask;
import com.t.medicaldocument.entity.PdfDescription;
import com.t.medicaldocument.entity.Vo.PdfDescVo;
import com.t.medicaldocument.service.PdfDescriptionService;
import com.t.medicaldocument.mapper.PdfDescriptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
* @author sky
* @description 针对表【pdf_description】的数据库操作Service实现
* @createDate 2023-02-02 20:41:15
*/
@Service
public class PdfDescriptionServiceImpl extends ServiceImpl<PdfDescriptionMapper, PdfDescription>
    implements PdfDescriptionService{

	@Override
	public boolean deleteByPdfIds(List<Long> ids) {
		Integer delete=baseMapper.deleteByPdfIds(ids);
		if (delete>=1)
			return true;
		return false;
	}

	@Override
	public List<PdfDescVo> descSearchByPdfId(Long pdfId) {
		List<PdfDescVo> list =baseMapper.descSearchByPdfId(pdfId);
		return list;
	}
	//修改编辑,逻辑复杂+存储慢
}




