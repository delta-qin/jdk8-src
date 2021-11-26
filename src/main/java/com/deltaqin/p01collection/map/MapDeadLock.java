package com.deltaqin.p01collection.map;

/**
 * @author ：图灵-杨过
 * @date：2019/7/5
 * @version: V1.0
 * @slogan:天下风云出我辈，一入代码岁月催
 * description：
 */
public class MapDeadLock {

    public static void main(String[] args) {
        for (int i=0; i<10; i++){
            new Thread(new MultiThread()).start();
        }
    }
}
