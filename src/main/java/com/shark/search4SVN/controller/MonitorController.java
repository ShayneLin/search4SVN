package com.shark.search4SVN.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by liuqinghua on 16-9-10.
 */
@Controller
public class MonitorController {



    @RequestMapping(path = {"/monitorSpider"}, method={RequestMethod.GET})
    public String monitor(){

        //TODO 从redis中获取 SVNFile 的记录数
        //TODO 从redis中获取 SVNDir 的记录数
        //TODO 从redis中获取 SolrDoc 的记录数


        return "monitor";
    }

}
