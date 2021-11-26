package com.deltaqin.p08designPattern.d02_strategy;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author deltaqin
 * @date 2021/3/26 2:31 下午
 */
// 使用泛型，配合比较策略，直接调用策略的接口方法，
//     具体的策略由使用者使用的时候传入，而不是定义具体类的时候，类和策略应该是分离的
public class Sorter<T> {

    // 选择
    public void sort(T[] arr, Comparator<T> comparator) {
        for (int i = 0; i < arr.length -1; i++) {
            int min = i;
            for (int j = i+1; j < arr.length; j++) {
                if (comparator.compare(arr[j] , arr[min]) == -1 )
                    min = j;
            }
            swap(arr, min, i);
        }

        System.out.println(Arrays.toString(arr));
    }

    public void sort(Comparable[] arr) {
        for (int i = 0; i < arr.length -1; i++) {
            int min = i;
            for (int j = i+1; j < arr.length; j++) {
                if (arr[j].compareTo(arr[min]) == -1 )
                    min = j;
            }
            swap1(arr, min, i);
        }

        System.out.println(Arrays.toString(arr));
    }

    public  void swap(T[] arr, int i, int j) {
        T t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }

    public  void swap1(Comparable[] arr, int i, int j) {
        Comparable t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }

    public static void main(String[] args) {
        //sort(new int[]{2,4,1});
    }
}
