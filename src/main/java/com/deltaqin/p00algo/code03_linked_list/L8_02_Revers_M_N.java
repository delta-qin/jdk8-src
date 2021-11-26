package com.deltaqin.p00algo.code03_linked_list;

/**
 * @author deltaqin
 * @date 2021/8/2 下午10:35
 */
public class L8_02_Revers_M_N {

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
        // 闭区间反转
        Node res = reverse(node, 2, 3);

        print(node);
    }

    static Node reverse(Node head, int m, int n) {
        if (m == 1) {
            return reverseN(head, n);
        }

        // 要接在自己的后面
        head.next = reverse(head.next, m - 1, n - 1);
        return head;
    }


    static Node tail = null;
    private static Node reverseN(Node node, int k) {
        if (k == 1) {
            tail = node.next;
            return node;
        }

        Node res = reverseN(node.next, k - 1);
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
