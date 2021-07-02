package com.deltaqin.algo.code01_sort;

import java.util.Arrays;

public class InsertDemo {
    public static void insertSort(int[] nums){
        int len = nums.length;
        // 注意其实位置是1不是0
        for(int i=1; i<len-1; i++) {
            int temp = nums[i];
            // j=0需要判断，因为可能插在开头
            int j;
            for(j=i-1; j>=0; j--){
                if(temp < nums[j]){
                    nums[j+1] = nums[j];
                } else {
                    // 这里忘记写了，不小于的时候就应该停止了，
                    // 不应该继续往下多了
                    break;
                }
            }
            // 注意要+1，在for循环里面-1了
            nums[j+1] = temp;
        }
        print(nums);
    }

    public static void print(int[] nums) {
        System.out.println(Arrays.toString(nums));
    }

    public static void main(String[] args) {
        int[] nums = new int[]{1,4,3,2,6,5};
        insertSort(nums);
    }
}
