package com.deltaqin.mutilthread.queue;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;


@Slf4j
public class NumbersConsumer implements Runnable {
    private BlockingQueue<Integer> queue;
    private final int poisonPill;

    public NumbersConsumer(BlockingQueue<Integer> queue, int poisonPill) {
        this.queue = queue;
        this.poisonPill = poisonPill;
    }

    public void run() {
        try {
            while (true) {
                Integer number = queue.take();
                if (number.equals(poisonPill)) {
                    return;
                }
                log.info("武大郎-{}号,喝药-编号:{}",Thread.currentThread().getId(),number);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
