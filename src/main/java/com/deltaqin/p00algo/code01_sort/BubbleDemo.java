package com.deltaqin.p00algo.code01_sort;

import java.util.Arrays;

public class BubbleDemo{
    public static void bubble(int[] nums) {
        int length = nums.length;
        boolean flag ;
        for(int i = length-1; i>0; i--){
            flag = true;
            for(int j = 0; j<i; j++) {
                if(nums[j] > nums[j+1]){
                    int temp = nums[j];
                    nums[j] = nums[j+1];
                    nums[j+1] = temp;
                    flag = false;
                }
            }
            if(flag){
                break;
            }
        }
        print(nums);
    }

    public static void print(int[] nums) {
//        for (int num : nums) {
//            System.out.println(num);
//        }
        Arrays.stream(nums).forEach(System.out::println);
    }

    public static void main(String[] args) {
        int[] nums = {1,4,5,6,3,2};
        bubble(nums);
    }
}
