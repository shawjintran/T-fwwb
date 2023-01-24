package com.t.medicaldocument.mapper;

import com.t.medicaldocument.entity.PdfFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author sky
* @description 针对表【pdf_file】的数据库操作Mapper
* @createDate 2023-01-24 12:40:55
* @Entity com/t/medicaldocument.entity.PdfFile
*/
@Mapper
public interface PdfFileMapper extends BaseMapper<PdfFile> {

}




