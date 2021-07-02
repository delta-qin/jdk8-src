package com.deltaqin.jvm.classloader;

/**
 * @author deltaqin
 * @date 2021/6/1 下午10:22
 */
public class LocationDir {
    public static void main(String[] args) {
        System.out.println(System.getProperty("sun.boot.class.path"));
        System.out.println(System.getProperty("java.ext.dirs"));
        System.out.println(System.getProperty("java.class.path"));
        // /tools.jar:/Users/deltaqin/workspace/java/jdk8-src/target/classes:/Users/deltaqin/workspace/java/jdk8-src/lib/junit-4.12.jar:/User
    }
}
