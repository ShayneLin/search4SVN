package com.shark.search4SVN.interceptor;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author linchangshun
 * @Description
 * @Date Created in 16:11 2018/5/24
 * 检查拦截器，目前只实现了对用户爬取数据请求的过滤，也可以继续放大范围。
 */

@Component
public class CheckInterceptor implements HandlerInterceptor {
    private static Logger logger = Logger.getLogger(CheckInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String svnUrl = httpServletRequest.getParameter("SVNURL");
        String username = httpServletRequest.getParameter("username");
        String password = httpServletRequest.getParameter("password");
        System.out.println("Hello You Are Here Now!");
        if (StringUtils.isEmpty(svnUrl) || StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new Exception("svnUrl, username, password： one of them is null ");
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
