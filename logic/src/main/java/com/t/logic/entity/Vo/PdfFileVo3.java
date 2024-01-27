package com.t.logic.entity.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PdfFileVo3 {
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * pdf文件夹id
	 */
	private Long docId;

}
