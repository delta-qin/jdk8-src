package com.deltaqin.atest;

import java.util.Scanner;

/**
 * @author deltaqin
 * @date 2021/9/9 下午6:58
 */
public class Main10 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();

        for (int i = 0; i < t; i++) {
            int n = sc.nextInt();
            int k = sc.nextInt();
            process(n, k);
        }
        // System.out.println(ans);
    }

    public static void process(int n, int k) {
        int res = 0;
        int temp = n - k;
        for (int y = n; y > k; y--) {
            res += 1 + temp / y;
        }
        System.out.println(res);
    }

}
