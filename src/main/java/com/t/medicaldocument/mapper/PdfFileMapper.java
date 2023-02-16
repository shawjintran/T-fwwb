package com.t.medicaldocument.mapper;

import com.t.medicaldocument.entity.PdfFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.t.medicaldocument.entity.Vo.PdfFileVo;

import java.util.List;

/**
* @author sky
* @description 针对表【pdf_file】的数据库操作Mapper
* @createDate 2023-02-02 20:23:28
* @Entity com.t.medicaldocument.entity.PdfFile
*/
public interface PdfFileMapper extends BaseMapper<PdfFile> {

	Integer modifyDocId(List<Long> ids, Long docId);

	List<PdfFileVo> fileSearchByDocId(Long docId);
}




