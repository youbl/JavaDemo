package com.beinet.firstpg.configs;

import feign.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration  {

    // 也可以在配置里指定，2边都配置了，以此类为准
//    /**
//     * 指示记录日志的级别，FULL为记录所有
//     * @return 日志级别
//     */
//    @Bean
//    Logger.Level feignLoggerLevel() {
//        return Logger.Level.NONE;
//    }

    // 可以开启这个方法设置超时，也可以在配置里指定
//    /**
//     * 设置全局请求的超时时间,毫秒
//     * @return
//     */
//    @Bean
//    public Request.Options options() {
//        return new Request.Options(100, 100);
//    }
//
//    /**
//     * 指定是否重试
//     * @return
//     */
//    @Bean
//    public Retryer feignRetryer() {
//        return new Retryer.Default();
//    }
}
