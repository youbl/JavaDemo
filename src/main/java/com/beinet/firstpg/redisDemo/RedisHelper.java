package com.beinet.firstpg.redisDemo;

import com.beinet.firstpg.configs.ConfigHelper;
import com.beinet.firstpg.serializeDemo.SerializeHelper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.Callable;

@Component
public class RedisHelper {
    static RedisTemplate redis;

    static {
        RedisHelper.redis = initConnection();
    }

    /**
     * 构造函数进行静态注入
     *
     * @param redis1 注入对象
     */
    @Autowired
    public RedisHelper(@Qualifier("redisTemplate") RedisTemplate redis1) {
        if (redis1 == null || redis != null)
            return;
        initSerializer(redis1);

        RedisHelper.redis = redis1;
    }

    // <editor-fold desc="字符类型存取操作">

    /**
     * 读取指定key
     *
     * @param key Redis的key
     * @return 字符串
     */
    public static String get(String key) {
        Object ret = redis.opsForValue().get(key);
        if (ret == null) return null;
        return ret.toString();
    }

    /**
     * 删除指定key
     *
     * @param key Redis的key
     * @return 成败
     */
    public static boolean remove(String key) {
        return redis.delete(key);
    }


    /**
     * 写入指定key, 存在时覆盖
     *
     * @param key Redis的key
     * @param val Redis的val
     */
    public static void set(String key, String val) {
        redis.opsForValue().set(key, val);
    }


    /**
     * 写入指定key, 存在时覆盖
     *
     * @param key         Redis的key
     * @param val         Redis的val
     * @param cacheSecond 缓存时长，单位秒
     */
    public static void set(String key, String val, int cacheSecond) {
        redis.opsForValue().set(key, val, Duration.ofSeconds(cacheSecond));
    }


    /**
     * 写入指定key, 存在时 不覆盖
     *
     * @param key Redis的key
     * @param val Redis的val
     * @return 写入成败
     */
    public static boolean add(String key, String val) {
        return redis.opsForValue().setIfAbsent(key, val);
    }


    /**
     * 写入指定key, 存在时 不覆盖
     *
     * @param key         Redis的key
     * @param val         Redis的val
     * @param cacheSecond 缓存时长，单位秒
     * @return 写入成败
     */
    public static boolean add(String key, String val, int cacheSecond) {
        return redis.opsForValue().setIfAbsent(key, val, Duration.ofSeconds(cacheSecond));
    }

    /**
     * 读取指定key的旧值返回, 并写入新数据
     *
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
     *
     * @param key              缓存key
     * @param cacheSecond      缓存时长，单位秒
     * @param fillWhileNoFound 缓存不存在时的数据获取方法，用于填充缓存
     * @param cacheNull        数据获取方法返回null时，是否填充缓存
     * @param <T>              缓存数据类型
     * @return 缓存数据
     * @throws Exception 方法的异常
     */
    public static <T> T getCache(String key, int cacheSecond, Callable fillWhileNoFound, boolean cacheNull) throws Exception {
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

        private Class<T> clazz;

        public FastJsonRedisSerializer(Class<T> clazz) {
            super();
            this.clazz = clazz;
        }

        @Override
        public byte[] serialize(T t) throws SerializationException {
            return SerializeHelper.serialize(t);
        }

        @Override
        public T deserialize(byte[] bytes) throws SerializationException {
            return SerializeHelper.deserialize(bytes, clazz);
        }

    }


    public static RedisTemplate initConnection() {
        // 单实例redis
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        // 哨兵redis 用 new RedisSentinelConfiguration();
        // 集群redis 用 new RedisClusterConfiguration();

        String host = ConfigHelper.getConfig("spring.redis.host");
        String pwd = ConfigHelper.getConfig("spring.redis.password");
        int port = ConfigHelper.getInt("spring.redis.port", 6379);
        int db = ConfigHelper.getInt("spring.redis.database", 0);
        int timeout = ConfigHelper.getInt("spring.redis.timeout", 1000);
        boolean useSsl = ConfigHelper.getBool("spring.redis.ssl");

        redisConfig.setHostName(host);
        redisConfig.setPassword(RedisPassword.of(pwd));
        redisConfig.setPort(port);
        redisConfig.setDatabase(db);

        int maxActive = ConfigHelper.getInt("spring.redis.jedis.pool.max-active", 8);
        int maxWait = ConfigHelper.getInt("spring.redis.jedis.pool.max-wait", -1);
        int maxIdel = ConfigHelper.getInt("spring.redis.jedis.pool.max-idle", 8);
        int minIdel = ConfigHelper.getInt("spring.redis.jedis.pool.min-idle", 0);

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMaxIdle(maxIdel);
        poolConfig.setMaxWaitMillis(maxWait);
        poolConfig.setMinIdle(minIdel);

        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration
                .builder()
                .poolConfig(poolConfig)
                .commandTimeout(Duration.ofMillis(timeout));
        if (useSsl)
            builder.useSsl();
        LettuceClientConfiguration config = builder.build();

        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisConfig, config);
        factory.afterPropertiesSet();

        RedisTemplate redis = new RedisTemplate();
        redis.setConnectionFactory(factory);
        redis.afterPropertiesSet();

        initSerializer(redis);
        return redis;
    }

    private static void initSerializer(RedisTemplate redis1) {
        // Key用StringRedisSerializer，避免写入Redis的Key和Value，前缀都会出现 \xAC\xED\x00\x05t\x00\x03
        RedisSerializer keySerializer = new StringRedisSerializer();
        redis1.setKeySerializer(keySerializer);
        redis1.setHashKeySerializer(keySerializer);

        RedisSerializer valSerializer = new FastJsonRedisSerializer<>(Object.class);
        redis1.setValueSerializer(valSerializer);
        redis1.setHashValueSerializer(valSerializer);
    }
}
