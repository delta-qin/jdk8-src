package com.deltaqin.p08designPattern.d02_strategy;

/**
 * @author deltaqin
 * @date 2021/3/26 3:38 下午
 */
@FunctionalInterface
public interface Comparator<T> {
    public int compare(T o1, T o2);
}
