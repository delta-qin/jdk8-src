package com.deltaqin.jvm;

/**
 * @author deltaqin
 * @date 2021/5/11 4:30 下午
 */
// -Xmn10485760 -Xms20971520 -Xmx20971520
// -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=15
// -XX:PretenureSizeThreshold=10485760
// -XX:+UseParNewGC -XX:+UseConcMarkSweepGC
// -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc.log

public class PermGCLogTest {
    public static void main(String[] args) {

        byte[] bytes = new byte[2 * 1024 * 1024];
        bytes = new byte[2 * 1024 * 1024];
        bytes = new byte[2 * 1024 * 1024];

        byte[] bytes2 = new byte[128 * 1024];
        bytes2 = null;

        // 动态年龄判断之后，不一定是全部加入老年代，有一部分会到达Survivor
        byte[] bytes3 = new byte[2 * 1024 * 1024];

    }
}
