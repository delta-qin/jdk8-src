package com.deltaqin.atest;

import java.util.Arrays;

/**
 * @author deltaqin
 * @date 2021/8/26 上午7:21
 */
public class Main02 {
    public static Main02 main02;

    public static void main(String[] args) {
        main02 = new Main02();
        int[] arr = new int[]{1, 2, 5, 4};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void sort(int[] nums) {
        int len = nums.length - 1;
        while (len > 0) {
            for (int i = 0; i < len; i++) {
                if (nums[i] > nums[i + 1]) {
                    swap(nums, i , i + 1);
                }
            }
            len--;
        }
    }

    private static void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }
}
