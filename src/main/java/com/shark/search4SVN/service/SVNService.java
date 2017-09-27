package com.shark.search4SVN.service;

import com.shark.search4SVN.service.wrapper.SVNAdapter;
import com.shark.search4SVN.util.MessageUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liuqinghua on 2017-08-28.
 */
@Service("svnService")
public class SVNService {
    /**
     * 由于 一组（用户名，密码） 对应一个 SVNAdapter
     * 这里的key是 md5(用户名+密码)
     */
    private Map<String, SVNAdapter> svnMap = new ConcurrentHashMap<String, SVNAdapter>();

    public void setSVNAdapter(String username, String password, SVNAdapter svnAdapter) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String key = MessageUtil.md5(MessageUtil.concat(username, password));
        this.setSVNAdapter(key, svnAdapter);
    }

    public void setSVNAdapter(String key, SVNAdapter svnAdapter){
        if(svnMap.containsKey(key)) return;
        svnMap.put(key, svnAdapter);
    }

    public SVNAdapter getSVNAdapter(String key){
        SVNAdapter svnAdapter = svnMap.get(key);
        return svnAdapter;
    }

    public Map<String, SVNAdapter> getSvnMap() {
        return svnMap;
    }
}
