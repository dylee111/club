package org.zerock.club.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Log4j2
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean // 스프링 빈을 생성
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        /* bcrypt라는 해시함수 이용 패스워드 암호화
        * 암호화된 복화화가 불가, 매번 암호화된 값도 다름
        * */
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("user1") // 사용자 계정
//                .password("$2a$10$H6YWai6..rgdWp9jts3or.pUvlY3JrRWMkC9KOWBO/PQfgmRLVnxS")
//                .roles("USER");
//    } //configure()

    @Override
    // HttpSecurity : URL에 대한 권한을 설정.
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/sample/all").permitAll()
                .antMatchers("/sample/member").hasRole("USER"); // ENUM에서 선언한 USER

        http.formLogin(); // 인가, 인증에 문제 시 로그인 페이지로 이동
        // disable 설정 이유 : 개발 중에 에러 발생 시, csrf에서 발생했는지 알 수 없기 때문에 disable로 설정 후, 개발이 마무리 단계에서 적용하는 것이 유리
        http.csrf().disable(); // CSRF 토큰을 발행하지 않기 위한 설정
        http.logout();
    } //configure() -> 지정한 URL은 별도의 인증 없이 접근 가능 및 특정 URL에 권한 부여
}
