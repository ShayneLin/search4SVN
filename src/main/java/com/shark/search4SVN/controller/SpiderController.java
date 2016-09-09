package com.shark.search4SVN.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by liuqinghua on 16-9-9.
 */
@Controller
public class SpiderController {


    @RequestMapping(path = {"/spider"}, method = {RequestMethod.GET})
    public String spider(){
        return "spider";
    }



}
