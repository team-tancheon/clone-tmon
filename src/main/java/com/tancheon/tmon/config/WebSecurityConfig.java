package com.tancheon.tmon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // references : https://victorydntmd.tistory.com/328
    @Override
    public void configure(WebSecurity web) throws Exception {
        // static 하위 목록은 인증 무시(= 항상 통과)
        web.ignoring().antMatchers("/static/**",
                        "/swagger-resources",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/swagger/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        // TODO - 추후 설정
//                .authorizeRequests()
//                .antMatchers().hasAnyRole()
//                .and()
//                    .formLogin()
//                    .loginPage("/view/signin")
//                    .loginProcessingUrl("/")
//                    .successHandler()
//                    .failureHandler()
//                    .usernameParameter("email")
//                    .passwordParameter("password")
//                .and()
//                    .logout()
//                    .logoutUrl()
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
