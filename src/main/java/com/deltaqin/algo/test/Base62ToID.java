package com.deltaqin.algo.test;

/**
 * @author deltaqin
 * @date 2021/8/19 上午11:46
 */
public class Base62ToID {
    public static void main(String[] args) {
        String s = toBase64(15928939179L);
        System.out.println(s);
        long i = toNumber(s);
        System.out.println(i);
    }

    public static String chars = "0123456789abcdefghijklmnopqrstyvwxyzABCDEFGHJZKLMNOPQRSTUVWXYZ";

    public static String toBase64(long number) {
        StringBuilder sb = new StringBuilder();
        while (number != 0) {
            int i = (int)(number % 62);
            sb.insert(0, chars.charAt(i));
            number /= 62;
        }
        while (sb.length() < 7) {
            sb.insert(0, "0");
        }
        return sb.toString();
    }

    public static long toNumber(String base64) {
        long res = 0;
        for (int i = 0; i < base64.length(); i++) {
            char c = base64.charAt(i);
            int index = chars.indexOf(c);
            res = res * 62 + index;
        }
        return res;
    }
}
