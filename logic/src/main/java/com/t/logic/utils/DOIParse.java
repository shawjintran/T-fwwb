package com.t.logic.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;


import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.UUID;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.index.analysis.Analysis;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DOIParse {
  private static final String sciHubUrl = "https://www.sci-hub.se/";
  public static String parse(String doi){
    try {
      String pdfUrl = fetchPdfUrl(doi, sciHubUrl);
      if (pdfUrl != null) {
        String filename = UUID.randomUUID()
            .toString()
            .replace("-","").substring(0,8);
        downloadPdf(sciHubUrl+pdfUrl, FileUtils.temp_location+filename+ ".pdf");
        System.out.println(filename+" PDF下载完成.");
      } else {
        System.err.println("未能获取PDF下载链接.");
      }
    } catch (IOException | URISyntaxException e) {
      e.printStackTrace();
    }
    return "1";
  }
  // 创建一个HttpClient实例，允许设置代理以解决域名解析问题
  private static CloseableHttpClient createHttpClient() {
    // 如果需要通过代理访问，可以添加如下代理配置
     Proxy proxy = new Proxy(
         Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10809));
     RequestConfig config = RequestConfig.custom().setProxy(new HttpHost(
         String.valueOf(proxy.address()))).build();
    // 若无代理需求，则不设置

    // 创建HttpClient对象
    return HttpClients.custom()
        .setDefaultRequestConfig(RequestConfig.DEFAULT)
//         若有代理，这里添加
//        .setProxy(config.getProxy())
        .build();
  }
  private static String fetchPdfUrl(String doi, String sciHubUrl) throws IOException, URISyntaxException {
    CloseableHttpClient httpClient = createHttpClient();
    HttpGet httpGet = new HttpGet(new URI(sciHubUrl + doi));

    try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
      String html = EntityUtils.toString(response.getEntity(), "UTF-8");

//          <html>
//    <head>
//        <title>Sci-Hub: 未找到文章</title>
//        <meta charset="UTF-8">
//        <meta name="viewport" content="width=device-width, initial-scale=1.0">
//    </head>
//    <body>
//        <style type = "text/css">

//      <html>
//    <head>
//        <title>Sci-Hub | MRZ code extraction from visa and passport documents using convolutional neural networks. International Journal on Document Analysis and Recognition (IJDAR) | 10.1007/s10032-021-00384-2</title>
//        <meta charset="UTF-8">
//        <meta name="viewport" content="width=device-width">
//        <script src="/scripts/jquery-3.6.0.min.js"></script>
//    </head>

          // 解析HTML文档
      Document doc = Jsoup.parse(html);

      // 查找并提取下载按钮元素
      Elements downloadButtons = doc.select("button[onclick*=location.href]");
      String downloadUrl = null;
      for (Element button : downloadButtons) {
        // 提取onclick属性值中的URL
        String onClickValue = button.attr("onclick");
        int start = onClickValue.indexOf("'") + 1;
        int end = onClickValue.lastIndexOf("'");
        downloadUrl =  onClickValue.substring(start, end);
        System.out.println("下载链接: " + downloadUrl);
      }
      return downloadUrl;
//      int pdfLinkIndex = html.indexOf("location.href");
//      if (pdfLinkIndex != -1) {
//        int startIndex = html.indexOf("href=\"", pdfLinkIndex) + 6;
//        int endIndex = html.indexOf("\"", startIndex);
//        return html.substring(startIndex, endIndex);
//      } else {
//        return null;
//      }
    }
  }

  private static void downloadPdf(String pdfUrl, String filename)
      throws IOException, URISyntaxException {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet(new URI(pdfUrl));

    try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
          entity.writeTo(fos);
        }
      } else {
        System.err.println("未获取到PDF内容.");
      }
    }
  }
}
