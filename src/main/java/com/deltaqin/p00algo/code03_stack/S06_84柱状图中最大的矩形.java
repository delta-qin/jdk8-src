package com.deltaqin.p00algo.code03_stack;

import java.util.Stack;

/**
 * @author deltaqin
 * @date 2021/5/25 12:19 下午
 */
public class S06_84柱状图中最大的矩形 {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int area = solution.largestRectangleArea(new int[]{2, 4});
        System.out.println(area);
    }

}

class Solution {
    public int largestRectangleArea(int[] heights) {

        int[] leftMinArr = leftMin(heights);
        int[] rightMinArr = rightMin(heights);
        int left = 0;
        int right = 0;
        int maxArea = 0;
        for(int i = 0; i < heights.length; i++) {
            left = leftMinArr[i];
            right = rightMinArr[i];
            // 处理边界，没有比自己小的，就使用最边上的索引
            if(left == -1 ) left = -1;
            if(right == -1) right = heights.length;

            int len = right - left - 1;
            maxArea = Math.max(maxArea, len * heights[i]);
        }
        return maxArea;
    }

    public int[] leftMin(int[] h) {

        if(h==null || h.length == 0) return null;
        if(h.length == 1) return new int[]{-1};

        int[] res = new int[h.length];

        Stack<Integer> stack = new Stack<>();
        // 这里一开始写了0.。。。说好的从右往左插入
        stack.push(h.length -1);
        for(int i = h.length -2; i >= 0; i--) {
            while(!stack.isEmpty() && h[stack.peek()] > h[i]) {
                res[stack.pop()] = i;
            }
            stack.push(i);
        }
        while(!stack.isEmpty()) {
            res[stack.pop()] = -1;
        }
        return res;
    }

    public int[] rightMin(int[] h) {

        if(h==null || h.length == 0) return null;
        if(h.length == 1) return new int[]{-1};

        int[] res = new int[h.length];

        Stack<Integer> stack = new Stack<>();
        stack.push(0);
        for(int i = 0; i < h.length; i++) {
            while(!stack.isEmpty() && h[stack.peek()] > h[i]) {
                res[stack.pop()] = i;
            }
            stack.push(i);
        }
        while(!stack.isEmpty()) {
            res[stack.pop()] = -1;
        }
        return res;
    }


}
