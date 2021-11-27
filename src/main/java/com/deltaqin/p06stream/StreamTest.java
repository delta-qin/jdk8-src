package com.deltaqin.p06stream;

import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shiqiao.qzt
 * @Classname StreamTest
 * @Description TODO
 * @Date 2021/11/26 10:24 上午
 */

public class StreamTest {

    Map<Integer, Integer> map = new HashMap<>();
    List<DO> list = new ArrayList<>();

    @Before
    public void before() {
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        list.add(new DO(1, 1));
        list.add(new DO(2, 2));
        list.add(new DO(2, 3));
        list.add(new DO(3, 3));
     }

    @Test
    public void test1() {
        List<Integer> collect1 = list.stream().map(DO::getId).collect(Collectors.toList());
        System.out.println(collect1.toString());

        List<String> collect2 = list.stream().map(item -> String.valueOf(item.getCount())).collect(Collectors.toList());
        System.out.println(collect2);

        // 转换中出现相同的，如何处理
        // Collectors.toMap
        Map<Integer, Integer> collect = list.stream()
                                       .collect(Collectors.toMap(DO::getId, DO::getCount, (a, b) -> b));
        System.out.println(collect.toString());

        // 保存相同的
        // Collectors.groupingBy
        Map<Integer, List<DO>> collect3 = list.stream().collect(Collectors.groupingBy(DO::getId));
        System.out.println(collect3);
    }
}
