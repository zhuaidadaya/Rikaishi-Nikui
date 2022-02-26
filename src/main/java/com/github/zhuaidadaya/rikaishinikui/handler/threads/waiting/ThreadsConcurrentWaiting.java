package com.github.zhuaidadaya.rikaishinikui.handler.threads.waiting;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class ThreadsConcurrentWaiting {
    private final Collection<Thread> threads = new LinkedHashSet<>();
    private final ThreadsDoneCondition condition;
    private int alive = 0;
    private ExecutorService useThreadPool;

    /**
     * waiting with parent thread<br>
     * faster but unsafer<br>
     * <br>
     *
     * @param doneCondition what when we should have done waiting
     * @param threads       participated threads
     */
    public ThreadsConcurrentWaiting(ThreadsDoneCondition doneCondition, Thread... threads) {
        this.threads.addAll(List.of(threads));
        this.condition = doneCondition;
    }

    public void start() {
        synchronized (this) {
            if (useThreadPool == null) {
                try {
                    threads.forEach(t -> {
                        t.start();
                        alive++;
                    });

                    while (threads.size() != 0) {
                        if (condition == ThreadsDoneCondition.ALIVE & threads.parallelStream().allMatch(Thread::isAlive)) {
                            Thread.currentThread().join(10);
                        }
                        threads.removeIf(t -> {
                            if (!t.isAlive()) {
                                alive--;
                                return true;
                            }
                            return false;
                        });
                    }
                } catch (Exception e) {

                }
            }
        }
    }

    public Collection<Thread> getThreads() {
        return threads;
    }

    public int aliveThreads() {
        return alive;
    }
}
