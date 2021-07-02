package com.deltaqin.algo.code03_stack;

import java.util.Stack;

/**
 * @author deltaqin
 * @date 2021/5/25 9:05 上午
 */
public class S03_大鱼吃小鱼 {
    public static void main(String[] args) {
        int[] size = new int[]{4, 3, 2, 1, 5};
        int[] Dir = new int[]{0, 1, 0, 0, 0};
        System.out.println(process(size, Dir));

    }

    public static int process(int[] size, int[] direction) {
        if(size.length < 2) return size.length;

        int left = 0;
        int right = 1;

        // 栈里面必须放索引，因为还需要知道方向
        Stack<Integer> stack = new Stack<>();

        stack.push(0);
        boolean hasEated = false;
        int index = 1;
        while (index < size.length) {
            while (!stack.isEmpty() && direction[index] == left
                    && direction[stack.peek()] == right ) {
                // 吃掉栈里的
                if (size[index] > size[stack.peek()]) {
                    stack.pop();
                } else if (size[index] < size[stack.peek()]){
                    hasEated = true;
                    break;
                } else {
                    // 相等说明入栈就行了
                    break;
                }
            }
            if (!hasEated) {
                stack.push(index);
            }
            index++;
        }
        return stack.size();
    }
}
