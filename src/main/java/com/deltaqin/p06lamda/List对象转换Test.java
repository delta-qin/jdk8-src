package com.deltaqin.p06lamda;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shiqiao.qzt
 * @Classname List对象转换Test
 * @Description TODO
 * @Date 2021/11/29 10:40 上午
 */
public class List对象转换Test {
    @Test
    public void test(){
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(1);
        integers.add(1);

        List<String> collect = integers.stream().map(integer -> {
            return (String)null;
        }).collect(Collectors.toList());
        System.out.println(collect.size());
        System.out.println(collect.toString());
    }
}
