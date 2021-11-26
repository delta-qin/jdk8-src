package com.deltaqin.p00algo.code03_queue;

class MyCircularQueue02 {

    private int[] arr;
    private int head = 0;
    private int rear = 0;
    //private int size = 0;
    private int capacity = 0;

    public MyCircularQueue02(int capacity) {
        arr = new int[capacity+1];
        this.capacity = capacity+1;
    }


    // 获取队头
    public int getHead(){
        // 这里忘记了判空了
        if (head==rear) return -1;
        return arr[head];
    }

    //获取队尾
    public int getRear(){
        if (head==rear) return -1;
        return arr[(rear - 1 + capacity) % capacity];
    }

    //是否为空
    public boolean isEmpty(){
        return head==rear;
    }

    //是否满
    public boolean isFull(){
        return head==(rear+1)%capacity;
    }

    //增加元素
    public void add(int num){
        if (isFull()) return;

        arr[rear] = num;

        rear = (rear + 1) % capacity;
    }

    //删除元素
    public void delete(){
        if (isEmpty()) {
            return;
        }

        head = (head + 1) % capacity;
    }

}
