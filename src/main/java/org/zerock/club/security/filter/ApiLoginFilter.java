package org.zerock.club.security.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.zerock.club.security.dto.ClubAuthMemberDTO;
import org.zerock.club.security.util.JWTUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

    private JWTUtil jwtUtil;

    public ApiLoginFilter(String defaultFilterProcessesUrl, JWTUtil jwtUtil) {
        super(defaultFilterProcessesUrl);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        log.info("========================= ApiLoginFilter ===========================");
        log.info("attemptAuthentication");

        String email = request.getParameter("email");
        String pw = request.getParameter("pw");

        // localhost:8080/api/login?email=***&pw=**** 입력 시, 기본 페이지로 이동하지만 로그인 인증 처리 ok.
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, pw);

        return getAuthenticationManager().authenticate(authToken);
/*
        if (email == null) {
            throw new BadCredentialsException("email cannot be null");
        }
*/
    } // attemptAuthentication()

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        log.info("========================= ApiLoginFilter ===========================");
        log.info("successfulAuthentication >>> " + authResult);

        log.info(authResult.getPrincipal());

        // email address
        String email = ((ClubAuthMemberDTO) authResult.getPrincipal()).getUsername();

        String token = null;

        try {
            token = jwtUtil.generateToken(email);

            response.setContentType("text/plane");
            response.getOutputStream().write(token.getBytes()); // Client에 Token을 문자열로 전송하는 역할
            log.info(token);
        } catch (Exception e) {
            e.printStackTrace();
        }

    } // successfulAuthentication()
}
