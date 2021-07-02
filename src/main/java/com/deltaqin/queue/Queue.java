package com.deltaqin.queue;

public interface Queue<T> {

  /**
   * 放数据
   * @param item 入参数
   * @return
   */
  boolean put(T item);

  /**
   * 拿数据
   * @return
   */
  T take();

  class Node<T> {

    // 数据本身
    T item;
    // 下一个元素
    Node<T> next;

    // 构造器
    public Node(T item) {
      this.item = item;
    }
  }
}
