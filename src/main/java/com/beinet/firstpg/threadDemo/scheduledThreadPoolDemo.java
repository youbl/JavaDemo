package com.beinet.firstpg.threadDemo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 定时任务线程池的使用Demo
 */
public class scheduledThreadPoolDemo extends baseDemo {

    /**
     * 不需要返回值的固定线程池测试
     */
    public void ScheduledThreadPoolTest1() {

        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
        // 下面10个任务，由2个线程轮流完成，所以如果任务多，要多建几个线程
        for (int i = 0; i < 10; i++) {
            // 只在1秒后，执行1次
            pool.schedule(() -> {
                out("schedule第1秒只执行1次");
                sleep(2000);
            }, 1, TimeUnit.SECONDS);
        }
        waitPoolShutdown(pool);
    }

    /**
     * 通过返回值阻塞线程的执行，直接完毕再退出
     */
    public void ScheduledThreadPoolTest2() {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(10);

        // 计划任务，每2秒执行一次，
        // 上一次启动经过2秒后，分2种情况：
        // 1、如果上一次任务已经完成，就立即执行
        // 2、如果上一次任务未完成，等到完成后再立即执行
        pool.scheduleAtFixedRate(() -> {
            out(" scheduleAtFixedRate 任务开始");
            sleep(3000);
            out(" scheduleAtFixedRate 任务结束");
        }, 1, 2, TimeUnit.SECONDS);
        // pool.shutdown(); 不能shutdown，会取消上面的任务
        sleep(30000);
        pool.shutdown();
    }

    /**
     * 带返回值的线程池操作
     */
    public void ScheduledThreadPoolTest3() {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(10);

        // 计划任务，每2秒执行一次，
        // 上一次任务执行完毕后，再等2秒后执行
        pool.scheduleWithFixedDelay(() -> {
            out(" scheduleWithFixedDelay 任务开始");
            sleep(3000);
            out(" scheduleWithFixedDelay 任务结束");
        }, 1, 2, TimeUnit.SECONDS);
        // pool.shutdown(); 不能shutdown，会取消上面的任务
        sleep(30000);
        pool.shutdown();
    }


}