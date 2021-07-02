package com.deltaqin.threadlocal;

public class ThreadLocalDemo {
    public static void main(String[] args) {
        // 创建ThreadLocal对象
        ThreadLocal<String> tl = new ThreadLocal<>();

        // 获取当前线程对象绑定的值
        String s = tl.get();
        System.out.println(s); // null, 因为当前线程对象还没有设置value值呢,所以为null

        // 给当前main线程对象(作为key),绑定值为"hello guizy"
        tl.set("hello guizy");
        s = tl.get();
        System.out.println(s);  // hello guizy

        // 创建SubThread类的对象\
        //  两个的值互不影响
        SubThread st = new SubThread(tl);

        // 开启线程
        st.start();
    }
}

