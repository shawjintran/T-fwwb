package com.t.logic.entity.Vo;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentVo {
	/**
	 *
	 */
	private Long docId;

	/**
	 *
	 */
	@ApiParam(required = true)
	private Long userId;

	/**
	 *
	 */
	@ApiParam(required = true)
	private String docName;

}
