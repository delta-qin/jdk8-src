package com.deltaqin.algo.code03_linked_list;

class ListNode {
      int val;
      ListNode next;
      ListNode() {}
      ListNode(int val) { this.val = val; }
      ListNode(int val, ListNode next) { this.val = val; this.next = next; }
  }

/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public static void main(String[] args) {
        ListNode node = new ListNode(1);
        node.next = new ListNode(2);
        node.next.next = new ListNode(3);
        node.next.next.next = new ListNode(4);

        new Solution().reorderList(node);

        while (node != null) {
            System.out.println(node.val);
            node = node.next;
        }
    }
    public void reorderList(ListNode head) {
        ListNode dummy = new ListNode();
        ListNode front = dummy;
        ListNode back = dummy;
        dummy.next = head;
        while(front != null && front.next != null) {
            front = front.next.next;
            back = back.next;
        }
        ListNode l1 = head;
        ListNode l2 = back.next;
        back.next = null;

        l2 = reverse(l2);

        ListNode dummy1 = new ListNode();
        ListNode rear = dummy1;
        while(l1 != null || l2 != null) {
            if(l1 != null) {
                rear.next = l1;
                l1 = l1.next;
                rear = rear.next;

            }
            if(l2 != null){
                rear.next = l2;
                l2 = l2.next;
                rear = rear.next;
            }
        }
        head = dummy1.next;
    }

    public ListNode reverse(ListNode node){
        ListNode dummy = new ListNode();
        // dummy.next = node;
        while(node != null) {
            ListNode t = node.next;
            node.next = dummy.next;
            dummy.next = node;
            node = t;
        }
        return dummy.next;
    }
}
