package com.t.medicaldocument.mapper;

import com.t.medicaldocument.entity.PdfFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.t.medicaldocument.entity.Vo.PdfFileVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author sky
* @description 针对表【pdf_file】的数据库操作Mapper
* @createDate 2023-02-02 20:23:28
* @Entity com.t.medicaldocument.entity.PdfFile
*/
public interface PdfFileMapper extends BaseMapper<PdfFile> {

	Integer modifyDocId(List<Long> ids, Long docId,Long userId);

	List<PdfFileVo> fileSearchPageById(Integer offset,Integer limit,Long docId, Long userId);

	Integer fileCount(Long docId,Long userId);

	List<Long> judgeIfRational(List<Long> ids);

	@Select("select pdf_id from pdf_file where user_id=#{userId}")
	List<Long> fileSelectByUser(Long userId);

	boolean fileDeleteByIds(List<Long> pdfIds);
}




