package com.deltaqin.p06lamda.字符串数组排序;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author deltaqin
 * @date 2021/11/27 8:11 下午
 */
public class StringArraySort {
    String[] words = new String[]{"123", "1345"};

    @Test
    public void test() {
        Arrays.sort(words, new LengthComparator());
        System.out.println(Arrays.toString(words));
    }

    class LengthComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            return -Integer.compare(o1.length(), o2.length());
        }
    }

    @Test
    public void test2() {
        Arrays.sort(words, (o1, o2) -> {
            return -Integer.compare(o1.length(), o2.length());
        });
        System.out.println(Arrays.toString(words));

    }

    @Test
    public void test3() {
        Arrays.sort(words, (o1, o2) -> -Integer.compare(o1.length(), o2.length()));
        System.out.println(Arrays.toString(words));

    }

}
