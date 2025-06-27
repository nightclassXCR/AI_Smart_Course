package com.dd.ai_smart_course.component;

import com.dd.ai_smart_course.utils.BaseContext;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import java.util.function.Function;

@Component
@Slf4j
public class JwtTokenUtil {
    private static long EXPIRATION = 1000*60*60*24;  //过期时间
    private static String KEY = "z4Z6e7J9H0k3N5q8T1w4Z7C9v2B5y8D1f4G7j1K4m6P9s3U6x0A8t5F2w7R0"; //密钥

    //生成令牌
    public String generateToken(String subject, String userID, String username, String role, String status){
        JwtBuilder jwtBuilder = Jwts.builder();
        String token = jwtBuilder
                //header
                .setHeaderParam("typ", "JWT")   //类型
                .setHeaderParam("alg", "HS256") //算法
                //payload
                .claim("userID", userID)        //自定义参数,用户id
                .setSubject(subject)    //主题
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .setId(UUID.randomUUID().toString())
                //signature
                .signWith(SignatureAlgorithm.HS256, KEY)  //加密算法及其密钥
                .compact();     //将三部分拼装
        log.info("id:{}", userID);
        BaseContext.setCurrentId(Integer.parseInt(userID));
        log.info("生成token: "+ token);
//        //生成日志
//        System.out.println("生成令牌: " + token);

        return token;
    }

    //从令牌中获取用户ID
    public Integer getUserIDFromToken(String token){
        Integer userID = null;
        Claims claims = getAllClaimsFromToken(token);
        try{
            String userIDString = claims.get("userID").toString();
            userID = Integer.valueOf(userIDString);
        }catch (Exception e){
            log.error("resolve fails: ", e);
        }

        return userID;
    }

    //"::" 是 Java 8 引入的 方法引用（Method Reference）语法，用于简化 Lambda 表达式
    //方法引用允许你直接引用现有方法，而不必显式编写 Lambda 表达式的完整形式。
    //Lambda 表达式的基本语法结构为: (参数列表) -> { 函数体 }
    //从令牌中获取过期时间
    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    //冲令牌中获取自定义声明
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimResolver.apply(claims);
    }

    //解析令牌
    private Claims getAllClaimsFromToken(String token){
        Claims claims =  Jwts.parser()
                .setSigningKey(KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims;
    }

    //判断令牌是否超时
    public Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //判断令牌是否有效
    public Boolean validateToken(String token,String userID){
        final Integer tokenUserID = getUserIDFromToken(token);
        return (tokenUserID.equals(userID) && !isTokenExpired(token));
    }

    //输出令牌信息
    public void printToken(String token){
        Claims claims = getAllClaimsFromToken(token);
        System.out.println();
        System.out.println("解析token: " + "ID = " + claims.getId() + "; subject: " + claims.getSubject() + "; expiration = " + claims.getExpiration()
                + "; userID = "+ claims.get("userID"));

    }


}
