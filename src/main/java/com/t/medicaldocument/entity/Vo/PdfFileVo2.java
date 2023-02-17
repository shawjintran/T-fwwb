package com.t.medicaldocument.entity.Vo;

import lombok.Data;

@Data
public class PdfFileVo2 {
	/**
	 * pdf文件id
	 */
	private Long pdfId;
	/**
	 * pdf文件夹id
	 */
	private Long docId;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * pdf文件标题
	 */
	private String pdfTitle;

	/**
	 * pdf文件作者
	 */
	private String pdfAuthor;
	/**
	 * 新的文件夹Id
	 */
    private Long newDocId;

}
