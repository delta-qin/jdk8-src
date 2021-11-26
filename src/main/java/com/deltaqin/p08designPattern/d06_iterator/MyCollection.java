package com.deltaqin.p08designPattern.d06_iterator;

/**
 * @author deltaqin
 * @date 2021/3/27 10:39 上午
 */
public interface MyCollection<E> {
    void add(E e);
    int size();

    MyIterator myiterator();
}
