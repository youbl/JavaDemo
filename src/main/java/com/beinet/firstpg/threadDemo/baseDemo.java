package com.beinet.firstpg.threadDemo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;

public class baseDemo {

    static void method(int tmpIdx) {
        out("参数 " + tmpIdx + " 任务开始");
        sleep(1000);
        out("参数 " + tmpIdx + " 任务结束");
    }

    static int method2(int tmpIdx) {
        method(tmpIdx);
        return tmpIdx + 1;
    }

    static void out(Object obj) {
        // (System.currentTimeMillis() / 1000)
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        System.out.println(now + " " + Thread.currentThread().getId() + " " + obj);
    }

    static void waitPoolShutdown(ExecutorService pool) {
        // 必须先shutdown，才会Terminate
        pool.shutdown();
        // 阻塞，直到线程全部正常结束
        while (!pool.isTerminated()) {
            sleep(1000);
        }
    }

    static void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
