package com.t.logic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.t.logic.entity.PdfDescription;
import com.t.logic.entity.Vo.PdfDescVo;
import com.t.logic.service.PdfDescriptionService;
import com.t.logic.mapper.PdfDescriptionMapper;
import org.springframework.stereotype.Service;

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
		try {
			baseMapper.deleteByPdfIds(ids);
		}catch (Exception e)
		{
			log.error(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public List<PdfDescVo> descSearchByPdfId(Long pdfId) {
		List<PdfDescVo> list =baseMapper.descSearchByPdfId(pdfId);
		return list;
	}
	//修改编辑,逻辑复杂+存储慢
}




