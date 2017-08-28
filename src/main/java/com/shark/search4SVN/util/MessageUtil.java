package com.shark.search4SVN.util;

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by liuqinghua on 2017-08-28.
 */
public class MessageUtil {
    public static String md5(String msg) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5= MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密后的字符串
        String newstr=base64en.encode(md5.digest(msg.getBytes("utf-8")));
        return newstr;
    }

    public static String concat(String... strs){
        StringBuffer sb = new StringBuffer();
        for(String str:strs){
            sb.append(str);
        }
        return sb.toString();
    }
}
