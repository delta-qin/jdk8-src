package com.deltaqin.atest;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @author deltaqin
 * @date 2021/9/4 下午5:42
 */
public class Main09 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String tmp = sc.nextLine();
            String[] nums = tmp.split(" ");
            int n = Integer.parseInt(nums[0]);
            int m = Integer.parseInt(nums[1]);

            String str = sc.nextLine();
            String[] nums2 = str.split(" ");
            int[] nums3 = new int[nums2.length];
            for (int i = 0; i < nums3.length; i++) {
                nums3[i] = Integer.parseInt(nums2[i]);
            }
            // Arrays.sort(nums3);

            int max1 = 0;
            int index = 0;
            for (int i = 0; i <= nums3.length - m; i++) {
                int res = 0;
                int max = findMax(nums3, i, i + m - 1);
                int min = findMin(nums3, i, i + m - 1);
                int ttt = 0;
                for (int j = i; j < m + i; j++) {
                    if (nums3[j] == min && nums3[j] == max) {
                        ttt++;
                    }
                    if (nums3[j] != min && nums3[j] != max) {
                        res += nums3[j];
                    }
                }
                res = res / (m - 2);
                if (max1 < res) {
                    index = i;
                }
            }
            System.out.println(index);
        }
    }

    public static int findMax(int[] num, int start, int end) {
        int max = num[start++];
        ;
        while (start <= end) {
            if (max < num[start]) {
                max = num[start];
            }
            start++;
        }
        return max;
    }

    public static int findMin(int[] num, int start, int end) {
        int min = num[start++];
        while (start <= end) {
            if (min > num[start]) {
                min = num[start];
            }
            start++;
        }
        return min;
    }
}
