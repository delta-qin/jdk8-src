package com.deltaqin.designPattern.d08_proxy.aopauto;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author deltaqin
 * @date 2021/3/27 7:10 下午
 */
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("appauto.xml");
        Tank t = (Tank)context.getBean("tank");
        t.move();
    }
}
