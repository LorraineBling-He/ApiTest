package com.automation.Util;

import com.automation.constants.Constants;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCUtils {

/*    //加载驱动
    static{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }*/
    //获取连接(自己决定，封装到方法中)
    //static静态，直接使用类名.方法名更方便
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(Constants.JDBC_URL, Constants.JDBC_USER, Constants.JDBC_PASSWORD);
        }catch (Exception e){
            e.printStackTrace();
        }
        return conn;
    }
    //释放资源
    public static void close(Connection conn) {
        try{
            if(conn!=null){
                conn.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
