package com.deltaqin.p08designPattern.d06_iterator;

/**
 * @author deltaqin
 * @date 2021/3/27 10:38 上午
 */
public interface MyIterator<T> {
    boolean hasNext();

    T next();
}
