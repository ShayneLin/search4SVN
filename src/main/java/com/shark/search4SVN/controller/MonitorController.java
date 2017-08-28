package com.shark.search4SVN.controller;

import com.shark.search4SVN.service.disruptor.DisruptorScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by liuqinghua on 16-9-10.
 */
@Controller
public class MonitorController {


    @Autowired
    private DisruptorScheduleService disruptorScheduleService;

    @RequestMapping(path = {"/monitorSpider"}, method={RequestMethod.GET})
    public ModelAndView monitor(){
        ModelAndView mv = new ModelAndView("monitor");

        List<String> handledURLs = disruptorScheduleService.getHandled();

        mv.addObject("handledURLs", handledURLs);
        return mv;
    }

}
