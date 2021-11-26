package com.deltaqin.p04io.file;

import java.io.File;

/**
 * @author deltaqin
 * @date 2021/10/1 上午8:49
 */
public class FileTran2 {
    public static void main(String[] args) {

    }

    public static void process(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File file1 : files) {
                        process(file1);
                    }
                }
            } else {
                System.out.println(" " + file.getName());
            }
        }
    }
}
