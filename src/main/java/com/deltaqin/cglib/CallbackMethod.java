package com.deltaqin.cglib;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CallbackMethod implements MethodInterceptor {

    /**
     * 实现对类的代理
     * @param o 代理对象
     * @param method 目标对象的方法
     * @param objects 目标对象方法的参数
     * @param methodProxy 代理对象中的代理方法对象
     * @return
     * @throws Throwable
     */
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        System.out.println("前置");

        // 执行被增强的方法
        // invokeSuper(代理对象, 参数);
        Object o1 = methodProxy.invokeSuper(o, objects);

        System.out.println("后置通知");
        return o1;
    }
}
