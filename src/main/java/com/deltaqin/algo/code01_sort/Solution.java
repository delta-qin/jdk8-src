package com.deltaqin.algo.code01_sort;

import java.util.Arrays;

public class Solution {

    public int[] sortArray(int[] nums){
        int len = nums.length;
        for (int i = 0; i < len - 1; i++){
            int tempMinIndex = i;
            for (int j = i+1; j < len; j++){
                if (nums[tempMinIndex] > nums[j]){
                    tempMinIndex = j;
                }
            }
            swap(i,tempMinIndex,nums);
        }

        return nums;
    }

    public void swap(int i, int j, int[] nums){
        int temp;
        temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] nums = {5, 2,4, 3, 1};
        int[] res = solution.sortArray(nums);
        System.out.println(Arrays.toString(res));
    }
}
