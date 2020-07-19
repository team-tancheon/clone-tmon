package com.tancheon.tmon.interceptor;

import com.tancheon.tmon.common.util.StringUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {
            String token = String.valueOf(session.getAttribute("clonetmon-token"));

            // check token
            if (!StringUtil.isEmpty(token)) {

            }
        }

        response.sendRedirect("/signin");

        return true;
    }
}
