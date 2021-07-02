package com.deltaqin.collection;

import java.util.*;

/**
 * 迭代器的使用
 *
 * 实现Iterable接口之后，内部定一个Iterator的实现类，实现类有两个方法hasNext和next
 *
 * @author deltaqin
 * @date 2021/5/25 1:05 下午
 */
public class 随机序列产生器 {
    public static void main(String[] args) {
        // 0. list 转换为list的时候有坑，注意直接使用toArray, 就是Object，需要传递一个特定类型的空数组
        //  可以看到方法toArray定义里面是有泛型的
        List<String> list0 = Arrays.asList(new String[]{"List", "Tree", "Array"});
        // 数组大小随意，放不下就直接用一个新的
        String[] strings1 = list0.toArray(new String[0]);

        // 1. 注意这里的数组转list的方法
        List<String> list = Arrays.asList(new String[]{"List", "Tree", "Array"});
        RandomString<String> strings = new RandomString<>(list);

        Iterator<String> iterator = strings.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}

class RandomString<T> implements Iterable<T> {

    private final List<T> list;

    public RandomString(List<T> list) {
        this.list = list;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                return list.get((int)(Math.random() * list.size()));
            }
        };
    }
}
