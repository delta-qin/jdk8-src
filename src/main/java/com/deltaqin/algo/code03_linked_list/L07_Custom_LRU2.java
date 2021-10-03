package com.deltaqin.algo.code03_linked_list;

import java.util.HashMap;
import java.util.Map;

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
public class L07_Custom_LRU2 {

    public static void main(String[] args) {

        LRU2 l = new LRU2(3);
        l.add(1, 1);
        l.add(2, 2);
        l.add(3, 3);
        l.add(4, 4);
        l.get(2);

        print(l);
    }

    private static void print(LRU2 l) {
        DeList list = l.list;
        Node2 cur = list.dummy.next;

        while (cur != null) {
            System.out.println(cur.val + " ");
            cur = cur.next;
        }
    }
}


class LRU2 {
    int size;
    Map<Integer, Node2> map = new HashMap<>();
    DeList list = new DeList();

    LRU2(int size) {
        this.size = size;
    }

    public void add(int key, int val) {
        if (map.containsKey(key)) {
            Node2 node = map.get(key);
            list.delete(node);
            map.remove(key);
            node.val = val;
            list.addLast(node);
            map.put(key, node);
            return;
        }

        if (map.size() >= size) {
            Node2 node2 = list.deleteFirst();
            map.remove(node2.key);
        }

        Node2 node1 = new Node2(key, val);
        list.addLast(node1);
        map.put(key, node1);
    }

    public int get(int key) {
        if (!map.containsKey(key)) {
            return -1;
        }
        Node2 node = map.get(key);
        list.delete(node);
        list.addLast(node);
        return node.val;
    }
}

class DeList {
    Node2 dummy;
    Node2 tail;

    DeList() {
        dummy = new Node2(0, 0);
        tail = dummy;
    }

    // 所有的都忘记设置prev
    public void addLast(Node2 node) {
        node.prev = tail;
        tail.next = node;
        tail = tail.next;
        // 有的node是之前的node，所以要把尾巴去掉
        tail.next = null;
    }

    public Node2 delete(Node2 node) {
        node.next.prev = node.prev;
        node.prev.next = node.next;
        return  node;
    }

    public Node2 deleteFirst() {
        Node2 node = dummy.next;
        node.next.prev = dummy;
        dummy.next = node.next;
        return node;
    }
}

class Node2 {
    int key;
    int val;
    Node2 prev;
    Node2 next;
    Node2(int key, int val) {
        this.key = key;
        this.val = val;
    }
}

