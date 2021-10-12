package org.zerock.club.Controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.club.security.dto.ClubAuthMemberDTO;

@Controller
@Log4j2
@RequestMapping("/sample")
public class SampleController {

    @PreAuthorize("permitAll()")
    @GetMapping("/all")
    public void exAll() {
        log.info("exAll >>>>>>");
    }

    //@PreAuthrize("principal.username == #clubAuthMember.username") // 회원이 작성한 글 수정시에 해당 멤버의 username과 일치하는지 확인하고 일치하면 수정 및 삭제 가능 권한 부여
    @PreAuthorize("isAuthenticated()") // 인증된 회원은 모두 가능
    @GetMapping("/member")
    public void exMember(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMemberDTO) {
        log.info("exMember >>>>>>");

        log.info("-----------------------------");
        log.info(clubAuthMemberDTO);
    }

    @PreAuthorize("hasRole('ADMIN')") // ADMIN이라는 권한을 가졌는지 확인
    @GetMapping("/admin")
    public void exAdmin() {
        log.info("exAdmin >>>>>>");
    }

    @PreAuthorize("#clubAuthMember != null && #clubAuthMember.username eq\"user98@ds.org\"")
    @GetMapping("/exOnly")
    public String exMemberOnly(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMember) {
        log.info("exMemberOnly >>>>>>>>>>>>>>>>>>>>>>>>>>");
        log.info(clubAuthMember);

        return "/sample/admin";
    }
}
