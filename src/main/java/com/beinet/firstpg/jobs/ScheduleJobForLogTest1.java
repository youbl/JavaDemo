package com.beinet.firstpg.jobs;

import com.beinet.firstpg.configs.ConfigReadTest;
import com.beinet.firstpg.configs.ConfigReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 演示日志级别的输出，在application.yml里配置当前类的日志级别
 */
@Component
@Slf4j // 日志对象注解
public class ScheduleJobForLogTest1 {

    // 当修改配置中心的配置值，并调用了/actuator/refresh时，Value注解的配置不生效。
    // 必须在class上添加注解 RefreshScope，才会生效，但是 RefreshScope会导致 当前类的Scheduled停止运行
    @Value("${" + ConfigReadTest.configName + "}")
    private String configField;

    @Autowired
    ConfigReadTest readTest;

    /**
     * 每5秒执行一次的job
     */
     @Scheduled(cron = "*/5 * * * * *")
    public void logJob1() {
        // 这个方法读取配置，会实时生效
        String config = ConfigReader.getConfig(ConfigReadTest.configName);

        System.out.println(configField + "\n" + config + "\n" + readTest.getConfigField());

//        String method = "ScheduleJobForLogTest1.logJob1";
//        log.trace("{} 我是trace", method);
//        log.debug("{} 我是debug", method);
//        log.info("{} 我是info", method);
//        log.warn("{} 我是warn", method);
//        log.error("{} 我是error", method);
    }

}
