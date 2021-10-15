package org.zerock.club.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.club.security.dto.ClubAuthMemberDTO;
import org.zerock.club.security.service.ClubUserDetailsService;

@Controller
@Log4j2
@RequiredArgsConstructor
public class LoginController {

//    private final ClubUserDetailsService clubUserDetailsService;
//    private final PasswordEncoder passwordEncoder;

/*
    @PostMapping("/login")
    public String loginForm(String username, String password) {

        String resultUrl = "";
        log.info("custom login ===========================================");
        ClubAuthMemberDTO clubAuthMemberDTO = (ClubAuthMemberDTO) clubUserDetailsService.loadUserByUsername(username);
        log.info("clubAuthMemberDTO >>>>>>" + clubAuthMemberDTO);

        // 매개변수 password와 DTO에서 넘어오는 password 값 비교
        boolean passwordResult = passwordEncoder.matches(password, clubAuthMemberDTO.getPassword()); // rawPassword, 인코딩된 password

        if (passwordResult) {
            resultUrl = "/sample/member";
        } else {
            resultUrl = "redirect:/sample/login";
        }

        return resultUrl;
    } // loginForm()
*/

}
