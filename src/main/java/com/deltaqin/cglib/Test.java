package com.deltaqin.cglib;

import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;

public class Test {
    public static void main(String[] args) {
        // 空的字节码对象
        Enhancer en = new Enhancer();
        // 设定字节码对象的父亲。目标对象的类
        en.setSuperclass(Student.class);

        Callback callback = new CallbackMethod();
        en.setCallback(callback);

        // 得到代理对象（代理类创建代理对象）
        Student student = (Student) en.create();
        student.study();
    }
    // 对字节码代理，
}
