package com.deltaqin.designPattern.d12_decorator;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author deltaqin
 * @date 2021/3/27 1:38 下午
 */
public class Demo {

    public static void main(String[] args) throws FileNotFoundException,
            IOException {
        FileInputStream fis = null;

        // 不使用装饰器BufferedInputStream
        fis = new FileInputStream("./test.data");
        long t = System.currentTimeMillis();
        int c;
        while ((c = fis.read()) != -1) {
        }
        t = System.currentTimeMillis() - t;
        System.out.println("遍历文件用了如下时间:" + t);
        fis.close();

        // 使用装饰器BufferedInputStream
        FileInputStream fis1 = new FileInputStream("./test.data");
        BufferedInputStream bis = new BufferedInputStream(fis1);
        t = System.currentTimeMillis();

        while ((c = bis.read()) != -1) {}
        fis1.close();
        t = System.currentTimeMillis() - t;
        System.out.println("遍历文件用了如下时间:" + t);
        fis1.close();
        bis.close();
    }
}

