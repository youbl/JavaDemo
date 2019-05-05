package com.beinet.firstpg.jobs;

import com.beinet.firstpg.configs.ConfigReader;
import com.beinet.firstpg.httpDemo.FeignDemo;
import com.beinet.firstpg.mysql.MySqlTest;
import com.beinet.firstpg.mysql.entity.Users;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 * 演示计划任务和配置读取.
 * 注意：在此类上添加注解： @EnableScheduling
 * 也可以添加在main函数的类上
 */
@Component
@Slf4j // 日志对象注解
@EnableScheduling
public class ScheduleJobs {
    private boolean runed;

    @Autowired
    private ConfigReader configs;

    @Autowired
    private MySqlTest mySqlTest;

    @Autowired
    private FeignDemo httpTest;


    /**
     * 每秒执行一次的job
     */
    @Scheduled(cron="* * * * * *")
    public void firstJob() {
        if (runed) return;
        runed = true;

        outputConfigs();

//        testMySql();
    }

    /**
     * 读取所有配置数据，并输出
     */
    void outputConfigs(){
        log.info("spring.application.name : " + configs.getConfig("spring.application.name"));
        log.info("not exists : " + configs.getConfig("not.exists"));

       /*
        getFields()只能获取public的字段，包括父类的。
        getDeclaredFields()只能获取自己声明的各种字段，包括public，protected，private。
        */
        for(Field field : configs.getClass().getFields()) {
            log.info(field.getName() + " -> " + field.toString());
            try {
                Object obj = field.get(configs);
                if (obj == null) {
                    log.info("    is null.");
                    continue;
                }
                if (obj.getClass().isArray()) {
                    StringBuilder msg = new StringBuilder();
                    // 反射遍历数组
                    int j = Array.getLength(obj);
                    msg.append("长度:" + String.valueOf(j) + "  ");
                    for (int i = 0; i < j; i++) {
                        msg.append(Array.get(obj, i) + ",");
                    }
                    log.info("    " + msg);
                } else {
                    log.info("    " + obj.toString());
                }
            } catch (Exception exp) {
                log.error(exp.toString());
            }
        }
    }

    void testMySql(){
        // 插入数据
        String account = "ybl" + Math.round(Math.random() * 1000000);
        try {
            Users user = mySqlTest.AddUser(account, "水边", "123456");

            // 在类上添加注解 @Slf4j，就自动注入一个log成员，可以用于记录日志，默认输出到控制台.
            // 再在resources目录下添加一个logback-spring.xml，就会写入文件日志
            log.info(user.toString());
        }catch (Exception exp){
            // 记录错误级别日志
            log.error(exp.toString());
        }
    }
}
