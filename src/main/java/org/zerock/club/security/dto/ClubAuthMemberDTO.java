package org.zerock.club.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Log4j2
@Getter
@Setter
@ToString
/*
* OAuth2User : 구글의 회원정보를 가져오기 위함
* */
public class ClubAuthMemberDTO extends User implements OAuth2User {

    private String email;
    private String name;
    private String password;
    private boolean fromSocial;
    private Map<String, Object> attr;

    public ClubAuthMemberDTO(String username, String password, boolean fromSocial,
                             Collection<? extends GrantedAuthority> authorities,  Map<String, Object> attr) {

        this(username, password, fromSocial, authorities);
        this.attr = attr;
    }

    public ClubAuthMemberDTO(String username, String password, boolean fromSocial,
                             Collection<? extends GrantedAuthority> authorities) {

        super(username, password, authorities);
        this.email = username;        // User 클래스에 선언된 username(ID에 해당)을 email과 매칭
        this.fromSocial = fromSocial;
        this.password = password;
    } // Constructor end

    @Override
    public Map<String, Object> getAttributes() {return this.attr;}
}
