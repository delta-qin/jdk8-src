package com.deltaqin.p00algo.code00_test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author deltaqin
 * @date 2021/5/31 8:58 上午
 */
public class Solution1 {
    public static void main(String[] args) {
        List<Integer> list = new Solution1().countSmaller(new int[]{5, 2, 6, 1});
        System.out.println(list);
    }

    public int[] res = null;
    public List<Integer> countSmaller(int[] nums) {

        res = new int[nums.length];
        merge(nums, 0, nums.length, new int[nums.length]);
        System.out.println(Arrays.toString(res));
        return Arrays.stream(res).boxed().collect(Collectors.toList());
    }

    public void merge(int[] nums, int b, int e, int[] temp) {
        if(b >= e || b+1 >= e) {
            return;
        }


        int m = b + ((e - b) >> 1);

        merge(nums, b, m, temp);
        merge(nums, m, e, temp);

        int i = b;
        int j = m;
        int to = b;

        while(i < m || j < e) {
            if(j >= e || ( i < m && nums[i] < nums[j])) {
                temp[to++] = nums[i++];
                res[i] += j - m;
            } else {
                temp[to++] = nums[j++];
            }
        }

        for(int z = b;  z < e; z++) {
            nums[z] = temp[z];
        }
    }
}
