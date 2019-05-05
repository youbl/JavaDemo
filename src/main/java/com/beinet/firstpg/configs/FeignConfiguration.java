package com.beinet.firstpg.configs;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import feign.*;
import feign.codec.ErrorDecoder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class FeignConfiguration  {

    @Bean
    public ErrorDecoder errorDecoder(){
        return (methodKey, response)->{
            int status = response.status();
            if (status >= 300 && status < 400) {
//                String body = "Redirect found";
//                try {
//                    body = IOUtils.toString(response.body().asReader());
//                } catch (Exception ignored) {}
//                HttpHeaders httpHeaders = new HttpHeaders();
//                response.headers().forEach((k, v) -> httpHeaders.add("feign-" + k, StringUtils.join(v,',')));
//                return new FeignBadResponseWrapper(status, httpHeaders, body);
            }
            return feign.FeignException.errorStatus(methodKey, response);
        };
    }

    @Data
    @EqualsAndHashCode(callSuper=false)
    static class FeignBadResponseWrapper extends HystrixBadRequestException {
        private final int status;
        private final HttpHeaders headers;
        private final String body;

        public FeignBadResponseWrapper(int status, HttpHeaders headers, String body) {
            super(body);
            this.status = status;
            this.headers = headers;
            this.body = body;
        }
    }
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
