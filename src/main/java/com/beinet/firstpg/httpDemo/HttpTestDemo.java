package com.beinet.firstpg.httpDemo;

import com.beinet.firstpg.configs.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// 使用帮助 https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-feign.html
// 测试url  http://www.kuaidi100.com/query?type=yuantong&postid=11111111111
@FeignClient(name="httpTest", url="http://${beinet.jsonUrl}",
        configuration = { FeignConfiguration.class })
@Component
public interface HttpTestDemo {

    // Headers注解无效，需要跟@RequestLine一起使用 @Headers(value = {"Content-Type=application/json"})
    // 2种添加Header的方法,RequestMapping的headers参数，或方法参数前加 @RequestHeader，外部传入
    @RequestMapping(method = RequestMethod.GET, value="/query",
            headers = {"Content-Type=application/json",
            "Accept=application/json",
            "User-Agent=${beinet.userAgent}"})
    String DoGet(@RequestParam("type") String type, @RequestParam("postid") String pid,
                 @RequestHeader("authorize") String auth);


    @RequestMapping(method = RequestMethod.POST, value="/query",
    headers = "Content-type=application/x-www-form-urlencoded;charset=utf-8")
    String DoPost(@RequestParam("type") String type, @RequestParam("postid") String pid,
                  @RequestBody String body);


    /**
     * body用对象，会默认序列化为json格式，因此Content-Type要设置为json
     */
    @RequestMapping(method = RequestMethod.POST, value="/query",
            headers = "Content-type=application/json;charset=utf-8")
    String DoPost(@RequestParam("type") String type, @RequestParam("postid") String pid,
                  @RequestBody Map<String, ?> body);

}
