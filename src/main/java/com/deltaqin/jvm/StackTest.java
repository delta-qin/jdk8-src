package com.deltaqin.jvm;

/**
 * @author deltaqin
 * @date 2021/4/25 4:17 下午
 */
public class StackTest {
    static int count = 0;


    // JVM 设置  -Xss设置128k很快over, -Xss默认1M会比较多，足够大了
    static void redo() {
        count++;
        redo();
    }

    public static void main(String[] args) {
        try {
            redo();
        } catch (Throwable t) {
            t.printStackTrace();
            System.out.println(count);
        }
    }
}
