package com.t.medicaldocument.Entity;

import io.swagger.annotations.ApiModel;

import java.time.LocalDateTime;

@ApiModel("pdf文件")
public class file_pdf {

	public long pdfId;

	public LocalDateTime createTime;
	public LocalDateTime updateTime;

}
