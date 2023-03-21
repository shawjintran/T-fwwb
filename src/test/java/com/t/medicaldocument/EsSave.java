package com.t.medicaldocument;


import com.t.medicaldocument.entity.Bo.EsDocumentBo;
import com.t.medicaldocument.entity.PdfDescription;
import com.t.medicaldocument.service.impl.SearchServiceImpl;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.core.TimeValue;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;

@SpringBootTest
@MapperScan("com.t.medical.medicaldocument.mapper")
@Slf4j
public class EsSave {
    @Autowired
    SearchServiceImpl searchService;
    //数据导入
    @Test
    void testBulkRequest(){


        ArrayList<EsDocumentBo> esDocumentBos=new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            EsDocumentBo esDocumentBo = new EsDocumentBo();
            esDocumentBo.setPdfdescid(1000l+i);
            esDocumentBo.setPdfid(120l-1);
            esDocumentBo.setPdfpage(1);
            esDocumentBo.setAll("时代法国红酒term做精确查询可以用它来处理数字，布尔值，日期以及文本。查询数字时问题不大，但是当查询字符串时会有问题。term查询的含义是termQuery会去倒排索引中寻找确切的term,但是它并不知道分词器的存在。term表示查询字段里含有某个关键词的文档，terms表示查询字段里含有多个关键词的文档。也就是说直接对字段进行term本质上还是模糊查询，只不过不会对搜索的输入字符串进行分词处理罢了。如果想通过term查到数据，那么term查询的字段在索引库中就必须有与term查询条件相同的索引词，否则无法查询到结果。");
            esDocumentBo.setPdfpicurl("http//pdf的url.pdf");
            esDocumentBo.setCreatetime("2023-02-15");
            esDocumentBo.setText("这是正文,term做精确查询可以用它来处理数字，布尔值，日期以及文本。查询数字时问题不大，但是当查询字符串时会有问题。term查询的含义是termQuery会去倒排索引中寻找确切的term,但是它并不知道分词器的存在。term表示查询字段里含有某个关键词的文档，terms表示查询字段里含有多个关键词的文档。也就是说直接对字段进行term本质上还是模糊查询，只不过不会对搜索的输入字符串进行分词处理罢了。如果想通过term查到数据，那么term查询的字段在索引库中就必须有与term查询条件相同的索引词，否则无法查询到结果。");
            esDocumentBo.setTitle("这是标题");
            esDocumentBo.setFigure("这是图片的内容");
            esDocumentBo.setFigure_caption("这是图片标题");
            esDocumentBo.setTable("这是表格内容");
            esDocumentBo.setTable_caption("这是表格标题");
            esDocumentBo.setHeader("这是页头");
            esDocumentBo.setFooter("这是页尾");
            esDocumentBo.setReference("这是引用");
            esDocumentBo.setEquation("这是公式");

            esDocumentBos.add(esDocumentBo);
        }

        System.out.println("内容");
        esDocumentBos.forEach(System.out::println);

        boolean b = searchService.save2ES(esDocumentBos);

        System.out.println(b);


    }




}
