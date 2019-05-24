package com.beinet.firstpg.serializeDemo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

public class SerializeHelper {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    static {
        // 反序列化支持需要配置，否则会报错：autoType is not support.
        // 因为有过安全漏洞，不建议全局开启AutoType，https://www.jianshu.com/p/a92ecc33fd0d
        // ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        ParserConfig.getGlobalInstance().addAccept("com.beinet.");
    }

    /**
     * 序列化为字符串
     * @param obj 对象
     * @return 字符串
     * @throws SerializationException 异常
     */
    public static String serializeToStr(Object obj) throws SerializationException {
        if (obj == null) {
            return null;
        }
        return JSON.toJSONString(obj, SerializerFeature.WriteClassName);
    }


    /**
     * 从字符串反序列化为对象
     * @param jsonStr 字符串
     * @param objClass 对象类型
     * @param <T> 对象类
     * @return 对象
     * @throws SerializationException 异常
     */
    public static<T> T deserialize(String jsonStr, Class<T> objClass) throws SerializationException {
        if (jsonStr == null) {
            return null;
        }
        return (T) JSON.parseObject(jsonStr, objClass);
    }

    /**
     * 序列化为字节数组
     * @param obj 对象
     * @return 字节数组
     * @throws SerializationException 异常
     */
    public static byte[] serialize(Object obj) throws SerializationException {
        String str = serializeToStr(obj);
        if (str == null) {
            return new byte[0];
        }
        return str.getBytes(DEFAULT_CHARSET);
    }

    /**
     * 从字节数组反序列化为对象
     * @param bytes 字节数组
     * @param objClass 对象类型
     * @param <T> 对象类
     * @return 对象
     * @throws SerializationException 异常
     */
    public static<T> T deserialize(byte[] bytes, Class<T> objClass) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);
        return deserialize(str, objClass);
    }
}
