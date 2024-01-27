package com.t.logic.mapper;

import com.t.logic.entity.PdfDescription;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.t.logic.entity.Vo.PdfDescVo;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
* @author sky
* @description 针对表【pdf_description】的数据库操作Mapper
* @createDate 2023-02-02 20:41:15
* @Entity com.t.medicaldocument.entity.PdfDescription
*/
@Mapper
public interface PdfDescriptionMapper extends BaseMapper<PdfDescription> {

	Integer deleteByPdfIds(List<Long> ids);

	List<PdfDescVo> descSearchByPdfId(Long pdfId);

}




