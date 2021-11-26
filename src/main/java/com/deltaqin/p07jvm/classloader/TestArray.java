package com.deltaqin.p07jvm.classloader;

/**
 * @author deltaqin
 * @date 2021/6/1 下午5:45
 */
public class TestArray {
    public static void main(String[] args) {

        Parent6[] parent6s = new Parent6[1];
        Parent6[][] parent6s1 = new Parent6[1][1];
        System.out.println(parent6s.getClass());
        System.out.println(parent6s1.getClass());
        System.out.println(parent6s1.getClass().getClassLoader());
    //     class [Lcom.deltaqin.jvm.classloader.Parent6;
    //    class [[Lcom.deltaqin.jvm.classloader.Parent6;
        // sun.misc.Launcher$AppClassLoader@18b4aac2

        String[] str = new String[1];
        System.out.println(str.getClass().getClassLoader()); // null

        int[] ints = new int[1];
        System.out.println(ints.getClass());// class [I
        System.out.println(ints.getClass().getClassLoader()); // null
        byte[] w = new byte[1];
        System.out.println(w.getClass()); // class [B
        short[] ww = new short[1];
        System.out.println(ww.getClass());// class [S
        long[] www = new long[1];
        System.out.println(www.getClass());// class [J
        char[] wwww = new char[1];
        System.out.println(wwww.getClass()); //class [C
        boolean[] wwwww = new boolean[1];
        System.out.println(wwwww.getClass()); // class [Z
    }
}

class Parent6 {
    static {
        System.out.println("Parent6");
    }
}
