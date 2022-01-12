package com.bbkj.common.security;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2020/10/23 15:50
 */
public class DoFiler implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // res.setContentType("textml;charset=UTF-8");
        res.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        // log.info("前端的请求网址: " + request.getHeader("Origin"));
        // log.info("拦截器的sessionId: " + request.getSession().getId());
        // res.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8080");
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        res.setHeader("Access-Control-Max-Age", "0");
        res.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, " +
                "Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,role,token,user");
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setHeader("XDomainRequestAllowed", "1");
        // 图片上传会用到
        if ("OPTIONS".equals(request.getMethod())) {
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
