package com.deltaqin.jvm.classloader;

/**
 * @author deltaqin
 * @date 2021/6/2 下午4:51
 */
//
//    当前类加载器：就是加载当前类的类加载器。每一个类都会尝试使用自己的类加载器去加载其他的类（依赖的类）
//                                      前提是被依赖的类还没有加载的情况下
//    线程上下文类加载器：是1.2 引入，Thread有一个set/getContextClassLoader
//                      如果没有使用这个来设置的话，线程就会继承父线程的上下文类加载器。java应用运行时初始线程上下文类加载器就是系统类加载器。
//                      线程中运行的代码可以通过这个类加载器加载类和资源
public class ThreadContextClassLoaderTest {
    public static void main(String[] args) {
        // null
        System.out.println(Thread.class.getClassLoader());
        // sun.misc.Launcher$AppClassLoader@18b4aac2
        System.out.println(Thread.currentThread().getContextClassLoader());
    }
}
