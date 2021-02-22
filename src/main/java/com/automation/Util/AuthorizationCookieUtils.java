package com.automation.Util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * 鉴权类
 */
public class AuthorizationCookieUtils {
    //模拟环境变量
    public static final Map<String,String> cookies=new HashMap<String,String>();
    public static final String RESPONSE_HEADER="Set-Cookie";
    public static final String REQUEST_HEADER="Cookie";
    public static final String COOKIE_NAME="JSESSIONID";

    //抓取cookies存到cookies缓存中
    public static void getCookieByResponse(HttpResponse response){
        //从响应头里面获取指定的头字段
        Header header= response.getFirstHeader(RESPONSE_HEADER);
        //如果header不为空
        if (header != null){
            //获取头字段的值
            String cookie=header.getValue();
            //如果头字段的值不为空
            if(StringUtils.isNoneBlank(cookie)){
                String[] values = cookie.split(";");
                for(String value:values){
                    //如果包含JESSIONID，那么放入缓存cookies中
                    if (value.contains(COOKIE_NAME)){
                        cookies.put(COOKIE_NAME,value);
                    }
                }
            }
        }
    }
    public static void addCookieInRequest(HttpRequest request){
        String value = cookies.get(COOKIE_NAME);
        //如果cookie不为空，添加到请求头中
        if(StringUtils.isNoneBlank(value)){
            request.setHeader(REQUEST_HEADER,value);
        }
    }












}
