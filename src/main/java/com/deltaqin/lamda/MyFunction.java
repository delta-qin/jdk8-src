package com.deltaqin.lamda;

@FunctionalInterface
public interface MyFunction <T,R>{
    public R getVal(T t1, T t2);
}
