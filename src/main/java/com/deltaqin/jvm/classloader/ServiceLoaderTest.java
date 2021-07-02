package com.deltaqin.jvm.classloader;

import java.sql.Driver;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author deltaqin
 * @date 2021/6/2 下午7:01
 */
public class ServiceLoaderTest {
    public static void main(String[] args) {
        ServiceLoader<Driver> loader = ServiceLoader.load(Driver.class);

        Iterator<Driver> iterator = loader.iterator();

        while (iterator.hasNext()) {
            Driver driver = iterator.next();
            System.out.println(driver.getClass()); // class com.mysql.cj.jdbc.Driver
            System.out.println(driver.getClass().getClassLoader()); // sun.misc.Launcher$AppClassLoader@18b4aac2
        }

        System.out.println(Thread.currentThread().getContextClassLoader()); // sun.misc.Launcher$AppClassLoader@18b4aac2
        System.out.println(ServiceLoader.class.getClassLoader()); // null
    }
}
