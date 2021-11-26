package com.deltaqin.p07jvm;

/**
 * @author deltaqin
 * @date 2021/5/11 5:35 下午
 */
public class FullGC {
    public static void main(String[] args) {
        // 直接去old
        byte[] bytes = new byte[4 * 1024 * 1024];
        bytes = null;

        byte[] bytes2 = new byte[2 * 1024 * 1024];
        byte[] bytes3 = new byte[2 * 1024 * 1024];
        byte[] bytes4 = new byte[2 * 1024 * 1024];
        byte[] bytes5 = new byte[128 * 1024];

        // young 前面的Survivor放不下， 直接去了老年代。
        byte[] bytes6 = new byte[2 * 1024 * 1024];

    }
}
