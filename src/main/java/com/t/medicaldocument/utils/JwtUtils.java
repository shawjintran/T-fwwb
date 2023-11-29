package com.t.medicaldocument.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtUtils {
  private static String secret;
  private static String header;
  private static Integer expire;
  public static String generateToken(Integer u_id) {
    Date nowDate = new Date();
    Date expireDate = new Date(nowDate.getTime() + 1000 * expire);
    HashMap<String, Object> payload = new HashMap<>();
    payload.put("id",u_id);
    return Jwts.builder()
        .setHeaderParam("typ", "JWT")
        .setClaims(payload)
        .setExpiration(expireDate)    // 7天过期
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
  }

  // 解析JWT
  public static Claims getClaimsByToken(String jwt) {
    try {
      return Jwts.parser()
          .setSigningKey(secret)
          .parseClaimsJws(jwt)
          .getBody();
    } catch (Exception e) {
      return null;
    }
  }

  // 判断JWT是否过期
  public static boolean isTokenExpired(Claims claims) {
    return claims.getExpiration().before(new Date());
  }
//  @Value("${jwt.secret}")
  public  void setSecret(String secret) {
    JwtUtils.secret = secret;
  }
//  @Value("${jwt.header}")
  public void setHeader(String header) {
    JwtUtils.header = header;
  }
//  @Value("${jwt.expire}")
  public  void setExpire(Integer expire) {
//    JwtUtils.expire = Integer.parseInt(expire);
    JwtUtils.expire = expire;
  }
}
