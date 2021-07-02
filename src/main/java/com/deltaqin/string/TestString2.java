package com.deltaqin.string;

public class TestString2 {
//    public static void main(String[] args) { // args[0] "abc", args[1] "abc"
//        String s1 = "a";
//        String s2 = "abc";
//        System.out.println(args[0] == s2);
//        System.out.println(args[1] == s2);

    public static void main(String[] args) {
        String s = new String("1");
        // 此时常量池已经有2了
        s.intern();
        // 没加进去，失败了，因为已经有了，所以常量池不是堆中String的引用，自己独立的
        String s2 = "1";
        // 没加进去，已经有了，但是不是堆里面的那个，是常量池自己的对象

        // s存的是堆里面的引用
        // s2存的是字符串自己的引用
        System.out.println(s == s2);

        String s3 = new String("1") + new String("1");
        // 常量池有1这个字符串，但是没有11这个字符串
        s3.intern();
        // 没有11字符串也没有引用，加入成功，此时常量池里面放的是s3的引用
        String s4 = "11";
        // 放S4 equals发现有了，所以直接返回对应的引用

        // 现在s3和s4都是使用的是在堆里面的引用，所以相等
        System.out.println(s3 == s4);
    }
}