package com.t.medicaldocument.entity.Vo;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {
	@ApiParam(required = true)
	private Long userId;
	/**
	 * 用户电话
	 */
	private String userPhone;
	/**
	 * 用户头像链接
	 */
	private String userUrl;
	/**
	 * 用户积分
	 */
	private Integer userPoints;

	private Long userCapacity;
}
