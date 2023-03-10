package com.t.medicaldocument.service.impl;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.t.medicaldocument.entity.Bo.DocumentBo;
import com.t.medicaldocument.entity.PdfDescription;
import com.t.medicaldocument.entity.Vo.SearchShow;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SearchServiceImpl {
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    //解析数据放在es索引
    public Boolean parseContent(List<PdfDescription> pdfDescriptions) throws IOException {


        //查询数据放入es
        //批量请求
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout(TimeValue.timeValueSeconds(60));
        log.info("正在存入");


        for (PdfDescription pdfDescription : pdfDescriptions) {
            bulkRequest.add(new IndexRequest("ys")
                    .source(JSON.toJSONString(pdfDescription),//存json
                    XContentType.JSON));
        }


        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return  !bulk.hasFailures();


    }


    //获取数据

    //实现搜索功能
    public List<SearchShow> searchPage(String keyword, int pageNo, int pageSize) throws IOException {
        //如果分页太小也从第一个开始分页
        if(pageNo<=0){
            pageNo=0;
        }

        log.info(keyword+":"+pageNo+":"+pageSize);


        //条件搜索
        SearchRequest searchRequest = new SearchRequest("pdf");
        //构造器
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();


        //分页
        sourceBuilder.from(pageNo);
        sourceBuilder.size(pageSize);
//
//        //匹配
//        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("pdftextstructure",keyword);
//        sourceBuilder.query(matchQueryBuilder)
//                .timeout(TimeValue.timeValueSeconds(60));
//        //执行搜索
//        searchRequest.source(sourceBuilder);
//        SearchResponse searchResponse=restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);

        //匹配
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("pdftextstructure", keyword);
        sourceBuilder.query(termQueryBuilder)
                .timeout(TimeValue.timeValueSeconds(10));

        //执行搜索
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse=restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
        log.info("es响应:"+searchResponse.getHits().getHits().length);
        //解析结果
        ArrayList<DocumentBo> list = new ArrayList<>();
        System.out.println(JSON.toJSONString(searchResponse.getHits()));//打印命中集合

        //对象映射
        for (SearchHit documentFields : searchResponse.getHits()) {
            DocumentBo documentBo=new DocumentBo(
                    Integer.toUnsignedLong((Integer) documentFields.getSourceAsMap().get("pdfdescid")),
                    Integer.toUnsignedLong((Integer) documentFields.getSourceAsMap().get("pdfid")),
                    (Integer)documentFields.getSourceAsMap().get("pdfpage"),
                    (String)documentFields.getSourceAsMap().get("pdftextstructure"),
                    (String)documentFields.getSourceAsMap().get("pdfpicurl"),
                    documentFields.getScore()
            );
            //添加到list集合
            list.add(documentBo);
            log.info(documentFields.toString());
            log.info(documentBo.toString());
        }
        //按照pdfid分类,分类成对应文献
        Map<Long ,List<DocumentBo>> map= list.stream().collect(Collectors.groupingBy(DocumentBo::getPdfId));//分类收集
        List<SearchShow> searchShows=new ArrayList<>();//新建一个合适的显示用的对象
        for(Map.Entry<Long,List<DocumentBo>> entry : map.entrySet()){//for遍历
            List<DocumentBo> documentBos = entry.getValue();//获取list集合
            SearchShow searchShow = new SearchShow();//用于显示在前端的对象
            for (DocumentBo documentBo : documentBos) {//然后组装成完整的
               if(searchShow.getText()==null) {//确保search.text是第一个的内容
                   searchShow.setText((String) documentBo.getPdfTextStructure());

                   searchShow.setPdfId(documentBo.getPdfId());

                   searchShow.setTitle("");//TODO 获取标题
                   searchShow.setImgUrl("");//TODO 设置图片
                   searchShow.setPageString("命中页数有:");
                   searchShow.setScore(0f);
               }
                searchShow.setScore(documentBo.getScore()+ searchShow.getScore());//加分
                searchShow.setPageString(searchShow.getPageString()+documentBo.getPdfPage()+"  ");

            }
            searchShows.add(searchShow);


        }

        return searchShows;
    }























    //实现高亮搜索功能
    public List<Map<String,Object>> highlightsearchPage(String keyword, int pageNo, int pageSize) throws IOException {
        //如果分页太小也从第一个开始分页
//        if(pageNo<=1){
//            pageNo=1;
//        }
        //条件搜索
        SearchRequest searchRequest = new SearchRequest("pdf");
        //构造器
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();


        //分页
        sourceBuilder.from(pageNo);
        sourceBuilder.size(pageSize);

        //匹配
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("pdftextstructure", keyword);
        sourceBuilder.query(termQueryBuilder)
                .timeout(TimeValue.timeValueSeconds(10));

        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");

//        //关闭多个字段匹配高亮
//        //highlightBuilder.requireFieldMatch(false);
//        highlightBuilder.preTags("<span style='color:red'>");
//        highlightBuilder.postTags("</span>");
//        sourceBuilder.highlighter(highlightBuilder);

        //执行搜索
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse=restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
        //解析结果
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (SearchHit documentFields : searchResponse.getHits()) {
            list.add(documentFields.getSourceAsMap());
        }
        return list;
    }


}
