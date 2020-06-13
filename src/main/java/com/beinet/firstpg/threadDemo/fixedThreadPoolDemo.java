package com.beinet.firstpg.threadDemo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 固定线程池的使用Demo
 */
public class fixedThreadPoolDemo extends baseDemo {

    /**
     * 不需要返回值的固定线程池测试
     */
    public void FixedThreadPoolTest1() {
        ExecutorService pool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            int tmpIdx = i;
            pool.execute(() -> method(tmpIdx));
        }
        waitPoolShutdown(pool);
    }

    /**
     * 通过返回值阻塞线程的执行，直接完毕再退出
     */
    public void FixedThreadPoolTest2() {
        List<Future> arrFuture = new ArrayList<>();
        ExecutorService pool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            int tmpIdx = i;
            arrFuture.add(pool.submit(() -> method(tmpIdx)));
        }
        for (Future item : arrFuture) {
            try {
                out("线程结束结果：" + item.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        pool.shutdown();
    }

    /**
     * 带返回值的线程池操作
     */
    public void FixedThreadPoolTest3() {
        List<Future<Integer>> arrFuture = new ArrayList<>();
        ExecutorService pool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            int tmpIdx = i;
            Future<Integer> ret = pool.submit(() -> method2(tmpIdx));
            arrFuture.add(ret);
        }
        for (Future<Integer> item : arrFuture) {
            try {
                out("线程结束结果：" + item.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        pool.shutdown();
    }


}