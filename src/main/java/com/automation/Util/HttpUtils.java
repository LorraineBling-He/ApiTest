package com.automation.Util;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;

import org.apache.log4j.Logger;
import com.automation.pojo.API;
import com.automation.pojo.Case;

import static com.automation.cases.BaseCase.urlReplace;
import static com.automation.constants.Constants.IP;

public class HttpUtils {
    public static Logger log=Logger.getLogger(HttpUtils.class);

    /**
     * call发起http请求
     * @param api
     * @param cases
     * @param isAuthor
     * @return
     */
    public static String call(API api, Case cases, boolean isAuthor){
        try {
            if (api.getContentType().equalsIgnoreCase("json")) {
                if (api.getType().equalsIgnoreCase("post")) {
                    return HttpUtils.jsonPost(api, cases,isAuthor);
                }else if(api.getType().equalsIgnoreCase("get")){
                    return HttpUtils.jsonGet(api,cases,isAuthor);
                }
            } else if (api.getContentType().equalsIgnoreCase("form")) {
                if (api.getType().equalsIgnoreCase("post")) {
                    return HttpUtils.formPost(api, cases,isAuthor);
                } else if (api.getType().equalsIgnoreCase("get")) {
                    return HttpUtils.formGet(api, cases,isAuthor);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 请求数据实体化，保证写入到数据库的中文不乱码
     * @param requestParam
     * @return HttpPost类型 对象
     */
    public static StringEntity entityJsonData(String requestParam){
        StringEntity entity = new StringEntity(requestParam, Charset.forName("UTF-8"));
        entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        entity.setContentType("application/json");
        return entity;
    }

    /**
     * json_post
     * @param api
     * @param cases
     * @param isAuthor
     * @return
     * @throws Exception
     */
    public static String jsonPost(API api,Case cases,boolean isAuthor) throws Exception{
        String url=IP+api.getUrl();
        url = urlReplace(url ,cases.getUrlParam());
        log.info("请求信息： "+api.getType()+" , "+api.getContentType()+" , "+url);
        log.info("请求参数："+cases.getParam());
        //1、创建post请求
        HttpPost post=new HttpPost(url);
        //2、请求头和参数
        post.addHeader("Connection","keep-alive");
        post.addHeader("Content-Type","application/json");
        post.addHeader("Host","test-doctool.cbim.org.cn");
        post.setEntity(entityJsonData(cases.getParam()));//StringEntity报异常
        if (isAuthor){
            AuthorizationTokenUtils.setTokenInRequest(post);
        }
        //3、创建客户端
        CloseableHttpClient client= HttpClients.createDefault();
        //4、执行并接受返回
        CloseableHttpResponse response=client.execute(post);
        String body=EntityUtils.toString(response.getEntity());
        return body;
        //5、格式化相应内容
       /* System.out.println(body);
        System.out.println(response.getStatusLine().getStatusCode());
        System.out.println(Arrays.toString(response.getAllHeaders()));*/
    }

    /**
     * json_get
     * @param api
     * @param cases
     * @param isAuthor
     * @return
     * @throws Exception
     */
    public static String jsonGet(API api,Case cases,boolean isAuthor) throws Exception{
        //1、创建request请求 2、选择请求方法method 3、填写url
        String url=IP+api.getUrl();
        url = urlReplace(url, cases.getUrlParam());
        if(cases.getParam()!=null) {
            url = url + "?" + cases.getParam();
        }
        log.info("请求信息： "+api.getType()+" , "+api.getContentType()+" , "+url);
        log.info("请求参数："+cases.getParam());
        HttpGet get=new HttpGet(url);
        //4、有参传参有头加头
        get.addHeader("","");
        if (isAuthor){
            AuthorizationTokenUtils.setTokenInRequest(get);
        }
        //5、创建客户端
        CloseableHttpClient client= HttpClients.createDefault();
        //6、点击发送,接收返回
        // 记录开始时间
        long startTime=System.currentTimeMillis();
        //execute会报异常，抛出去
        CloseableHttpResponse response=client.execute(get);
        //记录结束时间
        long endTime=System.currentTimeMillis();
        //响应时间
        long time=endTime-startTime;
        //7、返回格式化
        HttpEntity entity=response.getEntity();
        int status=response.getStatusLine().getStatusCode();
        Header[] allHeaders=response.getAllHeaders();
        String body=EntityUtils.toString(entity);
        return body;
        /*System.out.println(body);//这里不能再写EntityUtils.toString(entity)，该方法只能调用一次，调用后entity就为空了，所以后面要用body替代
        System.out.println(status);
        System.out.println(Arrays.toString(allHeaders));*/
    }

    public static String formPost(API api,Case cases,boolean isAuthor) throws Exception{
        String url=IP+api.getUrl();
        url = urlReplace(url, cases.getUrlParam());
        String param = ToUtils.json2kv(cases.getParam());
        log.info("请求信息： "+api.getType()+" , "+api.getContentType()+" , "+url);
        log.info("请求参数："+cases.getParam());
        //1、创建post请求
        HttpPost post=new HttpPost(url);
        //2、请求头和参数
        post.addHeader("Connection","keep-alive");
        post.addHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
        post.addHeader("Host","dev-doctool.cbim.org.cn");
        post.setEntity(new StringEntity(param));//StringEntity报异常
        if (isAuthor){
            AuthorizationTokenUtils.setTokenInRequest(post);
        }
        //3、创建客户端
        CloseableHttpClient client= HttpClients.createDefault();
        //4、执行并接受返回
        CloseableHttpResponse response=client.execute(post);
        String body=EntityUtils.toString(response.getEntity());
        return body;
        //5、格式化相应内容
       /* System.out.println(body);
        System.out.println(response.getStatusLine().getStatusCode());
        System.out.println(Arrays.toString(response.getAllHeaders()));*/

    }

    public static String formGet(API api,Case cases,boolean isAuthor) throws Exception{
        String url=IP+api.getUrl();
        url = urlReplace(url,cases.getUrlParam());
        if(cases.getParam()!=null) {
            url = url + "?" + cases.getParam();
        }
        log.info("请求信息： "+api.getType()+" , "+api.getContentType()+" , "+url);
        log.info("请求参数："+cases.getParam());
        //1、创建request请求 2、选择请求方法method 3、填写url
        HttpGet get=new HttpGet(url);
        //4、有参传参有头加头
        get.addHeader("Connection","keep-alive");
        get.addHeader("Content-Type","application/json");
        get.addHeader("Host","dev-doctool.cbim.org.cn");
        if (isAuthor){
            AuthorizationTokenUtils.setTokenInRequest(get);
        }
        //5、创建客户端
        CloseableHttpClient client= HttpClients.createDefault();
        //6、点击发送,接收返回
        // 记录开始时间
        long startTime=System.currentTimeMillis();
        //execute会报异常，抛出去
        CloseableHttpResponse response=client.execute(get);
        //记录结束时间
        long endTime=System.currentTimeMillis();
        //响应时间
        long time=endTime-startTime;
        //7、返回格式化
        HttpEntity entity=response.getEntity();
        int status=response.getStatusLine().getStatusCode();
        Header[] allHeaders=response.getAllHeaders();
        String body=EntityUtils.toString(entity);
        return body;
        /*System.out.println(body);
        System.out.println(status);
        System.out.println(Arrays.toString(allHeaders));*/
    }



}
