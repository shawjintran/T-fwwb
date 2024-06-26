package com.t.logic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.t.logic.entity.Document;
import com.t.logic.entity.User;
import com.t.logic.entity.Vo.DocumentVo;
import com.t.logic.service.DocumentService;
import com.t.logic.mapper.DocumentMapper;
import com.t.logic.service.PdfFileService;
import com.t.logic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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

	@Autowired
	PdfFileService pdfFileService;
	@Autowired
	UserService userService;
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
		document.setOwnId(doc.getOwnId());

		User byId = userService.getById(doc.getOwnId());
		document.setDocCapacity(byId.getUserCapacity());

		int insert = baseMapper.insert(document);
		if (insert==1)
			return true;
		return false;
	}

	@Override
	public List<Map<String, Object>> searchReadDocByUser(Long uId) {
		List<Map<String, Object>> maps = baseMapper.searchDocByUser(uId);
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
	@Transactional(rollbackFor = Exception.class)
	public boolean removeByDocIdAndUserId(Long DocId, Long UserId) {



		//Mapper层添加了docId判断
		Integer delete=baseMapper.removeByDouble(DocId,UserId);
		if (delete<1)
			return false;
		if(DocId==null)
		{
			boolean remove=pdfFileService.deleteFileByUser(UserId);
			if (remove)
				return true;
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		return true;
	}

	@Override
	public DocumentVo docEcho(Long docId, Long userId) {
		DocumentVo vo=baseMapper.searchDoc(docId,userId);
		if (vo==null||vo.getDocName()==null)
			return null;
		vo.setDocId(docId);
		vo.setOwnId(userId);
		return vo;
	}

	@Override
	public boolean isFullCapacity(Long docId, Long userId,Integer size) {
		QueryWrapper<Document> wrapper = new QueryWrapper<>();
		wrapper.eq("doc_id",docId).eq("user_id",userId);
		Document one = baseMapper.selectOne(wrapper);
		if (one==null)
			return true;
		if (one.getDocCapacity()<one.getDocSize()+size)
			return true;
		return false;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean expandCapacity(Long docId, Long userId) {
		Integer integer = baseMapper.addDocCapacity(docId, userId);
		boolean b = userService.updatePoint(1, 3, userId);
		if (integer==1&&b)
			return true;
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return false;
	}

	@Override
	public List<Map<String, Object>> searchWriteDocNameByUser(Long userId) {
		List<Map<String, Object>> maps = baseMapper.searchDocByUser(userId);
		maps.stream().forEach( item ->{
			item.remove("auth");
		});
		return null;
	}
	@Override
	public List<Long> selectGroupDocs(Long groupId) {
		return baseMapper.selectGroupDoc(groupId);
	}

}




