package org.zerock.club.security.handler;

import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class ApiLoginFailhandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        log.info("login fail handler ==========================");
        log.info("AuthenticationException >>> " + exception.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // json 리턴
        response.setContentType("application/json;charset=utf-8");
//        response.setContentType("text/html;charset=utf-8");
        JSONObject json = new JSONObject();
        String message = exception.getMessage();
        json.put("code", "401");
        json.put("message", message);

        PrintWriter out = response.getWriter();
        log.info(json);
        out.print(json);
/*        out.print("<script>");
        out.print("alert('로그인 실패');");
        out.print("</script>");*/ // setContentType -> text/html 일 경우.
    } // onAuthenticationFailure()
}
