package com.deltaqin.p08designPattern.d08_proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author deltaqin
 * @date 2021/3/27 11:55 上午
 */
public class JDKProxyTank implements Movable{
    @Override
    public void move() {
        System.out.println("Tank moving ...");
    }

    public static void main(String[] args) {
        JDKProxyTank tank = new JDKProxyTank();

        //reflection 通过二进制字节码分析类的属性和方法
        // 生成的代理类 注意强转
        Movable m = (Movable) Proxy.newProxyInstance(
                JDKProxyTank.class.getClassLoader(),// 类加载器写被代理类型的，为什么？也可以是父类加载器加载的，但不能是平行的
                new Class[]{Movable.class}, // 需要实现什么接口（是数组） JDKProxyTank.class.getInterfaces() == [Movable.class]
                new LogHander(tank) // 代理操作，动态指定
        );
        System.out.println(JDKProxyTank.class.getInterfaces());
        m.move();
    }
}

// 代理方法中自己的处理
class LogHander implements InvocationHandler {

    JDKProxyTank tank;

    // 之所以要传这个是因为要使用这个对象，使用匿名内部类就不需要这样了
    public LogHander(JDKProxyTank tank) {
        this.tank = tank;
    }

    //getClass.getMethods[]
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("method " + method.getName() + " start..");
        Object o = method.invoke(tank, args);
        System.out.println("method " + method.getName() + " end!");
        return o;
    }
}



