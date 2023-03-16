package com.t.medicaldocument.entity.Vo;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PdfFileVo2 {
	/**
	 * pdf文件id
	 */
	@ApiParam(required = true)
	private Long pdfId;
	/**
	 * pdf文件夹id
	 */
	@ApiParam(required = true)
	private Long docId;
	/**
	 * 用户id
	 */
	@ApiParam(required = true)
	private Long userId;
	/**
	 * pdf文件标题
	 */
	@ApiParam(required = true)
	private String pdfTitle;

	/**
	 * pdf文件作者
	 */
	private String pdfAuthor;
	/**
	 * 新的文件夹Id
	 */
	@ApiParam(value = "只有当点击了移动，并点击了对应文件夹后，前端对象才创建此属性，对其进行赋值")
    private Long newDocId;

}
