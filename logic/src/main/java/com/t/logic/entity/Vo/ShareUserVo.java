package com.t.logic.entity.Vo;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShareUserVo {
  @ApiParam(required = true)
  private Long userId;
  /**
   * 用户电话
   */
  private String userPhone;

  /**
   *
   */
  private Integer userRole;

  /**
   *
   */
  private Integer jointStatus;
}
