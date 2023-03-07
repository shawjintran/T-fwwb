package com.t.medicaldocument.service;

import com.t.medicaldocument.entity.Document;
import com.baomidou.mybatisplus.extension.service.IService;
import com.t.medicaldocument.entity.Vo.DocumentVo;

import java.util.List;
import java.util.Map;

/**
* @author sky
* @description 针对表【document】的数据库操作Service
* @createDate 2023-02-13 09:58:09
*/
public interface DocumentService extends IService<Document> {

	boolean nameRepeat(String name,Long uId);
	boolean addDoc(DocumentVo doc);

	List<Map<String,Object>> searchDocById(Long uId);

	boolean updateDoc(DocumentVo doc);
	boolean updateSize(Integer mode,Long docId,Integer size,Long userId);
	boolean removeByDocIdAndUserId(Long DocId,Long UserId);
}
