package com.shark.search4SVN.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Created by liuqinghua on 16-9-9.
 */
@Controller
public class SpiderController {

    private static final Logger logger = LoggerFactory.getLogger(SpiderController.class);

    @RequestMapping(path = {"/spider"}, method = {RequestMethod.GET})
    public String spider(){
        return "spider";
    }

    @RequestMapping(path = {"/stepSpider"}, method={RequestMethod.POST})
    public String stepSpider(@RequestParam(value = "SVNURL", defaultValue = "") String svnUrl,
                             @RequestParam(value = "username", defaultValue = "") String username,
                             @RequestParam(value = "password", defaultValue = "") String password) throws Exception {
        //TODO 如何考虑让一个Spring MVC的拦截器来完成此项工作
        if(StringUtils.isEmpty(svnUrl) || StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            //TODO 返回错误信息 500，Spring boot 如何统一处理 500/404等错误？
            throw new Exception("svnUrl, username, password： one of them is null ");
        }

        logger.debug("svnURl : " + svnUrl);


        return "index";
    }



    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e) {
        return "error:" + e.getMessage();
    }

}
