package com.zl.yxt.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zl.yxt.pojo.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


//@Component
public class TokenUtil {

    private final static Logger log = LoggerFactory.getLogger(TokenUtil.class.getName());

    private static final long EXPIRE_TIME= 10*60*60*1000; //10小时
//    private static final long EXPIRE_TIME= 2*60*1000; //2分钟
    private static final String TOKEN_SECRET="yxt";  //密钥盐

    /**
     * 签名生成
     * @param users
     * @return
     */
    public static String sign(Users users){
        String token = null;
        // 设置头部信息
        Map<String, Object> header = new HashMap<>(2);
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            token = JWT.create()
                    .withHeader(header)
                    .withIssuer(users.getPhone()) //签发者
                    .withClaim("username", users.getUsername()) //自定义声明
                    .withExpiresAt(expiresAt) //过期时间
                    // 使用了HMAC256加密算法。
                    .sign(Algorithm.HMAC256(TOKEN_SECRET));
        } catch (Exception e){
            e.printStackTrace();
        }
        return token;
    }

    /**
     * 签名验证
     * @param token
     * @return
     */
    public static boolean verify(String token,String user){
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer(user).build();
            DecodedJWT jwt = verifier.verify(token);
//            log.info("=================> 认证通过");
//            log.info("=================> username: " + jwt.getClaim("username").asString());
//            log.info("=================> 过期时间:" + jwt.getExpiresAt());
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
