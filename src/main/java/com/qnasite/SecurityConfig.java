package com.qnasite;

import com.qnasite.user.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@EnableWebSecurity //모든 요청 URL이 스프링 시큐리티의 제어를 받도록 만드는 애너테이션
@Configuration //스프링의 환경설정 파일임을 의미하는 애너테이션
public class SecurityConfig {
    //@EnableWebSecurity 애너테이션을 사용하면 내부적으로 SpringSecurityFilterChain이 동작하여 URL 필터가 적용된다.
    private final UserSecurityService userSecurityService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeRequests().antMatchers("/**").permitAll()
                .and()
                .csrf().ignoringAntMatchers("/h2-console/**") //h2-console url은 무시한다.
                .and()
                .headers()
                .addHeaderWriter(new XFrameOptionsHeaderWriter(
                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                .and()
                .formLogin()
                .loginPage("/user/login")
                .defaultSuccessUrl("/")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout")) //로그아웃 URL을 /user/logout으로 설정
                .logoutSuccessUrl("/") // 로그아웃이 성공하면 루트(/) 페이지로 이동
                .invalidateHttpSession(true) //로그아웃시 생성된 사용자 세션도 삭제
        ;

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
