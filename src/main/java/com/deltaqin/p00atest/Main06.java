package com.deltaqin.p00atest;

/**
 * @author deltaqin
 * @date 2021/9/3 上午11:23
 *
 * 输入一个正整数，判断它的十六进制形式是否为回文数。
 *
 * 注意：不允许使用标准库中任何可以直接将整数转换为十进制字符串的函数。
 */
import java.util.*;

public class Main06 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {
            int n1 = in.nextInt();
            String str = process(n1);
            isPlain(str);
        }
    }

    public static String process(int num) {
        if (num == 0) return  "0";
        char[] chars = new char[]{'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        StringBuffer buffer = new StringBuffer();
        int temp;
        while (num != 0) {
            temp = num & 15;
            buffer.insert(0, chars[temp]);
            num >>>= 4;
        }
        return buffer.toString();
    }

    public static void isPlain(String str) {
        str = str.toLowerCase();
        for (int i = 0, j = str.length() - 1; i < j; i++, j--) {
            if (str.charAt(i) != str.charAt(j)) {
                System.out.println("0");
                return;
            }
        }
        System.out.println("1");
    }

}


