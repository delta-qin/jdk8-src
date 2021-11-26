package com.deltaqin.p00atest;

import java.util.Scanner;

/**
 * @author deltaqin
 * @date 2021/9/4 下午5:12
 */
public class Main08 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String tmp = sc.nextLine();
            int n = Integer.parseInt(tmp);
            int res = 0;
            int subLen = 2;
            while (subLen <= n) {
                res += process(n, subLen);
                subLen++;
            }

            System.out.println(res);
        }
    }

    // i个一样的连在一起的时候，对于长度 n 有多少可能
    public static int process(int n, int i) {
        if (n == 1) {
            return 2;
        }
        if (n < 1) {
            return 1;
        }
        return process(n - i, i) * 2;
    }
}
