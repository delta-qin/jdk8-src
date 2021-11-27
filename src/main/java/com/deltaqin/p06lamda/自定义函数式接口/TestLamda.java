package com.deltaqin.p06lamda.自定义函数式接口;

import org.junit.Test;

public class TestLamda{
    @Test
    public void test(){
        String hello = new TestLamda().strHandler("hello", s -> s.toUpperCase());
        // 0123 包含2不包含5
        String hello1 = new TestLamda().strHandler("hello1", s -> s.substring(2,5));
        System.out.println(hello);
        System.out.println(hello1);

    }

    @Test
    public void test1(){
        new TestLamda().LongHandler(1L,2L,(l1,l2)-> l1+l2);
        new TestLamda().LongHandler(1L,2L,(l1,l2)-> l1*l2);
    }

     public String strHandler(String str, LamdaInterface lamdaInterface){
         return lamdaInterface.getVal(str);
     }

     public void LongHandler(Long l1, Long l2, MyFunction<Long, Long> mf){
         System.out.println(mf.getVal(l1,l2));
     }
}
