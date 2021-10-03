package com.deltaqin.algo.test;

import java.util.Arrays;

/**
 * @author deltaqin
 * @date 2021/8/22 下午3:56
 */
public class Solution3 {
    public static void main(String[] args) {
        convertMagicalString("azbA5#1@c");
    }
    public static long convertMagicalString (String magicalString) {
        if (magicalString.equals("") || magicalString.length() == 0) {
            return 0;
        }
        // write code here
        char[] chars = magicalString.toCharArray();
        char[] arr = new char[chars.length];
        int index = 0;
        for (char c:
                chars) {
            //if (Character.isLetterOrDigit(c)) {
            if (c > 'a' && c < 'z' || c > 'A' && c < 'Z' || (c > '0' && c < '9')) {
                arr[index++] = c;
            }
        }
        char[] chars1 = Arrays.copyOfRange(arr, 0, index);
        Arrays.sort(chars1);
        index = 0;
        for (Character c : chars1) {
            if (Character.isDigit(c)) {
                index++;
            }
        }
        char[] res1 = Arrays.copyOfRange(chars1, index, chars1.length);
        char[] res2 = Arrays.copyOfRange(chars1, 0, index);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < res1.length; i++) {
            int left = i;
            int right = i + 1;
            while (right < res1.length && res1[left] + 1 == res1[right]) {
                right++;
                left++;
            }
            char c = res1[left];
            sb.append(Character.toUpperCase(c) - 'A'  + 1);
            i = left;
        }
        sb.append(res2);
        String string = sb.toString();
        if (string.equals("")) {
            return 0;
        }
        return Long.parseLong(string);
    }
}
