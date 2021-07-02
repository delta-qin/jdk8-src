package com.deltaqin.algo.code03_stack;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author deltaqin
 * @date 2021/5/25 8:01 上午
 */
public class S02_判断字符串括号是否合法 {
    public static void main(String[] args) {

        //System.out.println(isValid1("(()))"));
        //System.out.println(isValid2("(())"));
        System.out.println(isValid3("[{(}())]"));

    }

    public static boolean isValid1(String s) {
        if (s == null || s.length() == 0) return true;

        if (s.length() % 2 == 1) return false;

        Stack<Character> stack = new Stack<>();
        char[] strs = s.toCharArray();

        for (char c : strs) {
            if (c == '(')
                stack.push(c);
            else {
                if (stack.isEmpty()) {
                    return false;
                }
                stack.pop();
            }
        }
        return stack.isEmpty();
    }

    public static boolean isValid2(String s) {
        if (s == null || s.length() == 0) {
            return true;
        }
        if (s.length() % 2 == 1) {
            return false;
        }

        int left = 0;
        char[] str = s.toCharArray();
        for (char c : str) {
            if (c == '(') {
                left++;
            } else {
                if (left <= 0) {
                    return false;
                }
                left--;
            }
        }
        return left == 0;
    }

    public static  boolean isValid3(String s) {
        if( s == null || s.length() == 0 )
            return true;
        if (s.length() % 2  == 1)
            return false;

        Map<Character,Character> map = new HashMap<>();
        map.put(')', '(');
        map.put('}', '{');
        map.put(']', '[');
        Stack<Character> stack = new Stack<>();
        char[] chars = s.toCharArray();
        for (char c : chars) {
            if (c == '(' || c == '[' || c == '{')
                stack.push(c);
            else {
                if (stack.isEmpty()) {
                    return false;
                }
                if (stack.peek() != map.get(c)) {
                    return false;
                }
                stack.pop();
            }
        }
        return stack.isEmpty();
    }
}
