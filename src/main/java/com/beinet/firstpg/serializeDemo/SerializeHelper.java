package com.beinet.firstpg.serializeDemo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.IOException;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
// import java.nio.charset.Charset;

public final class SerializeHelper {
    private SerializeHelper() {

    }

    // public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private static final ObjectMapper mapper;
    private static final String pattern = "yyyy-MM-dd HH:mm:ss";

    static {
        mapper = new ObjectMapper();

//        JavaTimeModule javaTimeModule = new JavaTimeModule();
//        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
//        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
//        mapper.registerModule(javaTimeModule);

        // 解决 com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Cannot construct instance of `java.time.LocalDateTime`
        // 自动支持JsonFormat注解和各种日期格式, 要引入 jackson-datatype-jsr310
         mapper.registerModule(new JavaTimeModule());

        /*
        注意：如果报错：java.lang.NoClassDefFoundError: com/fasterxml/jackson/databind/ser/std/ToStringSerializerBase
        请确保引入了 jackson-databind
        如果报错：java.lang.NoClassDefFoundError: com/fasterxml/jackson/core/exc/InputCoercionException
        请确保引入了 jackson-core
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-core</artifactId>
            <version>2.11.1</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.11.0</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.datatype</groupId>
            <artifactId>jackson-datatype-jsr310</artifactId>
            <version>2.11.1</version>
        </dependency>
        * */
    }

    public static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.format(DateTimeFormatter.ofPattern(pattern)));
        }
    }

    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException {
            return LocalDateTime.parse(p.getValueAsString(), DateTimeFormatter.ofPattern(pattern));
        }
    }

    /**
     * 序列化为字符串
     *
     * @param obj 对象
     * @return 字符串
     * @throws JsonProcessingException 异常
     */
    public static String serializeToStr(Object obj) throws JsonProcessingException {
        if (obj == null) {
            return null;
        }
        return mapper.writeValueAsString(obj);
    }


    /**
     * 从字符串反序列化为对象
     *
     * @param jsonStr  字符串
     * @param objClass 对象类型
     * @param <T>      对象类
     * @return 对象
     * @throws IOException 异常
     */
    public static <T> T deserialize(String jsonStr, Class<T> objClass) throws IOException {
        if (jsonStr == null || jsonStr.length() <= 0) {
            return null;
        }
        return mapper.readValue(jsonStr, objClass);
    }

    /**
     * 从字节数组反序列化为对象数组
     *
     * @param jsonStr  字符串
     * @param objClass 对象类型
     * @param <T>      对象类
     * @return 对象数组
     * @throws IOException 异常
     */
    public static <T> List<T> deserializeToArr(String jsonStr, Class<T> objClass) throws IOException {
        if (jsonStr == null || jsonStr.length() <= 0) {
            return null;
        }
        JavaType type = mapper.getTypeFactory().constructParametricType(List.class, objClass);
        return mapper.readValue(jsonStr, type);
    }

    /**
     * 序列化为字节数组
     *
     * @param obj 对象
     * @return 字节数组
     * @throws JsonProcessingException 异常
     */
    public static byte[] serialize(Object obj) throws JsonProcessingException {
        if (obj == null) {
            return null;
        }
        return mapper.writeValueAsBytes(obj);
        // String str = serializeToStr(obj);
        // return str.getBytes(DEFAULT_CHARSET);
    }

    /**
     * 从字节数组反序列化为对象
     *
     * @param bytes    字节数组
     * @param objClass 对象类型
     * @param <T>      对象类
     * @return 对象
     * @throws IOException 异常
     */
    public static <T> T deserialize(byte[] bytes, Class<T> objClass) throws IOException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        return mapper.readValue(bytes, objClass);
        // String str = new String(bytes, DEFAULT_CHARSET);
        // return deserialize(str, objClass);
    }


    /**
     * 从字节数组反序列化为对象数组
     *
     * @param bytes    字节数组
     * @param objClass 对象类型
     * @param <T>      对象类
     * @return 对象数组
     * @throws IOException 异常
     */
    public static <T> List<T> deserializeToArr(byte[] bytes, Class<T> objClass) throws IOException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        JavaType type = mapper.getTypeFactory().constructParametricType(List.class, objClass);
        return mapper.readValue(bytes, type);
    }
}
