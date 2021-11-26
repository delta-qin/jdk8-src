package com.deltaqin.p08designPattern.d01_singleton;

/**
 *
 * 不仅可以解决线程同步，还可以防止反序列化。
 *
 * 反射可以通过class文件加载到内存，通过反序列化构建对象
 * 枚举单例没有构造方法不会被反序列化，反编译之后是 abstract class
 *
 * @author deltaqin
 * @date 2021/3/26 2:18 下午
 */
public enum Single04 {
    INSTANCE;

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(()-> {
                System.out.println(Single04.INSTANCE.hashCode());
            }).start();
        }
    }
}
