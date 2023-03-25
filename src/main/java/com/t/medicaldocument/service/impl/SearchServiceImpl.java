package com.t.medicaldocument.service.impl;

import com.alibaba.fastjson2.JSON;
import com.t.medicaldocument.entity.Bo.EsDocumentBo;
import com.t.medicaldocument.entity.EsNestedChild;
import com.t.medicaldocument.entity.EsSearch1;
import com.t.medicaldocument.entity.PdfDescription;
import com.t.medicaldocument.entity.Vo.EsSearchVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.InnerHitBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.join.query.HasChildQueryBuilder;
import org.elasticsearch.join.query.JoinQueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SearchServiceImpl {
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    //解析数据放在es索引

    @Autowired
    private PdfFileServiceImpl pdfFileService;

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

     // 这是之前写的按照pdfid分类,分类成对应文献
    //获取数据
//
//    //实现搜索功能
//    public List<SearchShow> searchPage(String keyword, int pageNo, int pageSize) throws IOException {
//        //如果分页太小也从第一个开始分页
//        if(pageNo<=0){
//            pageNo=0;
//        }
//
//        log.info(keyword+":"+pageNo+":"+pageSize);
//
//
//        //条件搜索
//        SearchRequest searchRequest = new SearchRequest("pdf");
//        //构造器
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//
//
//        //分页
//        sourceBuilder.from(pageNo);
//        sourceBuilder.size(pageSize);
////
////        //匹配
////        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("pdftextstructure",keyword);
////        sourceBuilder.query(matchQueryBuilder)
////                .timeout(TimeValue.timeValueSeconds(60));
////        //执行搜索
////        searchRequest.source(sourceBuilder);
////        SearchResponse searchResponse=restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
//
//        //匹配
//        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("all", keyword);
//        sourceBuilder.query(termQueryBuilder)
//                .timeout(TimeValue.timeValueSeconds(10));
//
//        //执行搜索
//        searchRequest.source(sourceBuilder);
//        SearchResponse searchResponse=restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
//        log.info("es响应:"+searchResponse.getHits().getHits().length);
//        //解析结果
//        ArrayList<EsDocumentBo> list = new ArrayList<>();
//        System.out.println(JSON.toJSONString(searchResponse.getHits()));//打印命中集合
//
//        //对象映射
//        for (SearchHit documentFields : searchResponse.getHits()) {
//            EsDocumentBo esdocumentBo=new EsDocumentBo(
//                    Integer.toUnsignedLong((Integer) documentFields.getSourceAsMap().get("pdfdescid")),
//                    Integer.toUnsignedLong((Integer) documentFields.getSourceAsMap().get("pdfid")),
//                    (Integer)documentFields.getSourceAsMap().get("pdfpage"),
//                    (String)documentFields.getSourceAsMap().get("pdftextstructure"),
//                    (String)documentFields.getSourceAsMap().get("pdfpicurl"),
//                    documentFields.getScore()
//            );
//            //添加到list集合
//            list.add(esdocumentBo);
////            log.info(documentFields.toString());
////            log.info(esdocumentBo.toString());
//        }
//        //按照pdfid分类,分类成对应文献
//        Map<Long ,List<EsDocumentBo>> map= list.stream().collect(Collectors.groupingBy(EsDocumentBo::getPdfId));//分类收集
//        List<SearchShow> searchShows=new ArrayList<>();//新建一个合适的显示用的对象
//        for(Map.Entry<Long,List<EsDocumentBo>> entry : map.entrySet()){//for遍历
//            List<EsDocumentBo> documentBos = entry.getValue();//获取list集合
//            SearchShow searchShow = new SearchShow();//用于显示在前端的对象
//            for (EsDocumentBo documentBo : documentBos) {//然后组装成完整的
//               if(searchShow.getText()==null) {//确保search.text是第一个的内容
//                   searchShow.setText((String) documentBo.getAll());
//
//                   searchShow.setPdfId(documentBo.getPdfId());
//
//                   searchShow.setTitle("");//
//                   // searchShow.setImgUrl("");// 设置图片
//                   searchShow.setPageString("命中页数有:");
//                   //searchShow.setScore(0f);
//               }
//                //searchShow.setScore(documentBo.getScore()+ searchShow.getScore());//加分
//                searchShow.setPageString(searchShow.getPageString()+documentBo.getPdfPage()+"  ");
//
//            }
//            searchShows.add(searchShow);
//
//
//        }
//
//        return searchShows;
//    }

    /**
     * 按照进行分数排序
     * @param searchString
     * @param pageNo
     * @param pageSize
     * @param userId
     * @return
     * @throws IOException
     */
    public List<EsSearchVo> searchPageByScore(String searchString, int pageNo, int pageSize, Long userId) throws IOException {
        if(pageNo<0)//检验分页合法性
            pageNo=0;
        List<EsSearchVo> esSearchVoList = searchPage(searchString, userId);
        esSearchVoList=  esSearchVoList.stream()
                .sorted(Comparator.comparing(EsSearchVo::getScore).reversed())//按分数排序
                .skip((pageNo)*pageSize).limit(pageSize)//分页
                .collect(Collectors.toList());
        return esSearchVoList;
    }


    /**
     * 按照时间搜索排序
     * @param searchString
     * @param pageNo
     * @param pageSize
     * @param userId
     * @return
     * @throws IOException
     */
    public List<EsSearchVo> searchPageByTime(String searchString, int pageNo, int pageSize, Long userId) throws IOException {
        if(pageNo<0)//检验分页合法性
            pageNo=0;
        List<EsSearchVo> esSearchVoList = searchPage(searchString, userId);
        esSearchVoList= esSearchVoList.stream()
                .sorted(Comparator.comparing(EsSearchVo::getCreatetime,String::compareTo).reversed())//按时间排序近到远
                .skip((pageNo)*pageSize).limit(pageSize)
                .collect(Collectors.toList());
        return esSearchVoList;
    }


    //TODO es的检索结果
    //实现搜索功能
    public List<EsSearchVo> searchPage(String searchString,  Long userId) throws IOException {
        // TODO: 2023/3/22 优化检索出的信息，以及解析JSON格式字符串
        //如果分页太小也从第一个开始分页


        //条件搜索
        SearchRequest searchRequest = new SearchRequest("pdfpage");
        //构造器
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //匹配
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("all", searchString);
        sourceBuilder.query(queryBuilder)
                .timeout(TimeValue.timeValueSeconds(30));


        //执行搜索
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse=restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
        log.info("获得结果数:"+searchResponse.getHits().getHits().length);
        //解析结果

        //存入list
        ArrayList<EsSearch1> list =new ArrayList<>();

        //遍历收集成map集合
        for (SearchHit hit : searchResponse.getHits()) {
            EsSearch1 esSearch1=new EsSearch1(
                    (Long) hit.getSourceAsMap().get("pdfId"),
                    (Integer) hit.getSourceAsMap().get("pdfPage"),
                    (String)hit.getSourceAsMap().get("createtime"),
                    hit.getScore()
                    );
            list.add(esSearch1);
        }

        //前端显示的对象列表
        List<EsSearchVo> esSearchVoList=new ArrayList<>();
        //分类处理map<pdf的id,分数>

        Map<Long,List<EsSearch1>> pdfIdListMap=list.stream().collect(Collectors.groupingBy(EsSearch1::getPdfId));//按照pdfId进行分类
        for(Map.Entry<Long,List<EsSearch1>> entry : pdfIdListMap.entrySet()) {//for遍历
            List<EsSearch1> esSearch1s = entry.getValue();
            //前端显示对象
            EsSearchVo esSearchVo=new EsSearchVo();
            for (int i = 0; i < esSearch1s.size(); i++) {
                EsSearch1 esSearch1 = esSearch1s.get(i);
                if(i==0){
                    //循环初始化
                    esSearchVo.setPdfId(esSearch1.getPdfId());
                    if(userId!=0)//正常状态
                        esSearchVo.setTitle(pdfFileService.fileEcho(userId, esSearch1.getPdfId()).getPdfTitle());
                    else //测试状态
                        esSearchVo.setTitle("测试显示");
                    esSearchVo.setPdfPages("命中页数为:");
                    esSearchVo.setScore(0f);
                    esSearchVo.setCreatetime(esSearch1.getCreatetime());
                }
                esSearchVo.setPdfPages(esSearchVo.getPdfPages()+" "+esSearch1.getPdfPage());
                esSearchVo.setScore(esSearchVo.getScore()+esSearch1.getScore());
            }
            esSearchVoList.add(esSearchVo);

        }





        //TODO 2023/3/24 如果按照时间排序


        return esSearchVoList;
    }
/*


    //实现搜索功能
    public Object searchPage(String searchString, int pageNo, int pageSize) throws IOException {
        //如果分页太小也从第一个开始分页
        if(pageNo<=0){
            pageNo=0;
        }

        log.info("搜索内容为"+searchString+"开始页:"+pageNo+"结束页:"+pageSize);


        //条件搜索
        SearchRequest searchRequest = new SearchRequest("pdf");
        //构造器
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();


        //分页
        sourceBuilder.from(pageNo);
        sourceBuilder.size(pageSize);
        //匹配
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("text", searchString);
        sourceBuilder.query(queryBuilder)
                .timeout(TimeValue.timeValueSeconds(10));

        //执行搜索
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse=restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
        log.info("es响应:"+searchResponse.getHits().getHits().length);
        //解析结果

        System.out.println(JSON.toJSONString(searchResponse.getHits()));//打印命中集合


        return searchResponse.getHits().getHits();
    }*/




//TODO pdf的检索结果

    public Object getdoc(Long pdfId,String searchString) throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();


        //高亮设置
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("esfathernested.esvalue")
                .preTags("<em>")
                .postTags("</em>")
                .fragmentSize(50);//前后50字

        searchSourceBuilder.highlighter(highlightBuilder);






        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("pdfId", pdfId))
                .should(QueryBuilders.nestedQuery("esfathernested",QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("esfathernested.esvalue",searchString)), ScoreMode.Avg)
                        .innerHit(new InnerHitBuilder().setName("inner_hits").setHighlightBuilder(highlightBuilder)));//内部高亮

        searchSourceBuilder.query(queryBuilder);



        //构造请求
        SearchRequest searchRequest = new SearchRequest("pdfpage");
        searchRequest.source(searchSourceBuilder);
        //获得响应
        SearchResponse searchResponse=restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);


        // 获取搜索结果
        SearchHit[] hits = searchResponse.getHits().getHits();


        log.info(JSON.toJSONString(hits));
        //构造前端显示结果
        Map<Integer,List<EsNestedChild>> listMap=new HashMap<>();

        //构造返回结果
        for (SearchHit hit : hits) {//遍历父文档

            List<EsNestedChild> esNestedChildren=new ArrayList<>();

            Map<String, SearchHits> innerHits = hit.getInnerHits();

            SearchHits inner_hits = innerHits.get("inner_hits");
            for (SearchHit inner_hit : inner_hits) {//子文档遍历
                Map<String, Object> sourceAsMap = inner_hit.getSourceAsMap();
                String estype = switchKey(sourceAsMap);
                ////////
                Map<String, HighlightField> highlightFields = inner_hit.getHighlightFields();
                HighlightField highlightField = highlightFields.get("esfathernested.esvalue");
                Text[] fragments = highlightField.fragments();
                String[] valueStrings = Texts2Strings(fragments);
                ////////
                for (int i = 0; i < valueStrings.length; i++) {
                    esNestedChildren.add(new EsNestedChild(estype,valueStrings[i]));
                }

            }

            if(esNestedChildren.size()!=0)
            listMap.put((Integer) hit.getSourceAsMap().get("pdfPage"),esNestedChildren);
        }


        return listMap;
    }


    private String[] Texts2Strings(Text[] fragments){

        String[] fragmentStrings=new String[fragments.length];
        for (int i = 0; i < fragments.length; i++) {
            fragmentStrings[i]=fragments[i].string();//只返回一个片段
        }
        return fragmentStrings;
    }

    private String switchKey(Map<String, Object> sourceAsMap) {

        String estype;
        String stringkey = (String) sourceAsMap.get("estype");
        switch (stringkey) {
                case "text" : {
                    estype="来源于正文";
                    break;
                }
                case "title" : {
                    estype="来源于标题";
                    break;
                }
                case "figure" : {
                    estype="来源于图片";
                    break;
                }
                case "figure_caption" : {
                    estype="来源于图片标题";
                    break;
                }
                case "table" : {
                    estype="来源于表格";
                     break;
                }
                case "table_caption" : {
                    estype="来源于表格标题";
                    break;
                }
                case "header" : {
                    estype="来源于页眉";
                    break;
                }
                case "footer" : {
                    estype="来源于页脚";
                    break;
                }
                case "reference" : {
                    estype="来源于引用";
                    break;
                }
                case "equation" : {
                    estype="来源于公式";
                    break;
                }
                default:{
                    estype="来源于其他";
                    break;
                }
            }
            //esNestedChild.setEsvalue((String) sourceAsMap.get("esvalue"));
        return estype ;
    }


