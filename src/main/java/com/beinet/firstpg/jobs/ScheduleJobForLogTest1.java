package com.beinet.firstpg.jobs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 演示日志级别的输出，在application.yml里配置当前类的日志级别
 */
@Component
@Slf4j // 日志对象注解
public class ScheduleJobForLogTest1 {
    /**
     * 每5秒执行一次的job
     */
    @Scheduled(cron = "*/5 * * * * *")
    public void logJob1() {
        String method = "ScheduleJobForLogTest1.logJob1";
        log.trace("{} 我是trace", method);
        log.debug("{} 我是debug", method);
        log.info("{} 我是info", method);
        log.warn("{} 我是warn", method);
        log.error("{} 我是error", method);
    }

}
