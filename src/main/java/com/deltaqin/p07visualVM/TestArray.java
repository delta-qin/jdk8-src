package com.deltaqin.p07visualVM;

/**
 * @author deltaqin
 * @date 2021/5/30 9:51 下午
 */

// 数组类型是JVM运行时期动态生成的。他的父类型就是Object
//    引用类型的数组在初始化的时候不会导致类的主动使用，
public class TestArray {
    public static void main(String[] args) {
        Child2[] child2s = new Child2[1];
        System.out.println(child2s.getClass());  // class [Lcom.deltaqin.visualVM.Child2;

        Child2[][] child2s1 = new Child2[1][1];
        System.out.println(child2s1.getClass());  // class [[Lcom.deltaqin.visualVM.Child2;


    }

}

class Child2 {
    static {
        System.out.println("111");
    }
}
