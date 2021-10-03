package com.deltaqin.algo.test;

/**
 * @author deltaqin
 * @date 2021/8/22 上午9:03
 * 当前有多少选择
 *
 */
import java.util.*;


import java.util.*;


public class Solution02 {
    /**
     * 代码中的类名、方法名、参数名已经指定，请勿修改，直接返回方法规定的值即可
     *
     * 输入s，返回使得s中不存在两个相同频次的字符所需的最小删除次数
     * @param s string字符串 输入字符串
     * @return int整型
     */
    public int minDeletions (String s) {
        // write code here
        Map<Character, Integer> map = new HashMap<>();
        // 统计每一个的个数
        char[] strs =  s.toCharArray();
        for (char c : strs) {
            int num = map.getOrDefault(c, 0);
            map.put(c, num + 1);
        }

        Set<Integer> set = new HashSet<>();
        int res = 0;
        for (Map.Entry<Character, Integer> entry:
                map.entrySet()) {
            Integer value = entry.getValue();
            while (value != 0 && set.contains(value)) {
                res++;
                value--;
            }
            if (value != 0) {
                set.add(value);
            }
        }

        return res;
    }
}
