package com.deltaqin.algo.code00_test;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @author deltaqin
 * @date 2021/8/5 下午2:53
 */
public class ScanerTest2 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String next = sc.next();
        int i = lengthOfLongestSubstring(next);
        System.out.println(i);
    }

    // 最长不重复子串
    public static int lengthOfLongestSubstring(String s) {

        int left = -1;
        int[] pos = new int[256];
        Arrays.fill(pos, -1);
        int max = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (pos[c] > left) {
                left = pos[c];
            }
            pos[c] = i;
            max = Math.max(max, i - left);
        }
        return max;
    }
}
