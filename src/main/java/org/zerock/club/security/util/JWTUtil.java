package org.zerock.club.security.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class JWTUtil {

    private String secretKey = "zerock12345678";

    private long expire = 60 * 24 * 30; // 1개월 -> 분 단위

    /*
    *   JWT Token 생성
    * */
    public String generateToken(String content) throws Exception {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(
                        Date.from(ZonedDateTime.now()
                                .plusMinutes(expire)
                                .toInstant()))
                        /*Date.from(ZonedDateTime.now()
                                .plusSeconds(1)  // jwt 만료 확인 테스트용.
                                .toInstant()))*/
                .claim("sub", content)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8"))
                .compact();
    } // generateToken()

    /*
    *   인코딩된 문자열에서 원하는 값을 추출하는 역할
    * */
    public String validateAndExtract(String tokenStr) throws Exception {

        String contentValue = null;

        try {
            DefaultJws defaultJws = (DefaultJws) Jwts.parser().setSigningKey(secretKey.getBytes("UTF-8")).parseClaimsJws(tokenStr);

            log.info(defaultJws);
            log.info(defaultJws.getBody().getClass());

            DefaultClaims claims = (DefaultClaims) defaultJws.getBody();
            log.info("=====================");
            contentValue = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            contentValue = null;
        }

        return contentValue;
    } // validateAndExtract()

} // class end
