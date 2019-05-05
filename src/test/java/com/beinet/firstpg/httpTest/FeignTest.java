package com.beinet.firstpg.httpTest;

import com.alibaba.fastjson.JSONObject;
import com.beinet.firstpg.BaseTest;
import com.beinet.firstpg.httpDemo.FeignDemo;
import com.beinet.firstpg.httpDemo.HttpRet;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FeignTest extends BaseTest {
    @Autowired
    private FeignDemo feignDemo;

    @Test
    public void TestGet(){
        // 验证get方法
        JSONObject obj = feignDemo.DoGet("yuantong", "111111", "youbl");
        out(obj.toJSONString());
        Assert.assertNotEquals(obj, null);
    }

    @Test
    public void TestPost(){
//        // 验证普通post方法
        HttpRet httpRet = feignDemo.DoPost("yuantong", "111111", "a=1&b=2");
        Assert.assertNotEquals(httpRet, null);
        out(httpRet.toString());

        // post对象
        Map<String, String> para = new HashMap<>();
        para.put("a", "123");
        para.put("b", "abc");
        HttpRet obj = feignDemo.DoPost("yuantong", "111111", para);
        Assert.assertNotEquals(obj, null);
        out(obj.toString());
    }

    @Test
    public void TestDynamicUrl() throws URISyntaxException {
        String ret = feignDemo.GetDynamicUrl("/", "query", "yuantong", "111111");
        out(ret);

        // will fail: java.lang.IllegalArgumentException: url values must be not be absolute.
        ret = feignDemo.GetDynamicUrl("http://10.2.3.43/a.aspx", "", "yuantong", "111111");
        out(ret);
    }
}
