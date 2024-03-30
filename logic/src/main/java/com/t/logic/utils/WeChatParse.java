package com.t.logic.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WeChatParse {
  private static final Pattern WECHAT_ARTICLE_URL_PATTERN = Pattern.compile("^https://mp.weixin.qq.com/s/[^\\s]*$");

  private static boolean isValidWeChatArticleUrl(String urlStr) {
    return urlStr != null && !urlStr.isEmpty() && WECHAT_ARTICLE_URL_PATTERN.matcher(urlStr).matches();
  }
  public static String parse(String wechatUrl) {
    if (!isValidWeChatArticleUrl(wechatUrl))
      return null;
    String nodeCommand = "node"; // Node.js命令
    String scriptPath = "D:\\CodeOfJava\\Medical-Document-Frontend\\search-1\\toPdf.js"; // Puppeteer脚本路径

    String filename = UUID.randomUUID()
        .toString()
        .replace("-","").substring(0,8);

    String outputFile = FileUtils.temp_location+filename+ ".pdf";

    // 构建命令行参数
    ProcessBuilder pb = new ProcessBuilder(nodeCommand, scriptPath, wechatUrl, outputFile);

    try {
      Process process = pb.start();
      BufferedReader normalReader = new BufferedReader(
          new InputStreamReader(process.getInputStream()));
      BufferedReader errorReader = new BufferedReader(
          new InputStreamReader(process.getErrorStream()));

      String line = null;
      StringBuilder sb = new StringBuilder();
      while ((line = normalReader.readLine()) != null) {
        sb.append(line);
      }
      System.out.println(sb.toString());
      String errorLine;
      boolean errorFlag=false;
      while ((errorLine = errorReader.readLine()) != null) {
        System.out.println(errorLine);
        log.warn("脚本文件执行信息ErrorStream:{}", errorLine);
        errorFlag=true;
      }


      // 等待进程结束
      int exitCode = process.waitFor();
      if (exitCode == 0) {
        System.out.println("Web page converted to PDF successfully: " + outputFile);
        return filename+".pdf";
      } else {
        return null;
      }
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      return null;
    }
  }

}
