package com.automation.cases;

import com.automation.Starter;
import com.automation.Util.AuthorizationTokenUtils;
import com.automation.Util.ExcelUtils;
import com.automation.Util.HttpUtils;
import com.automation.Util.JDBCUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import io.qameta.allure.Step;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import com.automation.pojo.API;
import com.automation.pojo.Case;
import com.automation.pojo.JsonPathValidate;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.automation.Util.ExcelUtils.writeDataList;
import static com.automation.constants.Constants.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@SpringBootTest(classes = {Starter.class})
public class BaseCase extends AbstractTestNGSpringContextTests{

    public static Logger log=Logger.getLogger(BaseCase.class);

    @BeforeSuite
    @Step("初始化")
    public static void init(){
        InputStream fis =null;
        try {
            Properties properties = new Properties();
            //fis = new ExcelUtils().getClass().getClassLoader().getResourceAsStream(CONFIG_PATH);
            fis = ExcelUtils.class.getClassLoader().getResourceAsStream(CONFIG_PATH);
            properties.load(fis);
            for (Object key: properties.keySet()) {
                if(properties.get(key).equals("__random")){
                    properties.setProperty(key.toString(),String.valueOf(new Random().nextInt(9999)));
                }
                AuthorizationTokenUtils.env.put(key.toString(),properties.get(key).toString());
            }
            fis.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
        }
    }

    @AfterSuite
    public static void finish(){
        //回写
        ExcelUtils.batchWrite(writeDataList);
        //清理测试数据
    }

    public static void excute(API api, Case cases, String apiName) {
        log.info("****************开始"+apiName+"****************");
        //1、参数化替换
        cases.setParam(replace(cases.getParam()));
        cases.setUrlParam(replace(cases.getUrlParam()));
        cases.setAfterSql(replace(cases.getAfterSql()));
        //2、前置数据库
        //Object beforeSqlResult=sqlResult(cases.getBeforeSql());
        //3、执行接口获取响应
        String response = call(api, cases);
        //4、响应断言
        boolean resAssert=responseAssert(cases, response);
        //5、后置数据库
        //Object afterSqlResult=sqlResult(cases.getAfterSql());
        //log.info("前置数据库查询结果:"+beforeSqlResult.toString());
        //log.info("后置数据库查询结果:"+afterSqlResult.toString());
        //6、数据库断言
        //boolean sqlassert=sqlAssert(cases,beforeSqlResult,afterSqlResult);
        //7、总断言
        boolean sqlassert=true;
        String finalFlag = finalAssert(cases, resAssert, sqlassert);
        //8、测试报告断言
        Assert.assertEquals(finalFlag,"Pass");
        log.info("最终断言结果："+finalFlag);
        log.info("****************结束"+apiName+"****************");
        System.out.println("\n");
    }

    public static String finalAssert(Case cases, boolean resAssert, boolean sqlassert) {
        boolean finalAssert=true;
        if (resAssert&&sqlassert){
            finalAssert=true;
        }else{
            finalAssert=false;
        }
        String finalFlag=finalAssert?"Pass":"Fail";
        ExcelUtils.addWriteData(Integer.parseInt(cases.getId()),FINALCELL,String.valueOf(finalAssert));
        return finalFlag;
    }

    /**
     *
     * @param api
     * @param cases
     * @return
     */
    @Step("执行接口")
    public static String call(API api, Case cases) {
        String response= HttpUtils.call( api,cases,true);
        int rowNum = Integer.parseInt(cases.getId());
        ExcelUtils.addWriteData(rowNum,CELLNUM,response);
        log.info("返回信息："+response);
        return response;
    }

    /**
     * 将环境变量替换到入参中
     * @param source
     * @return
     */
    public static String replace(String source) {
        if(StringUtils.isBlank(source)){
            return source;
        }
        for (String key: AuthorizationTokenUtils.env.keySet()) {
            //判断入参中是否包含环境变量，包含就替换，replace这个函数如果找不到不会报错，也不替换，省掉if判断
            source=source.replace(key,AuthorizationTokenUtils.env.get(key));
        }
        return source;
    }


