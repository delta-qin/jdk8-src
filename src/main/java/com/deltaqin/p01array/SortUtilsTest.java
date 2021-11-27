package com.deltaqin.p01array;


import com.deltaqin.p06lamda.Person;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author deltaqin
 * @date 2021/11/27 7:39 下午
 */
public class SortUtilsTest {
    @Test
    public void extracted2() {
        Person[] arr = {new Person("a",1), new  Person("q", 3)
                , new  Person("c", 2)};
        Arrays.sort(arr);
        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void extracted1() {
        String[] strs = new String[]{"a","f","c","d"};
        String[] strs1 = new String[5];
        System.arraycopy(strs, 0, strs1, 0 , strs.length);
        for(String str : strs1){
            System.out.println(str);
        }

        String s = Arrays.toString(strs1);
        System.out.println(s);
        System.out.println(strs1);
    }

    @Test
    public void test9(){
        List<Person> list = Arrays.asList(
                new Person("a", 12),
                new Person("c", 16),
                new Person("b", 14),
                new Person("n", 18),
                new Person("g", 10)
        );

        Collections.sort(list, (p1, p2)->{
            if (p1.getAge() == p2.getAge()){
                return p1.getName().compareTo(p2.getName());
            }
//            return Integer.compare(p1.getAge(),p2.getAge());
            // 倒排
            return -Integer.compare(p1.getAge(),p2.getAge());
        });

        list.forEach(System.out::println);
        System.out.println("-----------");
        list.stream().forEach(System.out::println);
    }
}
