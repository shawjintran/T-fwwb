package com.t.logic.interceptor;

import cn.hutool.jwt.JWTUtil;
import com.t.logic.config.MException;
import com.t.logic.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LoginInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String token = request.getHeader("token");
    if (token==null)
      return false;
    Claims claimsByToken = JwtUtils.getClaimsByToken(token);
    boolean tokenExpired = JwtUtils.isTokenExpired(claimsByToken);
    if (tokenExpired)
      return false;
    Integer integer = claimsByToken.get("id", Integer.class);
    ThreadLocal<Integer> integerThreadLocal = new ThreadLocal<>();
    integerThreadLocal.set(integer);
    return true;
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
