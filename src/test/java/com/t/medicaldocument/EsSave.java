package com.t.medicaldocument;


import com.t.medicaldocument.entity.PdfDescription;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.core.TimeValue;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
@MapperScan("com.t.medical.medicaldocument.mapper")
@Slf4j
public class EsSave {
    //数据导入
    @Test
    void testBulkRequest(){
        BulkRequest bulkRequest=new BulkRequest();
        bulkRequest.timeout(TimeValue.timeValueSeconds(10));
        ArrayList<PdfDescription> pdfDescriptions=new ArrayList<>();
        pdfDescriptions.add(new PdfDescription());

    }




}
