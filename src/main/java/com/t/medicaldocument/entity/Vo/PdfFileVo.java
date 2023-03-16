package com.t.medicaldocument.entity.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PdfFileVo {
	/**
	 * pdf文件id
	 */
	private Long pdfId;

	/**
	 * pdf文件id
	 */
	private Long docId;
	/**
	 * pdf文件标题
	 */
	private String pdfTitle;

	/**
	 * pdf文件作者
	 */
	private String pdfAuthor;

	/**
	 * pdf文件状态
	 */
	private Integer pdfStatus;

}
