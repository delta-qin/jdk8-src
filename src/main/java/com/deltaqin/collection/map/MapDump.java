package com.deltaqin.collection.map;

import java.util.HashMap;

public class MapDump {


    public static void main(String[] args){
        HashMap hashMap = new HashMap<Object, String>(11);

        hashMap.put("杨过","2020");

        System.out.println(hashMap.get("杨过"));

        Integer hash = "2020".hashCode();
        System.out.println(hash%16);
        System.out.println(hashMap.get("2020"));

        //test();
    }

    public static void test(){
        HashMap hashMap = new HashMap<Integer, Integer>();
        int j = 0;
        for (int i=0;i<9;i++){
            hashMap.put(j,i);
            j = j+16;
        }
    }


}
