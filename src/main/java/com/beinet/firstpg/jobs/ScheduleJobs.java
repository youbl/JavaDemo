package com.beinet.firstpg.jobs;

import com.beinet.firstpg.configs.ConfigReader;
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

    private final ConfigReader configs;

    @Autowired
    public ScheduleJobs(ConfigReader configs) {
        this.configs = configs;
    }

    /**
     * 每秒执行一次的job
     */
    @Scheduled(cron="* * * * * *")
    public void firstJob() {
        if (runed) return;
        runed = true;

        outputConfigs();
    }

    /**
     * 读取所有配置数据，并输出
     */
    private void outputConfigs(){
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
                    msg.append("长度:").append(j).append("  ");
                    for (int i = 0; i < j; i++) {
                        msg.append(Array.get(obj, i)).append(",");
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

}
