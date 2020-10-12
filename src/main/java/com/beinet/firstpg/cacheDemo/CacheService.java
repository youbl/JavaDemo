package com.beinet.firstpg.cacheDemo;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
    private int num;

    public void initNum() {
        num = 0;
    }

    /**
     * 对返回值进行缓存
     * 注：无论key是否存在，都会执行方法体，并更新缓存后返回
     *
     * @return 缓存后的加1结果
     */
    @CachePut("num")
    public int putResult() {
        num++;
        return num;
    }

    /**
     * 对返回值进行缓存
     * 注：如果key已经存在，则不执行方法体，直接返回缓存。
     * 如果key不存在，则执行方法体，并缓存后返回
     *
     * @return 缓存后的加1结果
     */
    @Cacheable("num")
    public int getResult() {
        num++;
        return num;
    }

    /**
     * 清除指定的key缓存
     */
    @CacheEvict("num")
    public void clearNumCache() {
        // empty because it used to clear cache.
    }

    @Cacheable("numWithPara")
    public int getResult(int add) {
        num += add;
        return num;
    }

    @CacheEvict("numWithPara")
    public void clearNumCache(int add) {
        // empty because it used to clear cache.
    }
}
