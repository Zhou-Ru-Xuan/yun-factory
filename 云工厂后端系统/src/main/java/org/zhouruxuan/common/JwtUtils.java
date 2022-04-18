package org.zhouruxuan.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;
import org.zhouruxuan.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 jwt工具类
 */
public class JwtUtils {

    public static final long EXPIRE = 1000 * 60 * 60 * 24;//设置token过期时间
    public static final String APP_SECRET = "ukc8BDbRigUDaY6pZFfWus2jZWLPHO";//密钥加密编码

    /**
     * 生产token字符串
     * @return token字符串
     */
    public static String getJwtToken(User user ){
        //构建token字符串

        return Jwts.builder()
                //设置头部
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                //起个名字
                .setSubject("cloudFactory-user")
                //设置过期时间
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                //设置token主体部分，以key-value的方式存储用户信息
                .claim("id", user.getId())
                //签名哈希
                .signWith(SignatureAlgorithm.HS256, APP_SECRET)
                .compact();
    }

    /**
     * 判断token是否存在与有效
     * @param jwtToken token字符串
     * @return 有效值
     */
    public static boolean checkToken(String jwtToken) {
        if(StringUtils.isEmpty(jwtToken)) return false;
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断token是否存在与有效
     * @param request 请求
     * @return 有效值
     */
    public static boolean checkToken(HttpServletRequest request) {
        try {
            String jwtToken = request.getHeader("token");
            if(StringUtils.isEmpty(jwtToken)) return false;
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据token获取用户信息
     * @param request 请求
     * @return 用户id
     */
    public static String getUserIdByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("token");
        if(StringUtils.isEmpty(jwtToken)) return null;
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
        Claims claims = claimsJws.getBody();
        return (String) claims.get("id");
    }
}
