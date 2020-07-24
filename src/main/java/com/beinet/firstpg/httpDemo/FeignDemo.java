package com.beinet.firstpg.httpDemo;

import com.beinet.firstpg.configs.FeignConfiguration;
import com.fasterxml.jackson.databind.util.JSONPObject;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

// 使用帮助 https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-feign.html
// 测试url  http://www.kuaidi100.com/query?type=yuantong&postid=11111111111
@FeignClient(name="httpTest", url="http://${beinet.jsonUrl}",
        configuration = { FeignConfiguration.class })
@Component
public interface FeignDemo {

    // Headers注解无效，需要跟@RequestLine一起使用 @Headers(value = {"Content-Type=application/json"})
    // 但是RequestLine又需要进行特殊配置

    // 2种添加Header的方法,RequestMapping的headers参数，或方法参数前加 @RequestHeader，外部传入
    // 返回JSONObject，外部自行解析
    @RequestMapping(method = RequestMethod.GET, value="/query",
            headers = {"Content-Type=application/json",
            "Accept=application/json",
            "User-Agent=${beinet.userAgent}"})
    Map<String, Object> DoGet(@RequestParam("type") String type, @RequestParam("postid") String pid,
                     @RequestHeader("authorize") String auth);


    /**
     * 用自定义的body数据提交.
     * 返回自定义对象
     */
    @RequestMapping(method = RequestMethod.POST, value="/query",
    headers = "Content-type=application/x-www-form-urlencoded;charset=utf-8")
    HttpRet DoPost(@RequestParam("type") String type, @RequestParam("postid") String pid,
                  @RequestBody String body);


    /**
     * body用对象，会默认序列化为json格式，因此Content-Type要设置为json.
     * 返回自定义对象
     */
    @RequestMapping(method = RequestMethod.POST, value="/query",
            headers = "Content-type=application/json;charset=utf-8")
    HttpRet DoPost(@RequestParam("type") String type, @RequestParam("postid") String pid,
                  @RequestBody Map<String, ?> body);


    /**
     * 动态url测试
     */
    @RequestMapping(method = RequestMethod.POST, value="{aa}{path}",
            headers = "Content-type=application/json;charset=utf-8")
    @RequestLine("GET")
    String GetDynamicUrl(@PathVariable("aa") String xx,@PathVariable String path, @RequestParam("type") String type, @RequestParam("postid") String pid);
}
