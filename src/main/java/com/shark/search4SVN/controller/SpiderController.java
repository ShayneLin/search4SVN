package com.shark.search4SVN.controller;

import com.shark.search4SVN.service.redis.SVNService;
import com.shark.search4SVN.service.wrapper.SVNAdapter;
import com.shark.search4SVN.service.disruptor.DisruptorScheduleService;
import com.shark.search4SVN.util.MessageUtil;
import com.shark.search4SVN.util.Search4SVNContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Created by liuqinghua on 16-9-9.
 */
@Controller
public class SpiderController {

    private static final Logger logger = LoggerFactory.getLogger(SpiderController.class);

    @Autowired
    private DisruptorScheduleService disruptorScheduleService;

    @RequestMapping(path = {"/spider"}, method = {RequestMethod.GET})
    public String spider(){
        return "spider";
    }

    @RequestMapping(path = {"/stepSpider"}, method={RequestMethod.POST})
    @ResponseBody
    public String stepSpider(@RequestParam(value = "SVNURL", defaultValue = "") String svnUrl,
                             @RequestParam(value = "username", defaultValue = "") String username,
                             @RequestParam(value = "password", defaultValue = "") String password) throws Exception {
        //TODO 如何考虑让一个Spring MVC的拦截器来完成此项工作
        if(StringUtils.isEmpty(svnUrl) || StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            //TODO 返回错误信息 500，Spring boot 如何统一处理 500/404等错误？
            throw new Exception("svnUrl, username, password： one of them is null ");
        }

        logger.info("检索 SVN 地址 svnURl : " + svnUrl);



      /*  ThreadScheduleService scheduleService = new ThreadScheduleService();
        scheduleService.init(svnUrl, username, password);
        scheduleService.setThreadManager(threadManager);
        threadManager.submitTask("0", scheduleService);*/

        SVNService svnService = (SVNService) Search4SVNContext.getBean("svnService");
        String key = MessageUtil.md5(MessageUtil.concat(username, password));

        SVNAdapter svnAdapter = new SVNAdapter(username,password);
        svnService.setSVNAdapter(key, svnAdapter);

        logger.info("svnService " + svnService.hashCode());
        logger.info("svnService map size " + svnService.getSvnMap().size());

        disruptorScheduleService.produceEvent(1, svnUrl,key, null);



        String responseBody = "启动爬虫成功 <a href='index'>返回首页</a>";
        //TODO 直接跳转到监控页面
        return responseBody;
    }



    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e) {
        return "error:" + e.getMessage();
    }

}
