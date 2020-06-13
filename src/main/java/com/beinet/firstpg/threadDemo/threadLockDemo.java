package com.beinet.firstpg.threadDemo;

import lombok.Synchronized;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 临界资源加锁验证
 */
public class threadLockDemo extends baseDemo {
    Integer total = 0;

    static int maxNum = 100000;

    /**
     * 不加锁测试
     */
    public void noLockThreadTest1() {
        total = 0;
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < maxNum; i++) {
            pool.execute(() -> total++);
        }
        waitPoolShutdown(pool);
        out("不锁1，应得结果" + maxNum + "，实际结果：" + total);
    }

    /**
     * 加锁测试1, 代码里加同步锁
     */
    public void lockThreadTest1() {
        total = 0;
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < maxNum; i++) {
            pool.execute(() -> {
                synchronized (this) {
                    total++;
                }
            });
        }
        waitPoolShutdown(pool);
        out("加锁1，应得结果" + maxNum + "，实际结果：" + total);
    }

    /**
     * 加锁测试2，使用注解加锁
     */
    public void lockThreadTest2() {
        total = 0;
        ExecutorService pool = Executors.newCachedThreadPool();
        lockClass cc = new lockClass();
        for (int i = 0; i < maxNum; i++) {
            pool.execute(cc);
        }
        waitPoolShutdown(pool);
        out("加锁2，应得结果" + maxNum + "，实际结果：" + total);
    }

    class lockClass implements Runnable {
        @Override
        @Synchronized // 锁当前实例
        public void run() {
            total++;
        }
    }


    /**
     * 加锁测试3，调用内部方法，使用注解加锁，以确认不存在aop代理问题
     */
    public void lockThreadTest3() {
        total = 0;
        ExecutorService pool = Executors.newCachedThreadPool();
        lockClass2 cc = new lockClass2();
        for (int i = 0; i < maxNum; i++) {
            pool.execute(cc);
        }
        waitPoolShutdown(pool);
        out("加锁3，应得结果" + maxNum + "，实际结果：" + total);
    }

    class lockClass2 implements Runnable {
        @Override
        public void run() {
            inner();
        }

        @Synchronized // 锁当前实例
        void inner(){
            total++;
        }
    }


    /**
     * 加锁测试4, 使用lock加锁
     */
    public void lockThreadTest4() {
        Lock lock = new ReentrantLock();
        total = 0;
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < maxNum; i++) {
            pool.execute(() -> {
                lock.lock();
                total++;
                lock.unlock();// 应该放在finally里
            });
        }
        waitPoolShutdown(pool);
        out("加锁4，应得结果" + maxNum + "，实际结果：" + total);
    }
}