package com.deltaqin.p00algo.code01_sort;

import java.util.Arrays;

/**
 * @author deltaqin
 * @date 2021/7/21 下午11:18
 */
public class S721QuickSort {
    public static void main(String[] args) {

        int[] m = new int[]{1, 3, 2,8,4};
        qSort(m);
        System.out.println(Arrays.toString(m));
    }

    public static void swap(int[] nums, int i, int j) {
        int t = nums[j];
        nums[j] = nums[i];
        nums[i] = t;
    }

    public static void qSort(int[] arr) {
        if (arr == null || arr.length == 0) return;
        process(arr, 0, arr.length);
    }

    public static void process(int[] arr, int b, int e) {
        if (b >= e || b + 1 >= e) {
            return;
        }

        int m = b + ((e - b) >> 1);
        int x = arr[m];
        int l = b;
        int t = b;
        //
        int r = e-1;
        while (t <= r) {
            if (arr[t] < x) {
                swap(arr, l++, t++);
            } else if (arr[t] == x) {
                t++;
            } else {
                swap(arr, t, r--);
            }
        }
        process(arr, b, l);
        process(arr, t, e);
    }
}
