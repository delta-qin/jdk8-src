package com.deltaqin.algo.code03_linked_list;

/**
 * @author deltaqin
 * @date 2021/8/2 下午9:51
 */
public class L8_02_Revers {
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
        Node res = reverseList1(node);

        print(res);
    }

    private static void print(Node res) {
        while (res != null) {
            System.out.print(res.val + " ");
            res = res.next;
        }
    }

    static Node reverseList(Node node) {

        if (node.next == null) {
            return node;
        }

        Node res = reverseList(node.next);
        node.next.next = node;
        node.next = null;
        return res;
    }

    static Node reverseList1(Node node) {
        if (node == null) {
            return null;
        }

        Node pre = null;
        Node cur = node;
        while (cur != null) {
            Node tmp = cur.next;
            cur.next = pre;
            pre = cur;
            cur = tmp;
        }

        return pre;
    }


}
