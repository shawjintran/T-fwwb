package com.t.medicaldocument.entity.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
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
