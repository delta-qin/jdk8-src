package com.deltaqin.designPattern.d05_templateMethod;

/**
 * @author deltaqin
 * @date 2021/3/27 10:14 上午
 */

// 在接口里面定义方法模板，不管具体实现
//     使用的时候面向接口编程，这就是模板方法
//     典型应用：controller调用service接口，不管具体实现，controller是对service抽象方法的组装
public class Demo {
    public static void main(String[] args) {
        F f = new C1();
        f.m();
    }
}

abstract class F {
    public void m() {
        op1();
        op2();
    }

    abstract void op1();
    abstract void op2();
}

class C1 extends F {

    @Override
    void op1() {
        System.out.println("op1");
    }

    @Override
    void op2() {
        System.out.println("op2");
    }
}
