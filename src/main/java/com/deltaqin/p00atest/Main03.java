package com.deltaqin.p00atest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author deltaqin
 * @date 2021/8/27 下午7:00
 * 数组，int ， int target，
 * 三个数字和为target
 * 所有的组合，二维数组输出
 */
public class Main03 {
    //
    public static void main(String[] args) {
        Main03 main03 = new Main03();
        List<List<Integer>> lists = main03.threeTarget(new int[]{2, -1, 1, 0, -2}, 0);
        System.out.println(lists);
    }

    public List<List<Integer>> threeTarget(int[] nums, int target) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums.length < 4) return res;
        Arrays.sort(nums);

        for (int i = 0; i < nums.length - 2; i++) {
            int first = nums[i];
            if (first > target) {
                break;
            }
            int left = i + 1;
            int right = nums.length - 1;
            while (left < right) {
                int sum = nums[left] + nums[right] + first;
                if (sum == target) {
                    res.add(Arrays.asList(first, nums[left], nums[right]));
                    while (left < right && nums[left] == nums[left + 1]) {
                        left++;
                    }
                    while (left < right && nums[right] == nums[right - 1]) {
                        right--;
                    }
                    left++;
                    right--;
                } else if (sum < target) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return res;
    }
}
