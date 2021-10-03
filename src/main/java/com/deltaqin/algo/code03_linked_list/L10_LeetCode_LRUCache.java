package com.deltaqin.algo.code03_linked_list;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author deltaqin
 * @date 2021/7/18 下午2:57
 *
 * LinkedHashMap添加的时候也需要先删除后添加
 * 注意迭代器找最老的元素的写法
 * */
public class L10_LeetCode_LRUCache {
    public static void main(String[] args) {
        L10_LeetCode_LRUCache lru = new L10_LeetCode_LRUCache(3);
        lru.put(1, 1);
        lru.put(2,2);
        lru.put(3,3);
        lru.put(4,4);
        lru.get(3);
        lru.print();
    }

    private void print() {
        Iterator<Integer> iterator = map.values().iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }

    }

    private Map<Integer, Integer> map;
    private int cap;

    public L10_LeetCode_LRUCache(int capacity) {
        cap = capacity;
        map = new LinkedHashMap<>();
    }

    public int get(int key) {
        if (map.containsKey(key)) {
            int value = map.get(key);
            map.remove(key);
            map.put(key, value);
            return map.get(key);
        }
        return -1;
    }

    public void put(int key, int value) {
        if (map.containsKey(key)) {
            map.remove(key);
            map.put(key, value);
            return;
        }
        if (map.size() == cap) {
            int key1 = map.keySet().iterator().next();
            map.remove(key1);
        }
        map.put(key, value);
    }
}


/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */
