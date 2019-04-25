package com.beinet.firstpg.jobs;

import com.beinet.firstpg.configs.ConfigReader;
import com.beinet.firstpg.mysql.MySqlTest;
import com.beinet.firstpg.mysql.entity.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 * 演示计划任务和配置读取
 */
@Component
@Slf4j
public class ScheduleJobs {
    private boolean runed;

    @Autowired
    private ConfigReader configs;

    @Autowired
    private MySqlTest mySqlTest;

    /**
     * 每秒执行一次的job
     */
    @Scheduled(cron="* * * * * *")
    public void firstJob(){
        if(runed)return;runed=true;

        outputConfigs();

        testMySql();


    }

    /**
     * 读取所有配置数据，并输出
     */
    void outputConfigs(){
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
        Users user = mySqlTest.AddUser(account, "水边", "123456");
        log.info(user.toString());
    }
}
