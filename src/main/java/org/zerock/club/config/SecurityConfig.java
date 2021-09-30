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
        /*bcrypt라는 해시함수 이용 패스워드 암호화
        * 암호화된 복화화가 불가, 매번 암호화된 값도 다름
        * */
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user1") // 사용자 계정
                .password("$2a$10$H6YWai6..rgdWp9jts3or.pUvlY3JrRWMkC9KOWBO/PQfgmRLVnxS")
                .roles("USER");
    } //configure()

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/sample/all").permitAll()
                .antMatchers("/sample/member").hasRole("USER");
        http.formLogin(); // 인가, 인증에 문제 시 로그인 페이지로 이동
        http.csrf().disable(); // CSRF 토큰을 사용하지 않기 위한 설정
        http.logout();
    } //configure() -> 지정한 URL은 별도의 인증 없이 접근 가능.
}
