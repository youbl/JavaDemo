package com.beinet.firstpg.configs;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeignInterceptor implements RequestInterceptor {

    /**
     * 实现接口 RequestInterceptor，在这个方法里添加全局Header
     * @param requestTemplate 拦截器收到的请求模板
     */
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("aaa", "111111")
                .header("bbb", "2222");
    }
}
