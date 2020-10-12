package com.beinet.firstpg.cacheTest;

import com.beinet.firstpg.BaseTest;
import com.beinet.firstpg.cacheDemo.CacheService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CacheTest extends BaseTest {
    @Autowired
    CacheService cacheService;

    @Autowired
    CacheManager cacheManager;

    // 无参方法缓存测试
    @Test
    public void TestCacheNoPara() {
        // 未引用 spring-boot-starter-data-redis 时，或在application.yml里设置 spring.cache.type: simple 后
        // SpringBoot会使用 ConcurrentMapCacheManager 把数据缓存在 内存 里，
        //   该Manager不支持过期策略，也不推荐分布式系统使用它
        // 如果引用了 spring-boot-starter-data-redis，且未指定 spring.cache.type
        //   则Manager自动变成 org.springframework.data.redis.cache.RedisCacheManager
        Assert.assertEquals(cacheManager.getClass().getName(), "org.springframework.cache.concurrent.ConcurrentMapCacheManager");

        cacheService.initNum();

        int num = cacheService.getResult();
        Assert.assertEquals(num, 1);

        // 因为缓存了，所以不会执行getResult方法体，结果还是1，不是2
        num = cacheService.getResult();
        Assert.assertEquals(num, 1);

        // 清缓存
        cacheService.clearNumCache();

        num = cacheService.getResult();
        Assert.assertEquals(num, 2);

        num = cacheService.putResult();
        Assert.assertEquals(num, 3);
        num = cacheService.putResult();
        Assert.assertEquals(num, 4);

        num = cacheService.getResult();
        Assert.assertEquals(num, 4);
    }


    // 有参方法缓存测试
    @Test
    public void TestCacheWithPara() {
        cacheService.initNum();

        int para1 = 5;
        int para2 = 7;
        int num1 = cacheService.getResult(para1);
        Assert.assertEquals(num1, 5);
        // 因为Cacheable会根据参数缓存，因此下一行会执行方法，返回12
        int num2 = cacheService.getResult(para2);
        Assert.assertEquals(num2, 12);

        // 根据参数存在缓存了，所以不会执行getResult方法体
        num1 = cacheService.getResult(para1);
        Assert.assertEquals(num1, 5);
        num2 = cacheService.getResult(para2);
        Assert.assertEquals(num2, 12);

        // 清理para2的缓存
        cacheService.clearNumCache(para2);

        // para1的缓存还在
        num1 = cacheService.getResult(para1);
        Assert.assertEquals(num1, 5);
        // para2的缓存没了，因此执行方法体
        num2 = cacheService.getResult(para2);
        Assert.assertEquals(num2, 19);
    }
}
