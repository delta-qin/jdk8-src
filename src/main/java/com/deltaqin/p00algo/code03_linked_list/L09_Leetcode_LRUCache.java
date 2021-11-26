package com.deltaqin.p00algo.code03_linked_list;

import java.util.HashMap;

/**
 * @author deltaqin
 * @date 2021/7/18 下午2:38
 */
public class L09_Leetcode_LRUCache {

    public static void main(String[] args) {
        L09_Leetcode_LRUCache lru = new L09_Leetcode_LRUCache(3);
        lru.put(1, 1);
        lru.put(2,2);
        lru.put(3,3);
        lru.put(4,4);
        lru.get(3);
        lru.print();
    }

    private void print() {
        list.print();
    }


    private int cap;
    private HashMap<Integer, Node> map;
    private DoubleList list;
    public L09_Leetcode_LRUCache(int capacity) {
        cap = capacity;
        map = new HashMap<Integer, Node>();
        list = new DoubleList();
    }

    private void addLast(int key, int val) {
        Node node = new Node(key, val);
        map.put(key, node);
        list.addLast(node);
    }

    public void deleteFirst() {

        Node tmp = list.removeFirst();
        map.remove(tmp.key);

    }

    public void delete(int key) {
        list.remove(map.get(key));
        map.remove(key);
    }

    public int get(int key) {
        if(!map.containsKey(key)) return -1;
        Node tmp = map.get(key);
        list.remove(tmp);
        list.addLast(tmp);
        return tmp.val;
    }

    public void put(int key, int val) {
        if(map.containsKey(key)) {
            delete(key);
            addLast(key, val);
            return;
        }
        if(map.size() == cap) {
            deleteFirst();
        }
        addLast(key, val);
    }
}

//class Node1{
//    int key;
//    int val;
//    Node prev;
//    Node next;
//    Node1(int key, int val) {
//        this.key = key;
//        this.val = val;
//    }
//}

class DoubleList{
    //private int size;
    private Node dummy;
    private Node tail;
    DoubleList() {
        //this.size = 0;
        dummy = new Node(0, 0);
        tail = new Node(0, 0);
        dummy.next = tail;
        tail.prev = dummy;
    }
    public void addLast(Node node) {
        tail.prev.next = node;
        node.next = tail;
        node.prev = tail.prev;
        tail.prev = node;
    }

    public Node removeFirst() {
        if(dummy.next == tail) return null;
        Node tmp = dummy.next;
        dummy.next.next.prev = dummy;
        dummy.next = dummy.next.next;
        return tmp;
    }

    public void remove(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    public void print() {
        Node tmp = dummy.next;
        while (tmp != tail) {
            System.out.print(tmp.key + " " + tmp.val + " ");
            tmp = tmp.next;
        }
    }
}
/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */
