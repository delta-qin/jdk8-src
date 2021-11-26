package com.deltaqin.p00algo.code03_stack;

import java.util.Arrays;
import java.util.Stack;

/**
 * @author deltaqin
 * @date 2021/5/25 9:45 上午
 */
public class S04_找出数组中右边比第一个我小的元素下标 {
    public static void main(String[] args) {
        System.out.println(Arrays.toString(process(new int[]{5,4})));
        System.out.println(Arrays.toString(process(new int[]{1, 2, 4, 9, 4, 0, 5})));
        //assert(Arrays.equals(new int[]{1,-1}, process(new int[]{5,4})));
        //assert(Arrays.equals(new int[]{5, 5, 5, 4, 5, -1, -1}, process(new int[]{1, 2, 4, 9, 4, 0, 5})));
    }

    public static int[] process(int[] arr) {
        //if (arr == null || arr.length == 0) return null;
        //if (arr.length == 1) return new int[]{-1};
        int[] res = new int[arr.length];
        // 除了括号那种题一般使用的时候栈都是记录下标的
        Stack<Integer> stack = new Stack<>();
        stack.push(0);
        for (int i=1; i < arr.length; i++) {
            while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) {
                res[stack.pop()] = i;
            }
            stack.push(i);
        }
        while (!stack.isEmpty()) {
            res[stack.pop()] = -1;
        }
        return res;
    }
}
