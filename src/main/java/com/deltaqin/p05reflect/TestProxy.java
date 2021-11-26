package com.deltaqin.p05reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class TestProxy {
    public static void main(String[] args) {
        // 目标对象
        UserDao userDao = new UserDaoImpl();

        // 实现对目标对象增强的对象
        InvocationHandler invocationHandler = new MyInvocationHandler(userDao);

        /**
         * 返回的是代理对象
         *  类加载器
         *  类的接口
         *  定义增强的实现InvocationHandler接口的对象
         */
        Object o = Proxy.newProxyInstance(userDao.getClass().getClassLoader(),
                userDao.getClass().getInterfaces(),
                invocationHandler);

        // 代理对象强转
        UserDao userDao1 = (UserDao)o;
        userDao1.add();

        userDao1.delete();
    }
}
