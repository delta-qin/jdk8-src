package com.deltaqin.p06lamda.自定义函数式接口;

@FunctionalInterface
public interface MyFunction <T,R>{
    R getVal(T t1, T t2);
}
