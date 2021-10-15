package org.zerock.club.security.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.zerock.club.entity.ClubMemberRole;
import org.zerock.club.security.dto.ClubAuthMemberDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

@Log4j2
public class ClubLoginSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private PasswordEncoder passwordEncoder;

    public ClubLoginSuccessHandler(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // User가 성공적으로 권한을 부여받았을 때 호출하는 메서드
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        log.info("------------------------------");
        log.info("onAuthenticationSuccess");

        ClubAuthMemberDTO authMember = (ClubAuthMemberDTO) authentication.getPrincipal();

        boolean fromSocial = authMember.isFromSocial();

        log.info("Need Modify Member ? " + fromSocial);

        boolean passwordResult = passwordEncoder.matches("1111", authMember.getPassword()); // rawPassword, 인코딩된 password

        if (fromSocial && passwordResult) {
            redirectStrategy.sendRedirect(request, response, "/member/modify?from=social");
            return;
        }

//        Collection<GrantedAuthority> roleList = authMember.getAuthorities();

        List<String> roleList = new ArrayList<>();
        authMember.getAuthorities().forEach(new Consumer<GrantedAuthority>() {
            @Override
            public void accept(GrantedAuthority grantedAuthority) {
                roleList.add(grantedAuthority.getAuthority());
            }
        });
        log.info("Auth >>>>>>>>>>>>>>>>>>>>>>>" + authMember.getAuthorities());

        String sendUrl = "";
        if (roleList.contains("ROLE_USER")) { sendUrl = "/sample/all"; }
        if (roleList.contains("ROLE_MANAGER")) { sendUrl = "/sample/member"; }
        if (roleList.contains("ROLE_ADMIN")) { sendUrl = "/sample/admin"; }
        log.info("sendUrl >>>>>> " + sendUrl);

        redirectStrategy.sendRedirect(request, response, sendUrl);
    }
}
