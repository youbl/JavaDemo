package com.beinet.firstpg.redisDemo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.concurrent.Callable;

@Component
public class RedisHelper {
    static RedisTemplate redis;

    /**
     * 构造函数进行静态注入
     * @param redis1 注入对象
     */
    @Autowired
    public RedisHelper(@Qualifier("redisTemplate") RedisTemplate redis1){
        // Key用StringRedisSerializer，避免写入Redis的Key和Value，前缀都会出现 \xAC\xED\x00\x05t\x00\x03
        RedisSerializer keySerializer = new StringRedisSerializer();
        redis1.setKeySerializer(keySerializer);
        redis1.setHashKeySerializer(keySerializer);

        RedisSerializer valSerializer = new FastJsonRedisSerializer<>(Object.class);
        redis1.setValueSerializer(valSerializer);
        redis1.setHashValueSerializer(valSerializer);

        // 反序列化支持需要配置，否则会报错：autoType is not support.
        // 因为有过安全漏洞，不建议全局开启AutoType，https://www.jianshu.com/p/a92ecc33fd0d
        // ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        ParserConfig.getGlobalInstance().addAccept("com.beinet.");

        RedisHelper.redis = redis1;
    }

    // <editor-fold desc="字符类型存取操作">

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

    // </editor-fold>

    /**
     * 获取缓存，不存在时调用方法进行填充
     * @param key 缓存key
     * @param cacheSecond 缓存时长，单位秒
     * @param fillWhileNoFound 缓存不存在时的数据获取方法，用于填充缓存
     * @param cacheNull 数据获取方法返回null时，是否填充缓存
     * @param <T> 缓存数据类型
     * @return 缓存数据
     * @throws Exception 方法的异常
     */
    public static<T> T getCache(String key, int cacheSecond, Callable fillWhileNoFound, boolean cacheNull) throws Exception {
        Object ret = redis.opsForValue().get(key);
        if (ret != null)
            return (T) ret;
        ret = fillWhileNoFound.call();
        if (ret == null) {
            return null;
        }
        redis.opsForValue().set(key, ret, Duration.ofSeconds(cacheSecond));
        return (T) ret;
    }


    static class FastJsonRedisSerializer<T> implements RedisSerializer<T> {

        public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

        private Class<T> clazz;

        public FastJsonRedisSerializer(Class<T> clazz) {
            super();
            this.clazz = clazz;
        }

        @Override
        public byte[] serialize(T t) throws SerializationException {
            if (t == null) {
                return new byte[0];
            }
            return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
        }

        @Override
        public T deserialize(byte[] bytes) throws SerializationException {
            if (bytes == null || bytes.length <= 0) {
                return null;
            }
            String str = new String(bytes, DEFAULT_CHARSET);
            return (T) JSON.parseObject(str, clazz);
        }

    }
}
