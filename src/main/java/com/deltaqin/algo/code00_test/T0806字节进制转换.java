package com.deltaqin.algo.code00_test;

/**
 * @author deltaqin
 * @date 2021/8/6 下午10:01
 */
public class T0806字节进制转换 {
    public static void main(String[] args) {
        String s = toSeven1(99);
        System.out.println(s);
    }
    public static String toSeven(int number){
        String result="";
        String remain = "";
        while (number>=7){

            remain = String.valueOf(number%7);

            number = number/7;

            result = remain + result;
        }
        return String.valueOf(number)+result;
    }

    public static String toSeven1(int number) {
        String remain = "";
        String res = "";
        while (number != 0) {
            remain = String.valueOf(number % 7);
            number /= 7;
            res = remain + res;
        }
        return  res;
    }
}
