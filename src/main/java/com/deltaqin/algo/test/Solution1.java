package com.deltaqin.algo.test;

/**
 * @author deltaqin
 * @date 2021/8/22 下午3:56
 */
public class Solution1 {
    public static void main(String[] args) {
        calcMinSailCost(new int[][]{{1,0,1,1,0},{0,1,0,1,0},{1,1,0,0,1},{0,0,0,0,1}});

    }

    public static  int calcMinSailCost (int[][] input) {
        // write code here
        int row = input.length;
        int col = input[0].length;
        // 走到当前位置的最小费用
        int dp[][] = new int[row + 1][col + 1];
        for (int i = 1; i <= row; i++) {
            for (int j = 1; j <= col; j++) {
                int cur = (input[i - 1][j - 1] == 1) ? 1 : 2;
                if (i == 1 && j == 1) cur = 0;
                dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1])) + cur;

            }
        }
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print(dp[i][j]);
            }
            System.out.println("");
        }
        return dp[row][col];
    }

    public static  int calcMinSailCost1 (int[][] input) {
        // write code here
        int row = input.length;
        int col = input[0].length;
        // 走到当前位置的最小费用
        int dp[][] = new int[row + 1][col + 1];
        for (int i = 1; i <= row; i++) {
            for (int j = 1; j <= col; j++) {
                int cur = (input[i - 1][j - 1] == 1) ? 1 : 2;
                if (i == 1 && j == 1) cur = 0;
                dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1])) + cur;

            }
        }
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print(dp[i][j]);
            }
            System.out.println("");
        }
        return dp[row][col];
    }

    public static int min = Integer.MAX_VALUE;

    //public static void dfs(int[][] input, int x, int y, int cur) {
    //
    //
    //
    //    int row = input.length;
    //    int col = input[0].length;
    //
    //    if (x >= row || x < 0 || y >= col) {
    //        return;
    //    }
    //
    //    if (x == row - 1 && y == col  -1) {
    //        min = Math.min(cur, min);
    //        return;
    //    }
    //
    //    dfs(input, x + 1, y, input[x][y] + cur);
    //    dfs(input, x, y + 1, input[x][y] + cur);
    //    dfs(input, x - 1, y + 1, input[x][y] + cur);
    //    dfs(input, x + 1, y + 1, input[x][y] + cur);
    //}

    //public int calcMinSailCost (int[][] input) {
    //    // write code here
    //    int row = input.length;
    //    int col = input[0].length;
    //    // 走到当前位置的最小费用
    //    int dp[][] = new int[row + 1][col + 1];
    //    for (int i = 1; i <= row; i++) {
    //        for (int j = 1; j <= col; j++) {
    //            int cur = (input[i - 1][j - 1] == 1) ? 1 : 2;
    //            if (i == 1 && j == 1) cur = 0;
    //            dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1])) + cur;
    //
    //        }
    //    }
    //    return dp[row][col];
    //}

    //public int min = Integer.MAX_VALUE;
    //public int calcMinSailCost (int[][] input) {
    //    // write code here
    //    this.dfs(input, 0, 0, 0);
    //    return min;
    //}

    public void dfs(int[][] input, int x, int y, int cur) {
        int row = input.length;
        int col = input[0].length;

        if (x >= row || x < 0 || y >= col) {
            return;
        }

        if (x == row - 1 && y == col  -1) {
            min = Math.min(cur, min);
            return;
        }
        int cur1 = (input[x][y] == 1) ? 1 : 2;
        if (x == 0 && y == 0) cur1 = 0;
        dfs(input, x + 1, y, cur + cur1);
        dfs(input, x, y + 1, cur + cur1);
        dfs(input, x - 1, y + 1,cur + cur1);
        dfs(input, x + 1, y + 1, cur + cur1);
    }
}
