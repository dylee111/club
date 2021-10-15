package org.zerock.club.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.club.dto.NoteDTO;
import org.zerock.club.security.dto.ClubAuthMemberDTO;
import org.zerock.club.security.util.JWTUtil;
import org.zerock.club.service.NoteService;

@Controller
@Log4j2
@RequestMapping("/sample")
public class SampleController {

    @Autowired
    private JWTUtil jwtUtil;

    @PreAuthorize("permitAll()")
    @GetMapping("/all")

    public void exAll() {
        log.info("exAll >>>>>>");
    }

    //@PreAuthrize("principal.username == #clubAuthMemberDTO.username") // 회원이 작성한 글 수정시에 해당 멤버의 username과 일치하는지 확인하고 일치하면 수정 및 삭제 가능 권한 부여
    @PreAuthorize("isAuthenticated()") // 인증된 회원은 모두 가능
    @GetMapping("/member")
    public void exMember(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMemberDTO) {
        log.info("exMember >>>>>>");

        log.info("-----------------------------");
        log.info(clubAuthMemberDTO);
    }

    //    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PreAuthorize("hasRole('ADMIN')") // ADMIN이라는 권한을 가졌는지 확인
    @GetMapping("/admin")
    public void exAdmin() {
        log.info("exAdmin >>>>>>");
    }

    @PreAuthorize("#clubAuthMemberDTO != null && #clubAuthMemberDTO.username eq\"user98@ds.org\"")
    // 매개변수 clubAuthMember와 매칭, #은 매개변수로 넘어오는 객체를 받기 위함.
    @GetMapping("/exOnly")
    public String exMemberOnly(@AuthenticationPrincipal ClubAuthMemberDTO clubAuthMemberDTO) {
        log.info("exMemberOnly >>>>>>>>>>>>>>>>>>>>>>>>>>");
        log.info(clubAuthMemberDTO);

        return "/sample/admin";
    }

    //    @PreAuthorize("principal.")
    @GetMapping("/notes")
    public String notes(Model model, @AuthenticationPrincipal ClubAuthMemberDTO clubAuthMemberDTO, RedirectAttributes redirectAttributes) throws Exception {

        try {
            String email = clubAuthMemberDTO.getEmail();
            log.info("email >>>>>>>>>>>>>>>>>>>>>>>>>>>> " + email);
            String str = jwtUtil.generateToken(email);
            log.info("str >>>>>>>>>>>>>>>>>>>>>>>>>>>>" + str);
            model.addAttribute("jwtValue", str);
            return "/sample/notes";
        } catch (NullPointerException e) {
            e.printStackTrace();
            return "redirect:../sample/login";
        }

//        if (!(email == null)) {
//            model.addAttribute("jwtValue", str);
//            return "/notes";
//        } else {
//            return "redirect:/";
//        }

    } // notes()

    @GetMapping("/login")
    public void login(String error, String logout, Model model) {
        log.info("login error >>> " + error);
        log.info("login logout" + logout);
        if (error != null) {
            model.addAttribute("error", "Login Error Check your Account");
        }
        if (logout != null) {
            model.addAttribute("logout", "logout");
        }
    }
}
