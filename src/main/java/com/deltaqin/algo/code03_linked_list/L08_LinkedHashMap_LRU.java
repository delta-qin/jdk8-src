package com.deltaqin.algo.code03_linked_list;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * @author deltaqin
 * @date 2021/7/16 下午6:01
 *
 * 使用linkedHashMap 实现
 */
public class L08_LinkedHashMap_LRU {
    public static void main(String[] args) {
        SimpleLRU lru = new SimpleLRU(3);
        lru.add(1, 1);
        lru.add(2,2);
        lru.add(3,3);
        lru.add(4,4);
        lru.get(3);
        lru.print();
    }
}

class SimpleLRU {
    private int size;
    private LinkedHashMap<Integer, Integer> map;

    public SimpleLRU(int size) {
        if (size <= 0) {
            throw new RuntimeException("参数异常");
        }
        this.size = size;
        map = new LinkedHashMap<>();
    }

    public void add(int key, int val) {
        if (map.size() == size) {
            Integer k = map.keySet().iterator().next();
            map.remove(k);
        }
        if (map.containsKey(key)) {
            map.remove(key);
            map.put(key, val);
            return;
        }
        map.put(key, val);
    }

    public int get(int key) {
        if (map.containsKey(key)) {
            Integer val = map.get(key);
            map.remove(key);
            map.put(key, val);
            return val;
        }
        return -1;
    }

    public void print() {
        Iterator<Integer> iterator = map.values().iterator();
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            System.out.println(next + " ");
        }
    }
}
