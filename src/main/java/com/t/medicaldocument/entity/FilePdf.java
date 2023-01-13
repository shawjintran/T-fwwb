package com.t.medicaldocument.entity;

import io.swagger.annotations.ApiModel;

import java.time.LocalDateTime;

@ApiModel(description = "Pdf file object")
public class FilePdf {

	public Long pdfId;
	public Integer count;
	public LocalDateTime createTime;
	public LocalDateTime updateTime;

}
