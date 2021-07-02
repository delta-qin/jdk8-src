package com.deltaqin.designPattern.d08_proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Random;

/**
 * @author deltaqin
 * @date 2021/3/27 12:11 下午
 */

// Cglib 其实就是生成了被代理类的子类

public class CglibProxy {
    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        // 设置 enhancer 的父类是 Tank
        enhancer.setSuperclass(Tank.class);
        //设置回调，TimeMethodInterceptor 相当于 Proxy 的Handler
        enhancer.setCallback(new TimeMethodInterceptor());
        // 创建代理类，也就是 被代理类的子类
        Tank tank = (Tank)enhancer.create();
        tank.move();
    }
}

class TimeMethodInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        // Tank 的子类，如果Tank是final的话，就是生成不了的，ASM就可以，因为操作的是二进制吗
        //ASM直接修改二进制，
        // cglib底层是用的也是ASM
        System.out.println(o.getClass().getSuperclass().getName());
        System.out.println("before");
        Object result = null;
        result = methodProxy.invokeSuper(o, objects);
        System.out.println("after");
        return result;
    }
}

class Tank {
    public void move() {
        System.out.println("Tank moving...");
        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


