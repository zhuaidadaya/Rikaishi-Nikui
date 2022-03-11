package com.github.zhuaidadaya.rikaishinikui.handler.threads.waiting;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ThreadsConcurrentWaiting {
    private final Collection<Thread> threads = new ObjectArraySet<>();
    private final Collection<Thread> threadsTg = new ObjectArraySet<>();
    private final ThreadsDoneCondition condition;
    private int alive = 0;
    private boolean running = false;
    private int limit = 256;

    public ThreadsConcurrentWaiting(ThreadsDoneCondition doneCondition) {
        this.condition = doneCondition;
    }

    public ThreadsConcurrentWaiting(ThreadsDoneCondition doneCondition, Thread... threads) {
        this.threads.addAll(List.of(threads));
        this.condition = doneCondition;
    }

    public void start() {
        running = true;
        synchronized (this) {
            try {
                threadsTg.addAll(threads);
                Iterator<Thread> iterator = threads.iterator();
                while (alive < limit) {
                    if (iterator.hasNext()) {
                        iterator.next().start();
                        alive++;
                    } else {
                        break;
                    }
                }

                while (threadsTg.size() > 0) {
                    if (condition == ThreadsDoneCondition.ALIVE & alive > limit) {
                        Thread.currentThread().join(10);
                    }
                    threadsTg.removeIf(t -> {
                        if (!t.isAlive()) {
                            alive--;
                            return true;
                        }
                        return false;
                    });

                    if (iterator.hasNext() & alive < limit) {
                        iterator.next().start();
                        alive++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void add(Thread thread) {
        if (!running) {
            threads.add(thread);
        } else {
            throw new IllegalStateException("threads is running");
        }
    }

    public Collection<Thread> getThreads() {
        return threads;
    }

    public int aliveThreads() {
        return alive;
    }

    public void limit(int limit) {
        this.limit = limit;
    }
}
