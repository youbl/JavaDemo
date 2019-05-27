package com.beinet.firstpg.redisTest;


import com.beinet.firstpg.BaseTest;
import com.beinet.firstpg.redisDemo.RedisHelper;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Serializable;
import java.time.LocalDateTime;

// RedisHelper要注入，所以要用 SpringBootTest
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest  extends RedisTestNoSpring {

    /**
     * 测试正常的Redis读写（永不过期）
     */
    @Test
    public void TestStr(){
        super.TestStr();
    }


    /**
     * 测试带过期的Redis读写
     */
    @Test
    public void TestStrExpired() throws InterruptedException {
        super.TestStrExpired();
    }


    @Test
    public void TestCache() throws Exception {
        super.TestCache();
    }
}
