package com.deltaqin.p08designPattern.d08_proxy;

// 要求代理类和被代理实现一样的接口
// 构造的时候都使用接口作为形参，传递实参的时候使用接口的具体实现类
// 代理类中调用的时候都使用面向接口调用，运行时候根据函数的参数替换为具体的实现类
public class ManuelProxyTank implements Movable {

    @Override
    public void move() {
        System.out.println("Tank moving");
    }

    public static void main(String[] args) {

        ManuelProxyTank t = new ManuelProxyTank();
        TankTimeProxy ttp = new TankTimeProxy(t);
        TankLogProxy tlp = new TankLogProxy(ttp);
        tlp.move();
    }
}

class TankTimeProxy implements Movable {
    Movable m;

    public TankTimeProxy(Movable m) {
        this.m = m;
    }

    @Override
    public void move() {
        long start = System.currentTimeMillis();
        m.move();
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}

class TankLogProxy implements Movable {
    Movable m;

    public TankLogProxy(Movable m) {
        this.m = m;
    }

    @Override
    public void move() {
        System.out.println("start moving...");
        m.move();
        long end = System.currentTimeMillis();
        System.out.println("stopped!");
    }
}

