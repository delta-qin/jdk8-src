package com.deltaqin.algo.code03_linked_list;

/**
 * @author deltaqin
 * @date 2021/8/2 下午10:26
 */
public class L8_02_Revers_K {
    static class Node {
        int val;
        Node next;
        Node(int x) {
            val = x;
        }
    }

    public static void main(String[] args) {
        Node node = new Node(1);
        node.next = new Node(2);
        node.next.next = new Node(3);
        node.next.next.next = new Node(4);
        Node res = reverseList(node, 2);

        print(res);
    }

    static Node tail = null;
    private static Node reverseList(Node node, int k) {
        if (k == 1) {
            tail = node.next;
            return node;
        }

        Node res = reverseList(node.next, k - 1);
        node.next.next = node;
        node.next = tail;
        return res;
    }

    private static void print(Node res) {
        while (res != null) {
            System.out.print(res.val + " ");
            res = res.next;
        }
    }


}