    /**
     * 断言，如果是json数组形式，就采用多个字段断言，如果不是数组形式，就采用等值匹配
     * @param ccase
     * @param response
     */
    private static boolean responseAssert(Case ccase, String response) {
        //json数组转list
        Object obj= JSONObject.parse(ccase.getExpectValue());
        boolean assertFinal = true;
        //获取类型，如果是{}，转成JsonObject。如果是[],转成JsonArray
        if(obj instanceof JSONArray) {
            //System.out.println(obj.getClass());
            List<JsonPathValidate> expectlist = JSONObject.parseArray(ccase.getExpectValue(), JsonPathValidate.class);
            for (JsonPathValidate jpv : expectlist) {
                String expression = jpv.getExpression();
                String expectValue = jpv.getValue();
                String actualValue = String.valueOf(JSONPath.read(response, expression)) == null ? "" : String.valueOf(JSONPath.read(response, expression));
                log.info(expression.replace("$.","")+"断言结果：实际值=" + actualValue + "     预期值=" + expectValue + "     断言结果=" + expectValue.equals(actualValue));
                if (!expectValue.equals(actualValue)) {
                    assertFinal = false;
                    break;
                }
            }
           ExcelUtils.addWriteData(Integer.parseInt(ccase.getId()), ASSERTCELL, String.valueOf(assertFinal));
            log.info("响应断言结果："+ assertFinal);
        }else if(obj instanceof JSONObject){
            //忽略
        }
        return assertFinal;
    }

    /**
     * 数据库查询
     * @param sql
     * @return
     */
    public static Object sqlResult(String sql){
        Object result = null;
        Connection conn=null;
        if (StringUtils.isBlank(sql)){
            result = "";
        }else {
            try {
                QueryRunner runner = new QueryRunner();
                conn = JDBCUtils.getConnection();
                result = runner.query(conn, sql, new ScalarHandler<Object>());//这里的Object泛型是为了定义返回的result是什么类型，所以可以写int，String等，如果写Object，那么可以写成new ScalarHandler()，返回就是Object

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                JDBCUtils.close(conn);
            }
        }
        //System.out.println("数据库查询结果："+result);
/*        WriteData wdre = new WriteData(Integer.parseInt(cases.getId()), 9, result.toString());
        wdResult.add(wdre);*/
        return result;
    }

    /**
     * 数据库断言
     * @param cases
     * @param beforeSqlResult
     * @param afterSqlResult
     * @return
     */
    public static boolean  sqlAssert(Case cases,Object beforeSqlResult,Object afterSqlResult){
        boolean sqlAssert=true;
        if(StringUtils.isBlank(cases.getBeforeSql())){
            return sqlAssert;
        }else{
            String aa=String.valueOf(beforeSqlResult);
            String bb=String.valueOf(afterSqlResult);
            if(beforeSqlResult.toString().equals("0")&&afterSqlResult.toString().equals("1")){
                sqlAssert=true;
            }else{
                sqlAssert=false;
            }
        }
        ExcelUtils.addWriteData(Integer.parseInt(cases.getId()),SQLCELL,String.valueOf(sqlAssert));
        log.info("数据库断言结果："+sqlAssert);
        return sqlAssert;
    }
    /**
     *
     //替换url上的参数
     * @param url
     * @param urlParam
     * @return
     */
    public static String urlReplace(String url, String urlParam) {
        if (urlParam!=null){
            String[] urlParameter=urlParam.split(";");
            //创建Pattern对象
            Pattern r= Pattern.compile("\\{(.*?)\\}");
            //创建matcher对象
            Matcher m=r.matcher(url);
            //替换参数
            int i=0;
            while (m.find()){
                url=url.replace(m.group(),urlParameter[i]);
                i++;
            }
        }
        return url;
    }

}
