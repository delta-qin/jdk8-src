package com.deltaqin.p08designPattern.d06_iterator;

/**
 * @author deltaqin
 * @date 2021/3/27 10:33 上午
 */
public class Demo {
    public static void main(String[] args) {
        MyCollection<String> myCollection = new MyArrayList<>();

        myCollection.add("delta0");
        myCollection.add("delta1");
        myCollection.add("delta2");
        myCollection.add("delta3");

        MyIterator myiterator = myCollection.myiterator();
        while (myiterator.hasNext()) {
            System.out.println(myiterator.next());
        }
    }
}
