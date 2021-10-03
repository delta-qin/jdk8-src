package com.deltaqin.algo;

import java.util.*;

// 本质是合并两个有序数组
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            int n = sc.nextInt();
            int len = sc.nextInt();
            int[] input = new int[len];
            Set<Integer> set = new HashSet<>();
            for (int i = 0; i < len; i++) {
                input[i] = sc.nextInt();
                set.add(input[i]);
            }

            int[] tmp = new int[n - len];
            int index = 0;
            for (int j = 1; j <= n; j++) {
                if (!set.contains(j)) {
                    tmp[index++] = j;
                }
            }

            merge(input, len, tmp, n - len, n);
        }
    }

    private static void merge(int[] input, int len1, int[] tmp, int len2, int len) {
        int[] res = new int[len];
        int left = 0;
        int right = 0;
        int index = 0;
        while (left < len1 || right < len2) {
            if (right >= len2 || left < len1 && input[left] < tmp[right]) {
                res[index++] = input[left++];
            } else {
                res[index++] = tmp[right++];
            }
        }

        for (int num: res) {
            System.out.print(num + " ");
        }
        System.out.println("");
    }
}
