package com.beinet.firstpg.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

/**
 * 用于获取前端提供的日期数据，可以正确反序列化为LocalDateTime
 * 注意：无法把不带时间的字符串转换为 LocalDateTime，
 * StackOverflow上的答案都是建议先转换为 LocalDate，再二次转换为 LocalDateTime
 */
@Configuration
public class LocalDateTimeSerializerConfig {
    @Bean
    public ObjectMapper getMapper() {
        JavaTimeModule module = new JavaTimeModule();
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeSerializerBeinet(getDateTimeFormatter()));

        return Jackson2ObjectMapperBuilder.json().modules(module)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).build();
    }

    // 支持的格式有
    // "2020-12-31 23:58:59"
    // "2020-12-31T23:58:59"
    private DateTimeFormatter getDateTimeFormatter() {
        // return DateTimeFormatter.ofPattern("yyyy-MM-dd[[ ]['T']HH:mm:ss]");
        return new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(ISO_LOCAL_DATE)  // 这个要求4位年，2位月，2位日
                .optionalStart().appendLiteral(' ').optionalEnd()       // ' ' made optional
                .optionalStart().appendLiteral('T').optionalEnd()       // 'T' made optional
                .optionalStart().append(ISO_LOCAL_TIME).optionalEnd()   // time made optional
                .optionalStart().appendOffsetId().optionalEnd() // zone and offset made optional
                .optionalStart().appendOffset("+HHMM", "Z").optionalEnd()                //为了兼容时区不带:这种格式

                .optionalStart()
                .appendLiteral('[')
                .parseCaseSensitive()
                .appendZoneRegionId()
                .appendLiteral(']')
                .optionalEnd()
                .toFormatter();
    }
}

