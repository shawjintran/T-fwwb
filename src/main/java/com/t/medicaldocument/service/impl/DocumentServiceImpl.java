package com.t.medicaldocument.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.t.medicaldocument.entity.Document;
import com.t.medicaldocument.entity.Vo.DocumentVo;
import com.t.medicaldocument.service.DocumentService;
import com.t.medicaldocument.mapper.DocumentMapper;
import com.t.medicaldocument.utils.R;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
* @author sky
* @description 针对表【document】的数据库操作Service实现
* @createDate 2023-02-13 09:58:09
*/
@Service
public class DocumentServiceImpl extends ServiceImpl<DocumentMapper, Document>
    implements DocumentService{


	@Override
	public boolean nameRepeat(String name,Long uId) {
		String repeat = baseMapper.nameRepeat(name,uId);
		if (repeat==null)
			return false;
		return true;
	}

	@Override
	public boolean addDoc(DocumentVo doc) {
		Document document = new Document();
		document.setDocName(doc.getDocName());
		document.setUserId(doc.getUserId());
		if(document.getUserId()!=null)
			document.setDocId(doc.getDocId());
		int insert = baseMapper.insert(document);
		if (insert==1)
			return true;
		return false;
	}

	@Override
	public List<Map<String, Object>> searchDocById(Long uId) {
		List<Map<String, Object>> maps = baseMapper.searchDocById(uId);
		return maps;
	}

	@Override
	public boolean updateDoc(DocumentVo doc) {
		Integer update = baseMapper.updateDoc(doc.getDocId(), doc.getDocName());
		if (update==1)
			return true;
		return false;
	}

	@Override
	public boolean updateSize(Integer mode,Long docId,Integer size,Long userId) {
		Integer integer=0;
		if (mode==1)
			integer=baseMapper.AddSize(docId,size,userId);
		else if (mode==2)
			integer=baseMapper.SubSize(docId,size,userId);;
		if (integer==1)
			return true;
		return false;
	}

	@Override
	public boolean removeByDocIdAndUserId(Long DocId, Long UserId) {
		Integer delete=baseMapper.removeByDouble(DocId,UserId);
		if (delete==1)
			return true;
		return false;
	}

}




