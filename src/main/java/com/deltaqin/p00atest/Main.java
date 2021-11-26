package com.deltaqin.p00atest;

/**
 * @author deltaqin
 * @date 2021/9/1 下午6:38
 * 通配符匹配
 * 详细描述
 * 判断一个包含通配符的字符串是否能完全匹配另一个字符串：
 *
 * - ’?’表示匹配任意一个字符一次，如a?c匹配abc；
 *
 * - ’#’表示匹配任意一个字符零次或一次，如a#c匹配ac和abc；
 *
 * - ’*’表示匹配任意字符零次或者任意多次，如a*c匹配ac、abc和abbbbc；
 *
 * - 其他字符匹配原字符
 *
 *
 *
 * 注意：不允许使用任何正则表达式库
 */
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String n1 = in.nextLine();
            String n2 = in.nextLine();
            process(n2, n1);
        }
    }

    public static void process(String s, String p) {
        if (s.length() != 0 && p.length() == 0) {
            System.out.println(0);
            return;
        }
        int slen = s.length();
        int plen = p.length();
        // 当前位置是不是匹配的
        boolean[][] dp = new boolean[slen + 1][plen + 1];
        dp[0][0] = true;
        for (int j = 1; j <= plen && dp[0][j - 1]; j++) {
            dp[0][j] = p.charAt(j - 1) == '*';
        }
        for (int i = 1; i <= slen; i++) {
            for (int j = 1; j <= plen; j++) {
                char si = s.charAt(i - 1);
                char pj = p.charAt(j - 1);
                if (si == pj || pj == '?') {
                    dp[i][j] = dp[i - 1][j - 1];
                } else if (pj == '#') {
                    dp[i][j] = dp[i - 1][j - 1] || dp[i][j - 1];
                } else if (pj == '*') {
                    dp[i][j] = dp[i - 1][j] || dp[i][j - 1];
                }
            }
        }
        int res =  dp[slen][plen] ? 1 : 0;
        System.out.println(res);
    }
}
