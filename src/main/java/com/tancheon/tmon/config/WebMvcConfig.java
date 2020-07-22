package com.tancheon.tmon.config;

import com.tancheon.tmon.interceptor.SessionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

//@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

//    private List<String> sessionInterceptPatterns = Arrays.asList("/**");
//    private List<String> sessionExcludePatterns = Arrays.asList("/static/**", "/signin", "/error/**");
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new SessionInterceptor())
//                .addPathPatterns(sessionInterceptPatterns)
//                .excludePathPatterns(sessionExcludePatterns);
//    }
}
