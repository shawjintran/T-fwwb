package com.t.logic.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.t.logic.entity.PdfFile;
import com.t.logic.entity.Vo.PdfFileVo;
import com.t.logic.entity.Vo.PdfFileVo2;
import com.t.logic.entity.Vo.PdfFileVo3;
import com.t.logic.service.DocumentService;
import com.t.logic.service.PdfDescriptionService;
import com.t.logic.service.PdfFileService;
import com.t.logic.mapper.PdfFileMapper;
import com.t.logic.utils.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
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

	public String uploadPdfFile(PdfFile pdf)
			 {
		String filename = UUID.randomUUID()
				.toString()
				.replace("-","").substring(0,8);
		pdf.setPdfFileName(filename);
		//添加到默认的文件夹
		pdf.setDocId(0L);
		//文件未开始预测,状态为0;
		pdf.setPdfStatus(0);
//	 	String[] split = file.getOriginalFilename().split(".pdf");
		pdf.setPdfTitle(pdf.getPdfTitle());
		 try {
			 FileUtils.transferPDF(pdf.getPdfTitle(),filename+".pdf");
		 } catch (IOException e) {
		 	log.debug("上传文件出现异常");
			return null;
		 }
		return filename;
	}
	public String upload(MultipartFile file){
		String[] split = file.getOriginalFilename().split(".pdf");
		try {
			FileUtils.tempSave(file,file.getOriginalFilename());
		} catch (IOException e) {
			log.debug("上传文件出现异常");
			return null;
		}
		return split[0];
	}

	@Override
	public List<PdfFileVo> fileGetLats10(Long userId, Integer status) {
		QueryWrapper<PdfFile> pdfFileQueryWrapper = new QueryWrapper<>();
		pdfFileQueryWrapper.eq("user_id",userId);
		if (status==1)
				pdfFileQueryWrapper.orderByDesc("update_time")
						.last("limit 0,10");
		else
			pdfFileQueryWrapper.orderByAsc("update_time")
					.last("limit 0,10");

		List<PdfFile> pdfFiles = baseMapper.selectList(pdfFileQueryWrapper);

		List<PdfFileVo> pdfFileVoList=new ArrayList<>();

		for (int i = 0; i < pdfFiles.size(); i++) {
			PdfFileVo pdfFileVo = new PdfFileVo();
			BeanUtils.copyProperties(pdfFiles.get(i),pdfFileVo);
			pdfFileVoList.add(pdfFileVo);
		}
		return pdfFileVoList;
	}

	public void downloadPdfFile(HttpServletResponse response,String filename)
			 {
		response.setContentType("application/pdf");
		OutputStream outputStream=null;
		BufferedOutputStream buffer=null;
		try{
			String fileName = URLEncoder.encode(filename, "UTF-8");
			response.addHeader("Content-Disposition", "attachment;filename="+ fileName + ".pdf");
			outputStream = response.getOutputStream();
			//使用BufferOutPutStream 有问题
			buffer = new BufferedOutputStream(outputStream);
			Boolean down = FileUtils.downloadPDF(buffer, filename);
			if (down)
				return;
			throw new Exception();
		}catch (Exception e){
			log.error("文件流出现问题");
		}finally {
			try {
				buffer.close();
				outputStream.close();
			} catch (Exception e) {
				log.error("关闭流出现问题");
			}

		}

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteFileByUser(Long userId) {
		List<Long> pdfIds = baseMapper.fileSelectByUser(userId);
		boolean deleteDesc = descriptionService.deleteByPdfIds(pdfIds);
		boolean deletePdf=baseMapper.fileDeleteByIds(pdfIds);
		if (deleteDesc&&deletePdf)
			return true;
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return false;
	}

	@Override
	public PdfFileVo fileEcho(Long userId, Long pdfId) {
		PdfFileVo vo=baseMapper.fileSearchOne(userId,pdfId);
		return vo;
	}

	@Override
	public PdfFileVo fileExist(Long userId, Long pdfId) {
		return baseMapper.fileExist(userId,pdfId);
	}

	@Override
	public Integer dividePDF(String filename) throws IOException{
		return FileUtils.dividePDF(filename);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeFile(List<Long> ids, Long oldDocId,Long userId) {
		//限制只能同时，从同一个文件夹进行移动
		List<Long> longs = baseMapper.judgeIfRational(ids);
		if (longs.size()!=1)
			return false;
		//移入到用户默认文件夹
		Integer integer = baseMapper.modifyDocId(ids, 0L,userId);
		if (integer<1)
			return false;
		//更改文件夹的大小,先减少原文件夹大小,再增加默认文件夹大小
		boolean b1 = documentService.updateSize(2, oldDocId, integer, userId);
		boolean b2 = documentService.updateSize(1, 0L, integer, userId);
		if (b1&&b2)
			return true;
		//出现错误,事务回滚
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return false;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean fileMove(List<Long> ids,Long userId,Long newDocId){
//		Todo 权限变更
		//限制只能同时，从同一个文件夹进行移动
		List<Long> longs = baseMapper.judgeIfRational(ids);
		if (longs.size()!=1)
			return false;
		if (longs.get(0)==newDocId)
			return false;
		boolean fullCapacity = documentService.isFullCapacity(newDocId,userId,ids.size());
		if (fullCapacity)
			return false;
		//移入到新的文件夹
		Integer integer = baseMapper.modifyDocId(ids, newDocId,userId);
		if (integer!=ids.size()){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		//更改文件夹的大小,先减少原文件夹大小,再增加新文件夹大小
		boolean b1 = documentService.updateSize(2, longs.get(0), integer, userId);
		boolean b2 = documentService.updateSize(1, newDocId, integer,	 userId);
		if(b1&&b2)
			return true;
		//出现错误,事务回滚
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return false;
	}

	@Override
	public List<PdfFileVo> fileSearchPageById(Integer page, Integer size,
											  Integer total, PdfFileVo3 vo) {
		Integer offset=(page-1)*size;
		List<PdfFileVo> list=baseMapper.fileSearchPageById(offset,size, vo.getDocId(), vo.getUserId());
		return list;
	}

	@Override
	public Integer fileCount(Long docId, Long userId) {
		return baseMapper.fileCount(docId, userId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean fileDelete(List<Long> ids, Long docId, Long useId) {
		int deleteFile = baseMapper.deleteBatchIds(ids);
		boolean deleteDesc=descriptionService.deleteByPdfIds(ids);
		if (deleteFile==0&&deleteDesc)
			return true;
		boolean deleteDoc=documentService.updateSize(2,docId,deleteFile,useId);
		if(deleteDesc&&deleteDoc)
			return true;
		//出现错误,事务回滚
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return false;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean fileUpdate(PdfFileVo2 pdf) {
		System.out.println(pdf.toString());
		if (pdf.getPdfId()==null||pdf.getPdfTitle()==null||pdf.getDocId()==null)
			return false;
		UpdateWrapper<PdfFile> wrapper = new UpdateWrapper<>();
		wrapper.eq("pdf_id",pdf.getPdfId())
				.eq("user_id",pdf.getUserId())
				.set("pdf_title",pdf.getPdfTitle());
		if (pdf.getPdfAuthor()!=null)
				wrapper.set("pdf_author",pdf.getPdfAuthor());
		int doc_id=0;
		if (pdf.getNewDocId()==null)
			doc_id=baseMapper.update(null,wrapper);
		else
		{
			doc_id = baseMapper.update(null, wrapper.set("doc_id", pdf.getNewDocId()));
			if (doc_id==1){
				documentService.updateSize(2,pdf.getDocId(),1,pdf.getUserId());
				documentService.updateSize(1,pdf.getNewDocId(),1,pdf.getUserId());
			}
		}
		if (doc_id==1)
			return true;
		//出现错误,事务回滚
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return false;
	}

	/**
	 *
	 * @param ids
	 * @param newDocId
	 * @param userId
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean placeFile(List<Long> ids, Long newDocId, Long userId) {
		//限制只能同时，从同一个文件夹进行移动
		List<Long> longs = baseMapper.judgeIfRational(ids);
		if (longs.size()!=1)
			return false;
		//移入到新的文件夹
		Integer integer = baseMapper.modifyDocId(ids, newDocId,userId);
		if (integer<1)
			return false;
		//更改文件夹的大小,先减少原文件夹大小,再增加新文件夹大小
		boolean b1 = documentService.updateSize(2, 0L, integer, userId);
		boolean b2 = documentService.updateSize(1, newDocId, integer, userId);
		if(b1&&b2)
			return true;
		//出现错误,事务回滚
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return false;
	}

	public boolean statusUpdate(Long pdfId,Integer status){
		UpdateWrapper<PdfFile> wrapper = new UpdateWrapper<>();
		wrapper.eq("pdf_id",pdfId).
				set("pdf_status",status);
		int update = baseMapper.update(null, wrapper);
		if (update==1)
			return true;
		log.error("Pdf File status update error");
		return false;
	}
}




