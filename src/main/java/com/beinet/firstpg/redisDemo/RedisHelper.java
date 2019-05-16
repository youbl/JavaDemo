package com.beinet.firstpg.redisDemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisHelper {
    static RedisTemplate redis;

    /**
     * 构造函数进行静态注入
     * @param redis1 注入对象
     */
    @Autowired
    public RedisHelper(@Qualifier("redisTemplate") RedisTemplate redis1){
        // 不加这5行代码时，写入Redis的Key和Value，前缀都会出现 \xAC\xED\x00\x05t\x00\x03
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redis1.setKeySerializer(stringSerializer);
        redis1.setValueSerializer(stringSerializer);
        redis1.setHashKeySerializer(stringSerializer);
        redis1.setHashValueSerializer(stringSerializer);

        RedisHelper.redis = redis1;
    }


    /**
     * 读取指定key
     * @param key Redis的key
     * @return 字符串
     */
    public static String get(String key){
        Object ret = redis.opsForValue().get(key);
        if (ret == null) return null;
        return ret.toString();
    }

    /**
     * 删除指定key
     * @param key Redis的key
     * @return 成败
     */
    public static boolean remove(String key){
        return redis.delete(key);
    }



    /**
     * 写入指定key, 存在时覆盖
     * @param key Redis的key
     * @param val Redis的val
     */
    public static void set(String key, String val){
        redis.opsForValue().set(key, val);
    }


    /**
     * 写入指定key, 存在时覆盖
     * @param key Redis的key
     * @param val Redis的val
     * @param cacheSecond 缓存时长，单位秒
     */
    public static void set(String key, String val, int cacheSecond){
        redis.opsForValue().set(key, val, Duration.ofSeconds(cacheSecond));
    }



    /**
     * 写入指定key, 存在时 不覆盖
     * @param key Redis的key
     * @param val Redis的val
     * @return 写入成败
     */
    public static boolean add(String key, String val){
        return redis.opsForValue().setIfAbsent(key, val);
    }


    /**
     * 写入指定key, 存在时 不覆盖
     * @param key Redis的key
     * @param val Redis的val
     * @param cacheSecond 缓存时长，单位秒
     * @return 写入成败
     */
    public static boolean add(String key, String val, int cacheSecond){
        return redis.opsForValue().setIfAbsent(key, val, Duration.ofSeconds(cacheSecond));
    }

    /**
     * 读取指定key的旧值返回, 并写入新数据
     * @param key Redis的key
     * @param val Redis的val
     * @return 字符串
     */
    public static String getAndSet(String key, String val) {
        Object ret = redis.opsForValue().getAndSet(key, val);
        if (ret == null) return null;
        return ret.toString();
    }

}
