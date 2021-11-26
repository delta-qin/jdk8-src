package com.deltaqin.p00atest;


import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author deltaqin
 * @date 2021/10/4 上午9:09
 */
public class ProConTest2 {
    private Queue queue;
    private int max = 16;
    private ReentrantLock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();

    public ProConTest2(int size) {
        this.max = size;
        queue = new LinkedList();
    }

    public void  put(Object o) throws InterruptedException {
        lock.lock();
        // 阿里巴巴插件提示，lock 后面必须加上try
        try {
            while (queue.size() == max) {
                // 等待不满
                notFull.await();
            }
            queue.add(o);
            // 已经不空了
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public Object take() throws InterruptedException {
        lock.lock();
        try {
            // 不能使用if，否则多个被唤醒之后，都会往下执行
            while (queue.size() == 0) {
                // 等待非空
                // 等待的时候释放锁
                notEmpty.await();
                // 被唤醒之后获取锁，
                // 还需要使用while判断，而不是使用if直接往下执行，多线程会导致取不出来抛异常
            }
            Object item = queue.remove();
            // 已经不满了
            notFull.notifyAll();
            return item;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {

    }
}
