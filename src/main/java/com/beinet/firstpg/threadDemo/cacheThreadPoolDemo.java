package com.beinet.firstpg.threadDemo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 固定线程池的使用Demo
 */
public class cacheThreadPoolDemo extends baseDemo {

    /**
     * 可缓存线程池，无核心线程，空闲线程的存活时间60秒，所有任务不等待，立即执行，任务多时创建的线程也多，如果任务非密集型，线程创建消耗会比较大。
     * 注意：如果需要执行的任务特别多，可能导致线程创建太多，从而内存溢出。
     * 一般不建议使用
     */
    public void cacheThreadPoolTest1() {
        ExecutorService pool = Executors.newCachedThreadPool();
        // 下面的代码，会创建10个线程去执行
        for (int i = 0; i < 10; i++) {
            int tmpIdx = i;
            pool.execute(() -> out("参数 " + tmpIdx));
        }

        // 等30秒，用上面的4个线程再跑一次
        sleep(30000);
        for (int i = 0; i < 4; i++) {
            int tmpIdx = i;
            pool.execute(() -> out("参数 " + tmpIdx));
        }

        // 等31秒，再跑6次任务，可以发现，第2次的4个线程还在，多了2个新线程，第1次创建，但是第2次没用的6个线程被释放了
        sleep(31000);
        for (int i = 0; i < 6; i++) {
            int tmpIdx = i;
            pool.execute(() -> out("参数 " + tmpIdx));
        }
        waitPoolShutdown(pool);
    }

}