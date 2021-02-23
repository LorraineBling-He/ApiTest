package com.automation.constants;

public class Constants {
    //常量命名规则：所有英文单词都大写用下划线分割
    //final修饰变量，变量成为常量
    //final修饰类，类不能被继承（不常用）
    //final修饰方法，不能被重写（不常用）
    public static final String EXCEL_PATH="C:/Users/jjhe/Desktop/test.xls";
    public static final String IP="https://dev-doctool.cbim.org.cn";
    //实际响应数据回写列
    public static final int CELLNUM=6;
    //断言数据回写列
    public static final int ASSERTCELL=7;
    //数据库断言回写列
    public static final int SQLCELL=10;
    //总断言回写列
    public static final int FINALCELL=11;
    public static final String JDBC_URL="jdbc:mysql://127.0.0.1:3306/api-test?serverTimezone=UTC&characterEncoding=UTF8";
    public static final String JDBC_USER="root";
    public static final String JDBC_PASSWORD="123456";
    //config文件地址
    public static final String CONFIG_PATH="D:\\work\\CBIM\\接口自动化\\APIgit\\APItest-springboot\\src\\main\\resources\\paramConfig.properties";


}
