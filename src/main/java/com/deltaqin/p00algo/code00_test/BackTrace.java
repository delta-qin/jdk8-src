package com.deltaqin.p00algo.code00_test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author deltaqin
 * @date 2021/8/15 上午7:15
 */
public class BackTrace {
    public static void main(String[] args) {
        BackTrace backTrace = new BackTrace();
        List<List<Integer>> combine = backTrace.combine(4);
        Iterator<List<Integer>> iterator = combine.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next().toString());
        }
    }
    public List<List<Integer>> combine(int n) {
        List<List<Integer>> list = new LinkedList<>();
        backtrack(list, n, 1, new ArrayList<>());
        return list;
    }

    private void backtrack(List<List<Integer>> list,
                           int n,  int start,
                           List<Integer> tempList) {
        //终止条件，找到一组组合
        list.add(new LinkedList<>(tempList));
        if (start > n) {
            return;
        }
        //注意这里的i不能从0开始，如果从0开始会出现重复的，比如[1，2]和[2，1]
        for (int i = start; i <= n; i++) {
            //把当前值添加到集合中
            tempList.add(i);
            //递归调用
            backtrack(list, n, i + 1, tempList);
            //从当前分支跳到下一个分支的时候要把之前添加的值给移除
            tempList.remove(tempList.size() - 1);
        }
    }

}
