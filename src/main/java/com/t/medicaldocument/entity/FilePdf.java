package com.t.medicaldocument.entity;

import io.swagger.annotations.ApiModel;

import java.time.LocalDateTime;

@ApiModel(description = "Pdf文件对象")
public class FilePdf {

	private Long pdfId;
	private String author;
	private Integer pageCount;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
	private Integer delete;

}
