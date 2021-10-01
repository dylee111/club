package org.zerock.club.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Log4j2
@Getter
@Setter
@ToString
public class ClubAuthMemberDTO extends User {

    private String email;
    private String name;
    private boolean fromSocial;

    public ClubAuthMemberDTO(String username, String password, boolean fromSocial,
                             Collection<? extends GrantedAuthority> authorities) {

        super(username, password, authorities);
        this.email = username;        // User 클래스에 선언된 username(ID에 해당)을 email과 매칭
        this.fromSocial = fromSocial;
    } // Constructor end
}
