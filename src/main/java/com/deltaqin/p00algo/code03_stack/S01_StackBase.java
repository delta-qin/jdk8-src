package com.deltaqin.p00algo.code03_stack;

import java.util.Stack;

/**
 * @author deltaqin
 * @date 2021/5/25 7:59 上午
 */
public class S01_StackBase {
    public static void main(String[] args) {
        Stack<Character> stack = new Stack<>();
        stack.push('a');
        stack.push('b');
        System.out.println(stack.peek());
        System.out.println(stack.pop());
        System.out.println(stack.size());
    }
}