//
//
//    /**
//     * 资料整理
//     * 获取文档类
//     * @param pdfId
//     * @param searchString
//     * @return
//     */
//    public Object getdoc(long pdfId,String searchString) throws IOException {
//
//        //构建搜索条件
//        SearchSourceBuilder sourceBuilder=new SearchSourceBuilder();
//        //使用QueryBuilders工具实现
//        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
//                .must(QueryBuilders.termQuery("pdfId", pdfId))
//                .should(QueryBuilders.matchQuery("esfathernested.esvalue", searchString));
//
//        sourceBuilder.query(queryBuilder);
//
//        //高亮
//
//        HighlightBuilder highlightBuilder = new HighlightBuilder();
//        highlightBuilder
//                //.field("all")
//                .field("text")
//                .preTags("<em>")
//                .postTags("</em>")
//                .fragmentSize(50).numOfFragments(1);//前后50字
//
//        sourceBuilder.highlighter(highlightBuilder);//自定义设置高亮
//        //创建请求
//        SearchRequest searchRequest = new SearchRequest("pdfpage");
//        searchRequest.source(sourceBuilder);
//        //获得响应
//        SearchResponse searchResponse=restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
//
//        //////////////////
//
//        // 获取搜索结果
//        SearchHits hits = searchResponse.getHits();
//
//
//        // 处理每个结果并输出高亮结果
//        //System.out.println(get50WordListBySearchHits(hits, "figure"));
//        //////////////////
//
//        return get50WordListBySearchHits(hits, "figure");
//        //return JSON.toJSONString(searchResponse.getHits().getHits());//打印命中集合
//    }
//    /**
//     *
//     * 通过命中结果获取
//     * @param hits
//     * @param field
//     * @return
//     */
//    public ArrayList<HashMap> get50WordListBySearchHits(SearchHits hits,String field){
//        ArrayList<HashMap> result=new ArrayList<>();//list
//
//
//        for (SearchHit hit : hits) {
//            // 获取高亮结果
//            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
//            HighlightField highlightField = highlightFields.get(field);
//
//            // 处理高亮结果
//            if (highlightField != null) {
//                Text[] fragments = highlightField.fragments();
//                String fragmentString = "";
//                for (Text text : fragments) {
//                    fragmentString += text;
//                }
//                set.add((Integer) hit.getSourceAsMap().get("pdfPage"));
//                strings.add(fragmentString);
//            }
//        }
//        for (SearchHit hit : hits) {//遍历每页
//            HashMap<Integer,Object> map=new HashMap<>();//map<page,StringList>
//            ArrayList<Mate> mateList = new ArrayList<>();//mateList
//            //HashMap<String,String> Mate=new HashMap<>();//map<field,String>
//
//            log.info("解析hit"+hit.getSourceAsMap());
//            //获取每页高亮结果
//            Map<String,HighlightField> highlightFieldMap=hit.getHighlightFields();
//            //
//
//
//            HighlightField highlightFieldText = highlightFieldMap.get("text");
//            String stringByHighlightField = getStringByHighlightField(highlightFieldText);
//            if(!stringByHighlightField.equals(""))
//            mateList.add(new Mate("来源于正文", stringByHighlightField));
//
//            HighlightField highlightFieldTitle = highlightFieldMap.get("title");
//            String stringByHighlightField1 = getStringByHighlightField(highlightFieldTitle);
//            if(!stringByHighlightField1.equals(""))
//            mateList.add(new Mate("来源于标题",stringByHighlightField1));
//
//            HighlightField highlightFieldFigure = highlightFieldMap.get("figure");
//            String stringByHighlightField2 = getStringByHighlightField(highlightFieldFigure);
//            if(!stringByHighlightField2.equals(""))
//            mateList.add(new Mate("来源于图片", stringByHighlightField2));
//
//
////            HighlightField highlightFieldFigureCaption = highlightFieldMap.get("figure_caption");
////            mateList.add(new HashMap<String,String>().put("来源于图片标题",getStringByHighlightField(highlightFieldFigureCaption)));
////
////            HighlightField highlightFieldTable = highlightFieldMap.get("table");
////            mateList.add(new HashMap<String,String>().put("来源于表格",getStringByHighlightField(highlightFieldTable)));
////
////            HighlightField highlightFieldTableCaption = highlightFieldMap.get("table_caption");
////            mateList.add(new HashMap<String,String>().put("来源于表格标题",getStringByHighlightField(highlightFieldTableCaption)));
////
////            HighlightField highlightFieldHeader = highlightFieldMap.get("header");
////            mateList.add(new HashMap<String,String>().put("来源于页眉",getStringByHighlightField(highlightFieldHeader)));
////
////
////            HighlightField highlightFieldFooter = highlightFieldMap.get("footer");
////            mateList.add(new HashMap<String,String>().put("来源于页脚",getStringByHighlightField(highlightFieldFooter)));
////
////            HighlightField highlightFieldReference = highlightFieldMap.get("reference");
////            mateList.add(new HashMap<String,String>().put("来源于引用",getStringByHighlightField(highlightFieldReference)));
////
////            HighlightField highlightFieldEquation = highlightFieldMap.get("equation");
////            mateList.add(new HashMap<String,String>().put("来源于公式",getStringByHighlightField(highlightFieldEquation)));
//
//            map.put((Integer) hit.getSourceAsMap().get("pdfPage"),mateList);
//            log.info("这是map:"+map.toString());
//            result.add(map);
//        }
//        return result;
//    }
//
//
//    public String getStringByHighlightField(HighlightField highlightField){
//        String fragmentString = "";
//            // 处理高亮结果
//        if (highlightField != null) {
//            Text[] fragments = highlightField.fragments();
//            for (Text text : fragments) {
//                fragmentString += text;
//            }
//            //set.add((Integer) hit.getSourceAsMap().get("pdfPage"));
//           // strings.add(fragmentString);
//        }
//        return fragmentString;
//    }


    /**
     * 保存到es
     * @param objects
     * @return
     */
    public boolean save2ES(ArrayList<EsDocumentBo> objects) {


        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout(TimeValue.timeValueSeconds(50));//留给他50秒的响应
        log.info("正在存入");
        //循环添加到批量存储
        for (Object object: objects) {
            bulkRequest.add(new IndexRequest("pdfpage")//文档名
                    .source(com.alibaba.fastjson.JSON.toJSONString(object), //转json
                            XContentType.JSON));//固定的格式参数,不用管他
        }

        BulkResponse bulk = null;
        try {
            bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);//开始批量存储的请求
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (bulk.hasFailures())
        {
            for (BulkItemResponse item : bulk.getItems()) {
                if (item.isFailed())
                    log.error(item.getFailureMessage());
            }
        }
        return  !bulk.hasFailures();//返回成功与否

    }
}
