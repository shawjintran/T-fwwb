package com.t.medicaldocument.interceptor;

import com.alibaba.fastjson.JSON;
import com.t.medicaldocument.utils.R;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@Slf4j
public class ServiceInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
//    判断服务存在 逻辑

    log.info(request.getRemoteHost());
//  需要先设置编码，再获取流，避免乱码
    response.setCharacterEncoding("utf-8");
    PrintWriter writer = response.getWriter();
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Cache-Control", "no-cache");

//    response.setContentType("application/json;charset=utf-8");
//    String jsonString = JSON.toJSONString(R.fail("").setMes("Sorry,Server not provide this service").setCode(400));
    response.setContentType("text/html;charset=utf-8");
    String jsonString = "服务器暂不提供此服务，";
    writer.write(jsonString);
    writer.flush();
//  2023/3/31  无法解决中文乱码  2023/11/24 解决
    return false;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
    HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
  }
}
