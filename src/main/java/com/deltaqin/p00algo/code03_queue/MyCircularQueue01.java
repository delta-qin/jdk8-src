package com.deltaqin.p00algo.code03_queue;

class MyCircularQueue01 {

    private int[] arr;
    private int head = 0;
    private int rear = 0;
    private int size = 0;
    private int capacity = 0;

    public MyCircularQueue01(int capacity) {
        arr = new int[capacity];
        this.capacity = capacity;
    }


    // 获取队头
    public int getHead(){
        // 这里忘记了判空了
        if (size == 0) return -1;
        return arr[head];
    }

    //获取队尾
    public int getRear(){
        if (size==0) return -1;
        return arr[(rear - 1 + capacity) % capacity];
    }

    //是否为空
    public boolean isEmpty(){
        return 0 == size;
    }

    //是否满
    public boolean isFull(){
        return capacity == size;
    }

    //增加元素
    public void add(int num){
        if (size == capacity) return;

        arr[rear] = num;
        size++;
        rear = (rear + 1) % capacity;
    }

    //删除元素
    public void delete(){
        if (size == 0) {
            return;
        }
        size--;
        head = (head + 1) % capacity;
    }

}
