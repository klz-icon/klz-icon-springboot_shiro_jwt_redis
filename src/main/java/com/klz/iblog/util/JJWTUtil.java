package com.klz.iblog.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JJWTUtil {

    Logger logger = LoggerFactory.getLogger(JJWTUtil.class);

    @Value("${jjwt.secret}")
    public String secret;

    @Value("${jjwt.expirationTime}")
    public Integer expirationTime;

    @Value("${jjwt.refresh_expirationTime}")
    public Integer refresh_expirationTime;
    //1、头部
    //2、声明
    //3、签名

    /**
     * token: header(标题)、claims(有效荷载)、签名(sign）
     * claims有七个申明：
     * iss: 签发者
     * sub: 面向用户
     * aud: 接收者
     * iat(issued at): 签发时间
     * exp(expires): 过期时间
     * nbf(not before)：不能被接收处理时间，在此之前不能被接收处理
     * jti：JWT ID为web token提供唯一标识
     * 也可以自定义一些声明
     */
    public String createToken(String username,Long currentTimeMillis) {
        String token = null;
        try {
            //header:算法、类型
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("lgorithm", "HMAC256");
            map.put("type", "JWT");

            //设置toekn的过期时间
            Date expireTime = new Date(currentTimeMillis + (expirationTime * 1000));

            token = JWT.create()
                    .withHeader(map)
                    .withClaim("username", username)
                    .withClaim("currentTime",currentTimeMillis)
                    .withExpiresAt(expireTime)
                    .withIssuer("klz")
                    .withIssuedAt(new Date())
                    .sign(Algorithm.HMAC256(secret));
        }catch (IllegalArgumentException | JWTCreationException e){
            logger.error("token创建失败:"+e.getMessage());
//            throw new APIException(ResultCodeEnum.CREATE_TOKEN_FAIL.getCode(), ResultCodeEnum.CREATE_TOKEN_FAIL.getMessage()+":"+e.getMessage());
        }
        return token;
    }


    //验证token
    public boolean verify(String token){
        DecodedJWT jwt = null;
        try{
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer("klz")
                    .build();
            jwt = verifier.verify( token);
            return true;
        }catch (Exception e){
            logger.error("解析token失败:"+e.getMessage());
            return false;
//            throw new APIException(ResultCodeEnum.VERIFY_TOKEN_FAIL.getCode(), ResultCodeEnum.VERIFY_TOKEN_FAIL.getMessage()+":"+e.getMessage());
        }
    }

    //从token获取username
    public String getUsername(String token){
        DecodedJWT jwt = null;
        try{
            jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        }catch (Exception e){
            logger.error("从token获取username失败:"+e.getMessage());
            return null;
//            throw new APIException(ResultCodeEnum.GET_USERNAME_FROM_TOKEN.getCode(), ResultCodeEnum.GET_USERNAME_FROM_TOKEN.getMessage()+":"+e.getMessage());
        }
    }

    public Long getCurrentTime(String token){
        try{
            DecodedJWT decodedJWT=JWT.decode(token);
            return decodedJWT.getClaim("currentTime").asLong();

        }catch (JWTCreationException e){
            logger.error("从token获取currentTime失败:"+e.getMessage());
            return null;
        }
    }

    /**
     * 获得Token中的信息无需secret解密也能获得
     * @param token
     * @param claim
     */
    public String getClaim(String token, String claim) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            // 只能输出String类型，如果是其他类型返回null
            return jwt.getClaim(claim).asString();
        } catch (JWTDecodeException e) {
            logger.error("解密Token中的公共信息出现JWTDecodeException:",e.getMessage());
            return null;
//            throw new APIException(ResultCodeEnum.GET_CLAIMS_FROM_TOKEN.getCode(), ResultCodeEnum.GET_CLAIMS_FROM_TOKEN.getMessage()+":"+e.getMessage());
        }
    }


}


