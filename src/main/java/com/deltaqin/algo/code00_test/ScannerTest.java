package com.deltaqin.algo.code00_test;

import java.util.HashMap;
import java.util.Scanner;

/**
 * @author deltaqin
 * @date 2021/8/2 下午9:19
 */
public class ScannerTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int[][] orders = new int[N][2];
        for (int i = 0; i < N; i++) {
            orders[i][0] = sc.nextInt();
            orders[i][1] = sc.nextInt();
        }

        solution(orders);
    }

    private static void solution(int[][] orders) {
        if (orders.length == 0) {
            System.out.println("None");
            return;
        }
        int res = dp(orders, 0);
        System.out.println(res);
    }

    static HashMap<String, Integer> memo = new HashMap<>();

    private static int dp(int[][] orders, int i) {
        return 0;
    }
}
