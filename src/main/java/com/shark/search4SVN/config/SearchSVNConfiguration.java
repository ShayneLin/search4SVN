package com.shark.search4SVN.config;

import com.shark.search4SVN.interceptor.CheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Author linchangshun
 * @Description
 * @Date Created in 16:19 2018/5/24
 */
@Configuration
public class SearchSVNConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    private CheckInterceptor checkInterceptor;

    //注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkInterceptor).addPathPatterns("/stepSpider");
    }
}
