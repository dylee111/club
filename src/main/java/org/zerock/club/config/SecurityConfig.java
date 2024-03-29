package org.zerock.club.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zerock.club.security.filter.ApiCheckFilter;
import org.zerock.club.security.filter.ApiLoginFilter;
import org.zerock.club.security.handler.ApiLoginFailhandler;
import org.zerock.club.security.handler.ClubLoginSuccessHandler;
import org.zerock.club.security.service.ClubUserDetailsService;
import org.zerock.club.security.util.JWTUtil;

@Configuration
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ClubUserDetailsService userDetailsService;

    @Bean // 스프링 빈을 생성
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        /* bcrypt라는 해시함수 이용 패스워드 암호화
        * 암호화된 복호화로 디코딩 불가, 매번 암호화된 값도 다름
        * */
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("user1") // 사용자 계정
//                .password("$2a$10$H6YWai6..rgdWp9jts3or.pUvlY3JrRWMkC9KOWBO/PQfgmRLVnxS")
//                .roles("USER");
//    } // configure()

    @Override
    // HttpSecurity : URL에 대한 권한을 설정.
    protected void configure(HttpSecurity http) throws Exception {

        /* http.authorizeRequests()
                .antMatchers("/sample/all").permitAll()
                .antMatchers("/sample/member").hasRole("USER"); // ENUM에서 선언한 USER*/

//        http.formLogin(); // 인가, 인증에 문제 시 로그인 페이지로 이동            // spring security에 설정된 로그인 과정을 거치게 함.
        http.authorizeRequests().and().formLogin().loginPage("/sample/login").loginProcessingUrl("/login").successHandler(successHandler()).failureUrl("/sample/login?error").permitAll();

        http.csrf().disable(); // CSRF 토큰을 발행하지 않기 위한 설정 // disable 설정 이유 : 개발 중에 에러 발생 시, csrf에서 발생했는지 알 수 없기 때문에 disable로 설정 후, 개발이 마무리 단계에서 적용하는 것이 유리
        http.logout();
        http.oauth2Login().successHandler(successHandler());
        http.rememberMe().tokenValiditySeconds(60 * 60 * 24 * 7) // 로그인 7일간 유지
                .userDetailsService(userDetailsService);

        // ApiCheckFilter가 User~Filter보다 이전에 동작하도록 지정
        http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(apiLoginFilter(), UsernamePasswordAuthenticationFilter.class);
    } //configure() -> 지정한 URL은 별도의 인증 없이 접근 가능 및 특정 URL에 권한 부여

    @Bean
    public ClubLoginSuccessHandler successHandler() {
        return new ClubLoginSuccessHandler(passwordEncoder());
    }

    @Bean
    public ApiCheckFilter apiCheckFilter() { return new ApiCheckFilter("/notes/**/*", jwtUtil()); } // ** -> /notes/ 하위의 모든 url을 포함. 해당 url로 접근할 때만 동작하도록 지정

    @Bean
    public ApiLoginFilter apiLoginFilter() throws Exception {
        ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/api/login", jwtUtil()); // 매개변수로 지정된 url로 접근할 때 동작하도록 지정
        // AbstractAuthenticationProcessingFilter는 AuthenticationManager가 반드시 필요. authenticationManager()로 추가
        apiLoginFilter.setAuthenticationManager(authenticationManager());  // API 인증 처리
        apiLoginFilter.setAuthenticationFailureHandler(new ApiLoginFailhandler()); // 로그인 실패

        return apiLoginFilter;
    }

    @Bean
    public JWTUtil jwtUtil() { return new JWTUtil(); }

}
