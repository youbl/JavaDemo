package com.beinet.firstpg.redisTest;


import com.beinet.firstpg.BaseTest;
import com.beinet.firstpg.redisDemo.RedisHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

// RedisHelper要注入，所以要用 SpringBootTest
@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest  extends BaseTest {

    /**
     * 测试正常的Redis读写（永不过期）
     */
    @Test
    public void TestStr(){
        String key1 = "key1";
        String key2 = "key2";

        boolean bool = RedisHelper.remove(key1);
        out(bool);
        bool = RedisHelper.remove(key2);
        out(bool);

        String ret = RedisHelper.get(key1);
        Assert.assertEquals(ret, null);
        out(ret);

        ret = RedisHelper.getAndSet(key1, "123");
        Assert.assertEquals(ret, null);
        out(ret);

        ret = RedisHelper.getAndSet(key1, "456");
        Assert.assertEquals(ret, "123");
        out(ret);

        bool = RedisHelper.add(key1, "789");
        Assert.assertEquals(bool, false);
        out(bool);
        bool = RedisHelper.add(key2, "789");
        Assert.assertEquals(bool, true);
        out(bool);
    }


    /**
     * 测试带过期的Redis读写
     */
    @Test
    public void TestStrExpired() throws InterruptedException {
        String key1 = "key1";
        String key2 = "key2";

        boolean bool = RedisHelper.remove(key1);
        out(bool);
        bool = RedisHelper.remove(key2);
        out(bool);

        String ret = RedisHelper.get(key1);
        Assert.assertEquals(ret, null);
        out(ret);

        RedisHelper.set(key1, "123", 5);
        ret = RedisHelper.get(key1);
        Assert.assertEquals(ret, "123");
        out(ret);
        Thread.sleep(5000);
        ret = RedisHelper.get(key1);
        Assert.assertEquals(ret, null);
        out(ret);

        bool = RedisHelper.add(key1, "123", 5);
        Assert.assertEquals(bool, true);
        out(bool);
        ret = RedisHelper.get(key1);
        Assert.assertEquals(ret, "123");
        out(ret);
        Thread.sleep(5000);
        ret = RedisHelper.get(key1);
        Assert.assertEquals(ret, null);
        out(ret);

    }
}
