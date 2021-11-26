package com.deltaqin.p08designPattern.d08_proxy.aopmanuel;

/**
 * @author deltaqin
 * @date 2021/3/27 7:00 下午
 */
public class TimeProxy {

    public void before() {
        System.out.println("method start.." + System.currentTimeMillis());
    }

    public void after() {
        System.out.println("method stop.." + System.currentTimeMillis());
    }

}
