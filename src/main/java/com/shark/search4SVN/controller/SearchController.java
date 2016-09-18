package com.shark.search4SVN.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by liuqinghua on 16-9-13.
 * 用于查询的controller
 */
@Controller
public class SearchController {
    @RequestMapping(path = {"/toSearch"}, method = {RequestMethod.GET})
    public String index(){
        return "search";
    }

    @RequestMapping(path = {"/search"}, method={RequestMethod.POST})
    public void search(@RequestParam(value = "q", defaultValue = "") String searchKey){

    }
}
