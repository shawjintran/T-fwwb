package com.t.medicaldocument.entity.Vo;

import com.baomidou.mybatisplus.annotation.TableId;

public class PdfDescVo {
	/**
	 *
	 */
	private Long pdfDescId;

	/**
	 * pdf文件page页的文本json结构
	 */
	private Object pdfTextStructure;

	private Integer pdfPage;

}
