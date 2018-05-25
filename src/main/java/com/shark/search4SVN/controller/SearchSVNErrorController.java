package com.shark.search4SVN.controller;


import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author linchangshun
 * @Description
 * @Date Created in 17:10 2018/5/24
 * 处理所有的错误
 */
@Controller
public class SearchSVNErrorController implements ErrorController {
    private static final String ERROR_PATH = "/error";
    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request){
        //获取statusCode:401,404,500
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if(statusCode == 401){
            return "/error/401";
        }else if(statusCode == 404){
            return "/error/404";
        }else if(statusCode == 403){
            return "/error/403";
        }else{
            return "/error/500";
        }

    }
}
