package org.zerock.club.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zerock.club.security.util.JWTUtil;


public class JWTTests {

    private JWTUtil jwtUtil;

    @BeforeEach // 각 테스트 메서드 실행 전에 먼저 실행
    public void testBefore() {
        System.out.println("testBefore............................");
        jwtUtil = new JWTUtil();
    }

    @Test
    public void testEncode() throws Exception {
        String email = "user95@ds.org";

        String str = jwtUtil.generateToken(email);

        System.out.println(str);
    }

    @Test
    public void testValidate() throws Exception {
        String email = "user95@ds.org";

        String str = jwtUtil.generateToken(email);

        Thread.sleep(5000);

        String resultEmail = jwtUtil.validateAndExtract(str);

        System.out.println(resultEmail);
    }
}
