package com.deltaqin.algo.code01_sort;

import java.util.Arrays;

/**
 * @author deltaqin
 * @date 2021/7/21 下午11:55
 */
public class S721MergeSort {
    public static void main(String[] args) {
        int[] t = new int[]{1, 3, 2};
        sort(t);
        String string = Arrays.toString(t);
        System.out.println(string);
    }

    public static void sort(int[] arr) {
        if (arr == null || arr.length == 0) return;
        int[] t = new int[arr.length];
        process(arr, 0, arr.length, t);
    }

    public static void process(int[] arr, int b, int e, int[] t) {
        if (b >= e || b + 1 >= e) return ;
        // 位运算有毒
        int m = ((e - b) >> 1) + b ;
        process(arr, b, m, t);
        process(arr, m, e, t);
        int l = b;
        int r = m;
        int i = b;
        // 写的是左闭右开，结果这里全错了
        while (l < m || r < e) {
            // 右侧的数组左闭右开条件错了
            // 没有考虑稳定排序
            if (r >= e || (l < m && arr[l] <= arr[r])) {
                t[i++] = arr[l++];
            } else {
                t[i++] = arr[r++];
            }
        }
        for (int x = b; x < e; x++) {
            arr[x] = t[x];
        }
    }
}
