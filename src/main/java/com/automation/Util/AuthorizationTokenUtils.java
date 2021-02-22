package com.automation.Util;

import com.alibaba.fastjson.JSONPath;
import org.apache.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 鉴权类
 */
public class AuthorizationTokenUtils {
    //模拟环境变量
    public static final Map<String,String> env=new HashMap<String,String>();

    /**
     *提取接口返回信息中的参数，作为下个接口的入参
     */
    public static void storeToken(String response){
        Object token = JSONPath.read(response,"$.data.token_info.token");
        Object memberId=JSONPath.read(response,"$.data.id");
        //登录成功
        if(token != null){
            env.put("token",token.toString());
            if(memberId != null){
                env.put("memberId",memberId.toString());
            }
        }
    }

    /**
     * 请求头设置鉴权
     * @param request
     */
    public static void setTokenInRequest(HttpRequest request){
        /*String token=env.get("token");
        if(StringUtils.isNoneBlank(token)){
            request.setHeader("Authorization",token);
        }*/
        request.setHeader("Cookie","tool.tk=628b6f3f29953bf6e8d7d46e54e98af1");

    }

}
