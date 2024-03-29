package org.zerock.club.security.filter;

import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.club.security.util.JWTUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter { // OncePerRequestFilter -> 스프링이 동작할 때마다 동작하는 기본 필터

    private AntPathMatcher antPathMatcher;
    private String pattern;
    private JWTUtil jwtUtil;

    public ApiCheckFilter(String pattern, JWTUtil jwtUtil) {
        this.antPathMatcher = new AntPathMatcher();
        this.pattern = pattern;
        this.jwtUtil = jwtUtil;
    } // Constructor end.

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info("REQUEST URI >>>>>> " + request.getRequestURI());

        boolean result = antPathMatcher.match(pattern, request.getRequestURI());
        log.info("antPathMatcher.match() >>>>>> " + result);

        if (result) {
            log.info("ApiCheckFilter ===========================================");
            log.info("ApiCheckFilter ===========================================");
            log.info("ApiCheckFilter ===========================================");

            boolean checkHeader = checkAuthHeader(request);

            if (checkHeader) {
                filterChain.doFilter(request, response);
                return;
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 - FORBIDDEN
                response.setContentType("application/json;charset=utf-8"); // json 리턴 및 한글깨짐 수정.

                JSONObject json = new JSONObject();
                String message = "FAIL CHECK API TOKEN";
                json.put("code", "403"); // 403 에러 발생
                json.put("message", message); // 에러 내용

                PrintWriter out = response.getWriter();
                out.print(json);

                return;
            }// if(checkHeader) end
        } // if(result) end

        filterChain.doFilter(request, response);
    } // doFilterInternal()

    /*
    *   내부에서 Authorization 헤더를 추출하여 검증하는 역할.
    * */
    private boolean checkAuthHeader(HttpServletRequest request) {
        boolean checkResult = false;

        String authHeader = request.getHeader("Authorization");

        /*if (authHeader.equals("12345678")) {
            checkResult = true;
        }*/

        // StringUtils
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            log.info("Authorization exist >>> " + authHeader);

            try {
                String email = jwtUtil.validateAndExtract(authHeader.substring(7));
                log.info("validate result >>> " + email);
                checkResult = email.length() > 0; // 값이 제대로 넘어가면 true.
            } catch (Exception e) {
                e.printStackTrace();
            }

        } // if end.


        return checkResult;
    } // checkAuthHeader()
}
