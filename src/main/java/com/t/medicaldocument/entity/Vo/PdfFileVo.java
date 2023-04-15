package com.t.medicaldocument.entity.Vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "只做页面展示，不做参数传递")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PdfFileVo implements Serializable {
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
	 * pdf文件状态
	 */
	private Integer pdfStatus;

	private Integer pdfPagecount;

	private String pdfFileName;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime;


	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updateTime;
}
