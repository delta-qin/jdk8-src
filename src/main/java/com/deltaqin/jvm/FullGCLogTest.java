package com.deltaqin.jvm;

/**
 * @author deltaqin
 * @date 2021/5/11 4:01 下午
 */
//-Xmn10485760 -Xms20971520 -Xmx20971520
// -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=15
// -XX:PretenureSizeThreshold=10485760
// -XX:+UseParNewGC -XX:+UseConcMarkSweepGC
// -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:gc.log

public class FullGCLogTest {
    //public static void main(String[] args) {
    //
    //    byte[] bytes = new byte[2 * 1024 * 1024];
    //    bytes = new byte[2 * 1024 * 1024];
    //    bytes = new byte[2 * 1024 * 1024];
    //    bytes = null;
    //
    //    byte[] bytes1 = new byte[128 * 1024];
    //    byte[] bytes2 = new byte[2 * 1024 * 1024];
    //
    //    bytes2 = new byte[2 * 1024 * 1024];
    //    bytes2 = new byte[2 * 1024 * 1024];
    //    bytes2 = null;
    //
    //
    //    byte[] bytes4 = new byte[2 * 1024 * 1024];
    //
    //}

    public static void main(String[] args) {

        byte[] bytes = new byte[4 * 1024 * 1024];
        bytes = null;

        byte[] bytes2 = new byte[2 * 1024 * 1024];
        byte[] bytes3 = new byte[2 * 1024 * 1024];

        // young
        byte[] bytes4 = new byte[2 * 1024 * 1024];
        byte[] bytes5 = new byte[128 * 1024];

        byte[] bytes6 = new byte[2 * 1024 * 1024];


        byte[] bytes8 = new byte[2 * 1024 * 1024];
        byte[] bytes7 = new byte[2 * 1024 * 1024];

    }
}
