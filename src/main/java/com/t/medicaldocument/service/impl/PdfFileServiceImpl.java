package com.t.medicaldocument.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.t.medicaldocument.entity.PdfFile;

import com.t.medicaldocument.mapper.PdfFileMapper;
import com.t.medicaldocument.service.PdfFileService;
import org.springframework.stereotype.Service;

/**
* @author sky
* @description 针对表【pdf_file】的数据库操作Service实现
* @createDate 2023-01-24 12:40:55
*/
@Service
public class PdfFileServiceImpl extends ServiceImpl<PdfFileMapper, PdfFile>
    implements PdfFileService {

}




