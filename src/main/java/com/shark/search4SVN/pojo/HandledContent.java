package com.shark.search4SVN.pojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by qinghualiu on 2017/8/26.
 */
public class HandledContent {
    private List<String> handled = Collections.synchronizedList(new ArrayList<String>());

    public void addHandled(String content){
        handled.add(content);
    }

    public List<String> getHandled(){
        List<String> copyList = new ArrayList<String>();
        Collections.copy(copyList, handled);
        return copyList;
    }
}
