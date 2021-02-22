package com.automation.Util;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ToUtils {
    public static String json2kv(String json){
        String result=null;
        HashMap<String,String> map= JSONObject.parseObject(json,HashMap.class);
        //entry遍历
        Set<Map.Entry<String,String>> entry=map.entrySet();
        for(Map.Entry<String,String> i:entry){
            System.out.println(i);
        }
        //keyset遍历
        Set<String> keyset=map.keySet();
        for(String key:keyset){
            String value=map.get(key);
            if (result.length()>0){
            result+="&";
            }
            result+=key+"="+value;
        }
        return result;
    }
}
