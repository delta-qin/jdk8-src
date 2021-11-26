package com.deltaqin.p00algo.code03_stack;

import java.util.Arrays;
import java.util.Stack;

/**
 * @author deltaqin
 * @date 2021/5/25 10:42 上午
 */
public class S05_字典序最小的k个数的子序列 {
    public static void main(String[] args) {

        System.out.println(Arrays.toString(process(new int[]{9,2,4,5,1,2,6,3,100,4}, 3)));
        System.out.println(Arrays.toString(process(new int[]{9,2,4,5,1,2,6,3,100,4}, 2)));
        System.out.println(Arrays.toString(process(new int[]{9,2,4,5,1,2,6,3,100,4}, 1)));
        System.out.println(Arrays.toString(process(new int[]{1,1,1,1,1}, 1)));

        assert(Arrays.equals(new int[]{1,2,3}, process(new int[]{9,2,4,5,1,2,6,3,100,4}, 3)));
        assert(Arrays.equals(new int[]{1,2}, process(new int[]{9,2,4,5,1,2,6,3,100,4}, 2)));
        assert(Arrays.equals(new int[]{1}, process(new int[]{9,2,4,5,1,2,6,3,100,4}, 1)));
        assert(Arrays.equals(new int[]{1}, process(new int[]{1,1,1,1,1}, 1)));

    }

    public static int[] process(int[] arr, int k) {
        if (arr.length <= k) {
            return arr;
        }

        Stack<Integer> stack = new Stack<>();
        stack.push(0);
        for (int i = 0; i < arr.length; i++) {
            while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) {
                //if (stack.size() + 1 > k) {
                // 应该是栈里面的个数加上右边还剩的个数
                if (stack.size() + (arr.length - i) > k) {
                    stack.pop();
                } else break;
            }
            // 最后是一定push进去的，所以要放到while外面
            stack.push(i);
        }
        while (stack.size() != k) {
            stack.pop();
        }

        int[] res = new int[k];
        int index = k-1;
        while (!stack.isEmpty()) {
            res[index] = arr[stack.pop()];
            index--;
        }

        return res;
    }
}
