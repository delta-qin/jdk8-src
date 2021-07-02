package com.deltaqin.designPattern.d06_iterator;

/**
 * @author deltaqin
 * @date 2021/3/27 10:40 上午
 */
public class MyArrayList<E> implements MyCollection<E>{
    private E[] objects = (E[])new Object[10];
    private int size = 0;

    public void add(E e) {
        if (size+1 < objects.length) {
            E[] objectTmp = (E[])new Object[objects.length * 2];
            System.arraycopy(objects,0,objectTmp,0,objects.length);
            objects = objectTmp;
        }

        objects[size++] = e;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public MyIterator myiterator() {
        return new ArrayListIterator<E>();
    }

    class ArrayListIterator<E> implements MyIterator<E> {

        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public E next() {
            E e = (E) objects[index++];
            return e;
        }
    }
}
