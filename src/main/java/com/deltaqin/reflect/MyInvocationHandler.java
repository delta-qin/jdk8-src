package com.deltaqin.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MyInvocationHandler implements InvocationHandler {
    // 指向目标对象
    Object obj;


    public MyInvocationHandler(Object obj) {
        super();
        this.obj = obj;
    }

    /**
     * 定义要追加的功能
     * @param proxy 被代理的对象
     * @param method 目标对象中的方法对象，jdk传递过来的
     * @param args 方法对象的参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("相当于前置通知");
        Object invoke = method.invoke(obj, args);
        System.out.println("相当于后置通知");
        return invoke;
    }
}
