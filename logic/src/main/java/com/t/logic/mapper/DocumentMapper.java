package com.t.logic.mapper;

import com.t.logic.entity.Document;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.t.logic.entity.Vo.DocumentVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
* @author sky
* @description 针对表【document】的数据库操作Mapper
* @createDate 2023-02-13 09:58:09
* @Entity com.t.medicaldocument.entity.Document
*/
@Mapper
public interface DocumentMapper extends BaseMapper<Document> {
	String nameRepeat(String name,Long uId);
	@MapKey("docId")
	List<Map<String,Object>> searchDocById(Long uId);

	@Update("update document set doc_name = #{docName},update_time =now() where doc_id =#{docId}")
	Integer updateDoc(Long docId, String docName);
	@Update("update document set doc_size = doc_size+${size} ,update_time =now() where doc_id=#{docId} and user_id=#{userId}")
	Integer AddSize(Long docId, Integer size, Long userId);
	@Update("update document set doc_size = doc_size-${size} ,update_time =now() where doc_id=#{docId} and user_id=#{userId}")
	Integer SubSize(Long docId, Integer size, Long userId);
	@Update("update document set doc_capacity=doc_capacity+5, update_time=now() where doc_id=#{docId} and user_id=#{userId}")
	Integer addDocCapacity(Long docId,Long userI);
	Integer removeByDouble(Long docId, Long userId);
	@Select("select doc_name from document where doc_id=#{docId} and user_id=#{userId}")
	DocumentVo searchDoc(Long docId, Long userId);
}




