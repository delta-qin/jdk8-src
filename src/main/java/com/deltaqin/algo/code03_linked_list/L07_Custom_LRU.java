package com.deltaqin.algo.code03_linked_list;

import java.util.HashMap;

/**
 * @author deltaqin
 * @date 2021/7/16 下午5:05
 *
 * 删除的时候要获取前一个节点，使用双向链表可以是 O1 删除
 * 为什么尾部插入，头部最老，其实差不多
 *
 * 为什么双向链表要保存Node，而不直接保存val，key已经在map里面有了，
 * 注意通过LRU链表找到要删除的最后一个删除map的时候要知道key是谁，所以有必要维护ke的信息
 *
 * 注意添加新值的时候，key和原来一样的时候，需要删除map里面的kv，重新设置到map里面，
 * 而不是直接设置在链表里面最后加一个node就结束了，这样map里面的key指向的node 和在链表的里面的node不是同一个，
 */
// LRU 本来就是为了存放KV的
public class L07_Custom_LRU {
    public static void main(String[] args) {
        LRU lru = new LRU(3);
        lru.add(1, 1);
        lru.add(2,2);
        lru.add(3,3);
        lru.add(4,4);
        lru.get(3);
        lru.print();
    }

}

class Node{
    public int key;
    public int val;
    public Node prev;
    public Node next;
    public Node(int key, int val) {
        this.key = key;
        this.val = val;
    }

    @Override
    public String toString() {
        return "Node{" +
                "key=" + key +
                ", val=" + val +
                '}';
    }
}

class MyDoubleList{
    public Node head;
    public Node tail;
    public int size;

    public MyDoubleList() {
        head = new Node(0, 0);
        tail = new Node(0, 0);
        head.next = tail;
        tail.prev = head;
        size = 0;
    }

    public void addLast(Node node) {
        node.prev = tail.prev;
        node.next = tail;
        tail.prev.next = node;
        tail.prev = node;
        size++;
    }

    public void delete(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        size--;
    }

    public Node removeFirst() {
        if (head.next == tail) {
            return null;
        }
        Node x = head.next;
        //head.next = head.next.next;
        //head.next.prev = head;
        //size--;
        delete(x);
        return x;
    }

    public int size() {
        return size;
    }

    public void print() {
        Node tmp = head.next;
        while (tmp != tail) {
            System.out.println(tmp.toString());
            tmp = tmp.next;
        }
    }
}

class LRU {
    private MyDoubleList myDoubleList;
    private HashMap<Integer, Node> map;
    private int cap;

    public LRU(int cap) {
        myDoubleList = new MyDoubleList();
        map = new HashMap<>();
        this.cap = cap;
    }

    private void makeRecently(int key) {
        Node node = map.get(key);
        myDoubleList.delete(node);
        myDoubleList.addLast(node);
    }

    private void addRecently(int key, int val) {
        Node node = new Node(key, val);
        myDoubleList.addLast(node);
        map.put(key, node);
    }

    private void deleteLeastRecentlyNode() {
        Node node = myDoubleList.removeFirst();
        map.remove(node.key);
    }

    public void add(int key, int val) {
        if (myDoubleList.size == cap) {
            deleteLeastRecentlyNode();
        }
        if (map.containsKey(key)) {
            Node node = map.get(key);
            node.val = val;
            makeRecently(key);
            return;
        }
        addRecently(key, val);
    }

    public int get(int key) {
        if (map.containsKey(key)) {
            makeRecently(key);
            return map.get(key).val;
        }
        return -1;
    }

    public void print() {
        myDoubleList.print();
    }
}
