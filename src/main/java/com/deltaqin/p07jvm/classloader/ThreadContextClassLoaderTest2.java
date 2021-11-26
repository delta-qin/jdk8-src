package com.deltaqin.p07jvm.classloader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author deltaqin
 * @date 2021/6/2 下午7:32
 */
public class ThreadContextClassLoaderTest2 {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // com.mysql.cj.jdbc.Driver 导致他的初始化
        //Class<?> aClass = Class.forName("com.mysql.cj.jdbc.Driver");

        Connection connection = DriverManager.getConnection("jdbc:mysql://loacalhost:3306/mytestdb", "username", "passs");

    }
}


