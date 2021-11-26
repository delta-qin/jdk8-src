package com.deltaqin.p00atest;

/**
 * @author deltaqin
 * @date 2021/9/17 下午6:57
 */
import java.util.Scanner;

public class Main11 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (sc.hasNext()) {
            int n = sc.nextInt();
            int k = sc.nextInt();
            int[] nums = new int[n];
            for (int i = 0; i < n; i++) {
                nums[i] = sc.nextInt();
            }
            process(n, k, nums);
        }

        // System.out.println(ans);
    }

    public static void process(int n, int k, int[] nums) {
        int res = n;
        // i 之前有多少个比nums[i] 小的元素
        int[][] dp = new int[n + 1][k + 1];
        for (int i = 2; i < n; i++) {
            for (int j = 1; j <= k; j++) {
                if (nums[i - 1] > nums[i - 2]) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        for (int i = 2; i < k; i++) {
            res += dp[n][i];
        }
        System.out.println(res);
    }
}
