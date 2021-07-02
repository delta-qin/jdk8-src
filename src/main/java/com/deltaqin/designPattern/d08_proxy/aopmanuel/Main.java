package com.deltaqin.designPattern.d08_proxy.aopmanuel;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author deltaqin
 * @date 2021/3/27 7:15 下午
 */
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("app.xml");
        Tank t = (Tank)context.getBean("tank");
        t.move();
    }
}
