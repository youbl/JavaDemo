package com.beinet.firstpg.dynamicProxy;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProxyDemoController {
    @GetMapping("/proxy1")
    public String t1() {
        // 测试代理【接口】
        DemoInf proxy = DemoInvocationHandler.getProxy(DemoInf.class);
        return proxy.now(123, "abc");//String.valueOf(System.nanoTime());
    }

    @GetMapping("/proxy2")
    public String t2() {
        // 测试代理【类】
        DemoInf proxy = DemoInvocationHandler.getProxy(DemoInfImplment.class);
        return proxy.now(123, "abc");//String.valueOf(System.nanoTime());
    }

    @GetMapping("/proxy3")
    public String t3() {
        // 测试代理【类实例】
        DemoInf proxy = DemoInvocationHandler.getProxy(new DemoInfImplment());
        return proxy.now(123, "abc");//String.valueOf(System.nanoTime());
    }
}
