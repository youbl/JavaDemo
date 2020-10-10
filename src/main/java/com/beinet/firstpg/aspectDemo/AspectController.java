package com.beinet.firstpg.aspectDemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class AspectController {

    @GetMapping("/aspect1")
    @NeedLog("这是AspectController的testAspect方法")
    public String testAspect() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " OK beinet!";
    }


    @GetMapping("/aspect2")
    @NeedLog("出异常的测试")
    public int testAspec2t() {
        return Integer.parseInt("abc");
    }

    @GetMapping("/aspect3")
    @NeedLog("没有返回值的测试")
    public void noReturnTest(@RequestParam int idx) {
    }
}
