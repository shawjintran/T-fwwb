package com.t.medicaldocument.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.t.medicaldocument.entity.PdfFile;
import com.t.medicaldocument.entity.Vo.PdfFileVo;
import com.t.medicaldocument.entity.Vo.PdfFileVo2;
import com.t.medicaldocument.service.DocumentService;
import com.t.medicaldocument.service.PdfDescriptionService;
import com.t.medicaldocument.service.PdfFileService;
import com.t.medicaldocument.mapper.PdfFileMapper;
import com.t.medicaldocument.utils.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
* @author sky
* @description 针对表【pdf_file】的数据库操作Service实现
* @createDate 2023-02-02 20:23:28
*/
@Service
public class PdfFileServiceImpl extends ServiceImpl<PdfFileMapper, PdfFile>
    implements PdfFileService{
	@Autowired
	DocumentService documentService;
	@Autowired
	PdfDescriptionService descriptionService;
	public HashMap<String, Object> uploadPdfFile(MultipartFile file, PdfFile pdf) throws IOException {
		String filename = UUID.randomUUID()
				.toString()
				.replace("-","").substring(0,8);
		pdf.setPdfFileName(filename);
		pdf.setPdfStatus(0);
		FileUtils.savePDF(file,filename+".pdf");
		int insert = baseMapper.insert(pdf);
		if (insert==0)
			return null;
		HashMap<String,Object> map=new HashMap<>(3);
		map.put("id",pdf.getPdfId());
		map.put("filename",filename);
		return map;
	}
	public Integer dividePDF(String filename) throws IOException{
		return FileUtils.dividePDF(filename);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeFile(List<Long> ids, Long docId,Integer mode) {
		Integer integer = baseMapper.modifyDocId(ids, docId);
		if (integer<1)
			return false;
		//更改文件夹的大小
		documentService.updateSize(mode,docId,integer);
		return true;
	}

	@Override
	public List<PdfFileVo> fileSearchByDocId(Long docId) {
		List<PdfFileVo> list=baseMapper.fileSearchByDocId(docId);
		return list;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean fileDelete(List<Long> ids, Long docId) {
		int deleteFile = baseMapper.deleteBatchIds(ids);
		boolean deleteDesc=descriptionService.deleteByPdfIds(ids);
		boolean deleteDoc=documentService.updateSize(2,docId,deleteFile);
		if(deleteDesc&&deleteDoc)
			return true;
		return false;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean fileUpdate(PdfFileVo2 pdf) {
		if (pdf.getPdfId()==null||pdf.getPdfTitle()==null||pdf.getPdfAuthor()==null||pdf.getDocId()==null)
			return false;
		UpdateWrapper<PdfFile> wrapper = new UpdateWrapper<>();
		wrapper.eq("pdf_id",pdf.getPdfId())
				.set("pdf_title",pdf.getPdfTitle())
				.set("pdf_author",pdf.getPdfAuthor());
		int doc_id=0;
		if (pdf.getNewDocId()==null)
			doc_id=baseMapper.update(null,wrapper);
		else
		{
			doc_id = baseMapper.update(null, wrapper.set("doc_id", pdf.getNewDocId()));
			if (doc_id==1){
				documentService.updateSize(2,pdf.getDocId(),1);
				documentService.updateSize(1,pdf.getNewDocId(),1);
			}
		}
		if (doc_id==1)
			return true;
		return false;
	}

	public boolean statusUpdate(Long pdfId,Integer status){
		UpdateWrapper<PdfFile> wrapper = new UpdateWrapper<>();
		wrapper.eq("pdf_id",pdfId).
				set("pdf_status",status);
		int update = baseMapper.update(null, wrapper);
		if (update==1)
			return true;
		return false;
	}
}




