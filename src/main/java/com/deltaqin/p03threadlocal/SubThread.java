package com.deltaqin.p03threadlocal;

public class SubThread extends Thread {
    private ThreadLocal<String> tl;

    public SubThread(ThreadLocal<String> tl) {
        this.tl = tl;
    }

    @Override
    public void run() {
        // 获取当前线程对象上绑定的值
        String str = tl.get();
        System.out.println("run...." + str);

        // 给当前线程对象(key),绑定值为 "sub thread"
        tl.set("sub thread");
        str = tl.get();
        System.out.println("run...." + str);
    }
}
