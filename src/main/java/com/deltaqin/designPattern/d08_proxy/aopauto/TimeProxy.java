package com.deltaqin.designPattern.d08_proxy.aopauto;


import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;


/**
 * @author deltaqin
 * @date 2021/3/27 7:00 下午
 */
public class TimeProxy {

    @Before("execution (void com.deltaqin.designPattern.d08_proxy.aopauto.Tank.move())")
    public void before() {
        System.out.println("method start.." + System.currentTimeMillis());
    }

    @After("execution (void com.deltaqin.designPattern.d08_proxy.aopauto.Tank.move())")
    public void after() {
        System.out.println("method stop.." + System.currentTimeMillis());
    }

}
