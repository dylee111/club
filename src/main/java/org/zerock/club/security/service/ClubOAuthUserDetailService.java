package org.zerock.club.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.zerock.club.entity.ClubMember;
import org.zerock.club.entity.ClubMemberRole;
import org.zerock.club.repository.ClubMemberRepository;
import org.zerock.club.security.dto.ClubAuthMemberDTO;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClubOAuthUserDetailService extends DefaultOAuth2UserService {

    private final ClubMemberRepository clubMemberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("---------------------------");
        log.info("userRequest >>>>> " + userRequest); // org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest 객체

        String clientName = userRequest.getClientRegistration().getClientName();
        log.info("client Name >>>>>> " + clientName); // Google
        log.info(userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("===========================");
        oAuth2User.getAttributes().forEach((k, v) -> {
            log.info(k + " >>> " + v); // sub, picture, email, email_verified
        });

        String email = null;

        if(clientName.equals("Google")) {
            email = oAuth2User.getAttribute("email");
        }

        log.info("---------------------------");
        log.info("EMAIL >>> " + email);

        ClubMember member = saveSocialMember(email); // social member를 DB에 저장

//        return oAuth2User;

        ClubAuthMemberDTO clubAuthMemberDTO = new ClubAuthMemberDTO(
                member.getEmail(),
                member.getPassword(),
                true,
                member.getRoleSet().stream().map(
                        role -> new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toList()), oAuth2User.getAttributes());
                clubAuthMemberDTO.setName(member.getName());

        return clubAuthMemberDTO;
    }// loadUser()

    /*
    *  Social Member를 DB에 저장
    **/
    private ClubMember saveSocialMember(String email) {

        Optional<ClubMember> result = clubMemberRepository.findByEmail(email, true);

        if (result.isPresent()) { return result.get(); } // 있으면 끝.

        // 없으면 DB에 새로운 멤버 행 추가 (회원가입)
        ClubMember clubMember = ClubMember.builder()
                .email(email)
                .name(email)
                .password(passwordEncoder.encode("1111"))
                .fromSocial(true)
                .build();

        clubMember.addMemberRole(ClubMemberRole.USER);

        clubMemberRepository.save(clubMember);

        return clubMember;
    } //saveSocialMember()

} // 구글에서 넘어 온 로그인 정보를 캐치하는 역할의 클래스.
