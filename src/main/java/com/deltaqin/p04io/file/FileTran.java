package com.deltaqin.p04io.file;

import java.io.File;

/**
 * @author deltaqin
 * @date 2021/8/12 上午11:31
 *
 * 如果是文件直接输出
 * 如果是文件夹就先输出文件夹名字，之后遍历文件夹递归调用打印文件的函数
 */
public class FileTran {
    public static void main(String[] args) {
        File file = new File("./");
        walk(file);
    }

    private static void walk(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                System.out.println(file.getName());
                File[] files = file.listFiles();
                if (files != null) {
                    for (File file1 : files) {
                        walk(file1);
                    }
                }
            } else {
                System.out.println(" " + file.getName());
            }
        }
    }
}
