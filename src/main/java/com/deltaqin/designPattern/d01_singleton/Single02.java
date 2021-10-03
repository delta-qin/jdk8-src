package com.deltaqin.designPattern.d01_singleton;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author deltaqin
 * @date 2021/3/26 1:42 下午
 */
public class Single02 implements Serializable {

    // volatile 避免重排序
    private static volatile Single02 instance;

    private Single02() {
        if (instance != null) {
            throw new RuntimeException("不可以重复创建，可能是反射攻击");
        }
    }

    // 居然没有加static，我说怎么不能直接调用
    public static Single02 getInstance1() {
        if (instance == null)  {

            // 测试后睡眠保证在线程睡眠的时候其他线程可以进来获得锁并且初始化暴露错误
            try{
                Thread.sleep(1000);
            }catch (Exception e) {
                e.printStackTrace();
            }

            // instance = new Single02();

            // 使用类锁，保证唯一
            synchronized (Single02.class ) {
                if (instance == null) {
                    instance = new Single02();
                }
            }
        }
        return instance;
    }

    Object readesolve() throws ObjectStreamException {
        return Single02.instance;
    }


    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        for (int i = 0; i < 100; i++) {
            new Thread(()-> {
                System.out.println(Single02.getInstance1().hashCode());
            }).start();
        }

        // 使用双重检查锁不能防止反射攻击
        Constructor<Single02> declaredConstructor = Single02.class.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);
        Single02 single02 = declaredConstructor.newInstance();
        System.out.println(single02.hashCode() == Single02.getInstance1().hashCode());
    }
}
