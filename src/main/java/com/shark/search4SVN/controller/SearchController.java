package com.shark.search4SVN.controller;

import com.shark.search4SVN.pojo.SVNDocument;
import com.shark.search4SVN.service.SearchService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by liuqinghua on 16-9-13.
 * 用于查询的controller
 */
@Controller
public class SearchController {

    private static Logger logger = Logger.getLogger(SearchController.class);

    @Autowired
    private SearchService searchService;

    @RequestMapping(path = {"/toSearch"}, method = {RequestMethod.GET})
    public String index(){
        return "search";
    }

    @RequestMapping(path = {"/search"}, method={RequestMethod.POST})
    public ModelAndView search(@RequestParam(value = "q", defaultValue = "") String searchKey){
        ModelAndView mv = new ModelAndView("searchResult");
        if(!StringUtils.isEmpty(searchKey)){
            try {
                List<SVNDocument> results = searchService.searchByKey(searchKey);

                mv.addObject("searchResults", mv);
            }catch(Exception e){
                logger.error(e.getMessage(), e);
            }

        }


        return mv;
    }

    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }
}
