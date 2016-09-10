package com.shark.search4SVN.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by liuqinghua on 16-9-8.
 */

@Controller
public class IndexController {

    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET})
    public String indexController(){
        return "index";
    }


    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e) {
        return "error:" + e.getMessage();
    }

}
