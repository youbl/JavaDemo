package com.beinet.firstpg;

import com.beinet.firstpg.threadDemo.cacheThreadPoolDemo;
import com.beinet.firstpg.threadDemo.fixedThreadPoolDemo;
import com.beinet.firstpg.threadDemo.scheduledThreadPoolDemo;
import com.beinet.firstpg.threadDemo.threadLockDemo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.IOException;
import java.util.concurrent.Callable;

@EnableSwagger2  // 根据SpringBootApplication的 scanBasePackages参数配置，进行扫描，默认扫描当前类所在package，就是 com.beinet.firstpg
@SpringBootApplication(exclude = MongoAutoConfiguration.class) // (scanBasePackages = "com.beinet.firstpg.controller.demo")
@EnableFeignClients
@EnableCaching
public class BeinetFirstpgApplication {

    public static void main(String[] args) {
        //testPool();
        //System.out.println("程序退出");

        // testExcelHelper();
        SpringApplication.run(BeinetFirstpgApplication.class, args);
    }

    static void testPool() {
//        fixedThreadPoolDemo demo = new fixedThreadPoolDemo();
//        demo.FixedThreadPoolTest1();
//        demo.FixedThreadPoolTest2();
//        demo.FixedThreadPoolTest3();

//        scheduledThreadPoolDemo demo2 = new scheduledThreadPoolDemo();
//        demo2.ScheduledThreadPoolTest1();
//        demo2.ScheduledThreadPoolTest2();
//        demo2.ScheduledThreadPoolTest3();

//        cacheThreadPoolDemo demo3 = new cacheThreadPoolDemo();
//        demo3.cacheThreadPoolTest1();

        threadLockDemo demo4 = new threadLockDemo();
        printTime(() -> demo4.noLockThreadTest1());
        printTime(() -> demo4.noLockThreadTest1());
        printTime(() -> demo4.lockThreadTest1());
        printTime(() -> demo4.lockThreadTest2());
        printTime(() -> demo4.lockThreadTest3());
        printTime(() -> demo4.lockThreadTest4());

    }

    static void testExcelHelper() throws IOException {
        //System.out.println(com.beinet.firstmaven.ExcelHelper.readExcel("D:\\work\\1.xlsx", 1, 0));
    }

    static <T> T printTime(Callable<T> task) {
        try {
            long startTime = System.currentTimeMillis();
            T ret = task.call();
            System.out.println("执行耗时：" + (System.currentTimeMillis() - startTime) / 1000F + "秒");
            return ret;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    static void printTime(Runnable task) {
        try {
            long startTime = System.currentTimeMillis();
            task.run();
            System.out.println("执行耗时：" + (System.currentTimeMillis() - startTime) / 1000F + "秒");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
