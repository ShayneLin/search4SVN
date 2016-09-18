package com.shark.search4SVN.controller;

import com.shark.search4SVN.util.Constants;
import com.shark.search4SVN.util.JedisAdapter;
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
    private JedisAdapter jedisAdapter;

    @RequestMapping(path = {"/monitorSpider"}, method={RequestMethod.GET})
    public ModelAndView monitor(){
         ModelAndView mv = new ModelAndView("monitor");

        //TODO 从redis中获取 SVNFile 的记录数
        Set<String> svnFileSet = jedisAdapter.smembers(Constants.SVNFILE);
        List<String> svnFiles = new ArrayList<String>();
        Iterator<String> it= svnFileSet.iterator();
        while(it.hasNext()){
            svnFiles.add(it.next());
        }

        //TODO 从redis中获取 SVNDir 的记录数
        Set<String> svnDirSet = jedisAdapter.smembers(Constants.SOLRDOC);
        List<String> svnDirs = new ArrayList<String>();
        it= svnDirs.iterator();
        while(it.hasNext()){
            svnDirs.add(it.next());
        }

        //TODO 从redis中获取已经处理的 url的记录
        Set<String> finishURLSet = jedisAdapter.smembers(Constants.FINISHEDURL);
        List<String> finishURLs = new ArrayList<String>();
        it= finishURLSet.iterator();
        while(it.hasNext()){
            finishURLs.add(it.next());
        }

        mv.addObject("svnFiles", svnFiles);
        mv.addObject("svnDirs", svnDirs);
        mv.addObject("finishURLs", finishURLs);
        return mv;
    }

    public void setJedisAdapter(JedisAdapter jedisAdapter) {
        this.jedisAdapter = jedisAdapter;
    }
}
