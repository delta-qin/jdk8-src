package com.deltaqin.algo.code03_linked_list;

/**
 * @author deltaqin
 * @date 2021/8/2 下午11:01
 */
public class L8_02_Revers_K_Group {

    static class Node {
        int val;
        Node next;
        Node(int val) {
            this.val = val;
        }
    }

    public static void main(String[] args) {
        Node head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        head.next.next.next = new Node(4);
        Node res = reverse(head, 2);
        print(res);
    }

    static Node reverseAB(Node head, Node tail) {

        Node pre = null;
        Node cur = head;
        while (cur != tail) {
            Node tmp = cur.next;
            cur.next = pre;
            pre = cur;
            cur = tmp;
        }
        return pre;
    }

    static Node reverse(Node head, int k) {
        if (head == null) {
            return null;
        }

        Node a = head;
        Node b = head;
        for (int i = 0; i < k; i++) {
            if (b == null) {
                return head;
            }
            b = b.next;
        }

        Node node = reverseAB(a, b);
        head.next = reverse(b, k);
        return node;
    }

    static void print(Node head) {
        while (head != null) {
            System.out.println(head.val + " ");
            head = head.next;
        }
    }
}
