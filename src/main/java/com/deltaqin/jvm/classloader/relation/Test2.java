package com.deltaqin.jvm.classloader.relation;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * @author deltaqin
 * @date 2021/6/1 下午4:40
 */
public class Test2 {
    public static void main(String[] args) throws IOException {
        // 线程创建者提供的
        // 线程上下文类加载器
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(contextClassLoader); // sun.misc.Launcher$AppClassLoader@18b4aac2

        String name = "com/deltaqin/jvm/classloader/relation/Test2.class";

        // 类加载器获取资源的路径
        Enumeration<URL> resource = contextClassLoader.getResources(name);

        while (resource.hasMoreElements()) {
            URL url = resource.nextElement();

            System.out.println(url);
        }


    }
}
