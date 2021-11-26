package com.deltaqin.p00algo.code02_binary_search;

/**
 * @author deltaqin
 * @date 2021/8/3 上午10:50
 */
public class B0803Right {
    public static void main(String[] args) {
        int[] arr = new int[]{1, 2, 3, 4, 4, 4, 5, 6, 7};
        int res = search(arr, 4, 0, arr.length);
        System.out.println(res);
    }

    private static int search(int[] arr, int target, int start, int end) {

        int left = start;
        int right = end;


        while (left < right) {
            // 为运算的括号麻了
            int mid = ((right - left) >> 1) + left;
            if (arr[mid] <= target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return arr[left - 1]== target ? left - 1 : -1;
    }
}
