package com.deltaqin.atest;

import java.util.*;

/**
 * @author deltaqin
 * @date 2021/9/4 下午4:14
 */
public class Main07 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String tmp = sc.nextLine();
            int len = Integer.parseInt(tmp);
            String word1 = sc.nextLine();
            char[] chars = word1.toCharArray();
            int clen = 0;


            int res = 0;
            int index = word1.length() - 1;
            int aindex = word1.length() - 1;
            //System.out.println(alen);
            while (index >= 0) {
                // int t = aindex - index;
                while (index >= 0 && word1.charAt(index) != 'a') {
                    // res++;
                    index--;
                }
                if (index >= 0 && word1.charAt(index) == 'a') {
                    res += (aindex - index);
                    aindex--;
                }
                index--;
            }
            // int count = minEditDistance(word1, word2);
            System.out.println(res);
        }
    }
    //public static void main(String[] args) {
    //    Scanner sc = new Scanner(System.in);
    //    while (sc.hasNextLine()) {
    //        String tmp = sc.nextLine();
    //        int len = Integer.parseInt(tmp);
    //        String word1 = sc.nextLine();
    //        char[] chars = word1.toCharArray();
    //        int clen = 0;
    //        for (char c : chars) {
    //            if (c == 'c') {
    //                clen++;
    //            }
    //        }
    //        StringBuilder sb = new StringBuilder();
    //        int alen = len - clen;
    //
    //        int res = 0;
    //        int index = word1.length() - 1;
    //        int aindex = word1.length() - 1;
    //        //System.out.println(alen);
    //        while (index >= 0) {
    //            int t = aindex - index;
    //            while (index >= 0 && word1.charAt(index) != 'a') {
    //                res++;
    //                index--;
    //            }
    //            res += t;
    //            aindex--;
    //            index--;
    //        }
    //        // int count = minEditDistance(word1, word2);
    //        System.out.println(res);
    //    }
    //}
    //public static void main(String[] args) {
    //    Scanner sc = new Scanner(System.in);
    //    while (sc.hasNextLine()) {
    //        String tmp = sc.nextLine();
    //        int len = Integer.parseInt(tmp);
    //        String word1 = sc.nextLine();
    //        char[] chars = word1.toCharArray();
    //        int clen = 0;
    //        for (char c : chars) {
    //            if (c == 'c') {
    //                clen++;
    //            }
    //        }
    //        StringBuilder sb = new StringBuilder();
    //        int alen = len - clen;
    //        while (clen > 0) {
    //            sb.append('c');
    //            clen--;
    //        }
    //        while (alen > 0) {
    //            sb.append('a');
    //            alen--;
    //
    //        }
    //        String word2 = sb.toString();
    //        System.out.println(word1 + word2);
    //        int count = minEditDistance(word1, word2);
    //        System.out.println(count);
    //    }
    //}

    public static int minEditDistance(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();
        if (len1 * len2 == 0) {
            return len1 + len2;
        }
        int[][] dp = new int[len1 + 1][len2 + 1];
        for (int i = 0; i < len1; i++) {
            dp[i][0] = i;
        }
        for (int i = 0; i < len2; i++) {
            dp[0][i] = i;
        }
        for (int i = 1; i < len1; i++) {
            for (int j = 1; j < len2; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(
                            Math.min(dp[i - 1][j], dp[i][j - 1]),
                            dp[i - 1][j - 1]
                    ) + 1;
                }
            }
        }
        return dp[len1][len2];
    }
}
