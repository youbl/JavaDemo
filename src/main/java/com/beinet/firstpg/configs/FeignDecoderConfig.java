package com.beinet.firstpg.configs;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class FeignDecoderConfig {


    @Bean
    public Decoder feignDecoder() {
        return new ResponseEntityDecoder(new SpringDecoder(feignHttpMessageConverter()));
    }

    public ObjectFactory<HttpMessageConverters> feignHttpMessageConverter() {
        return () -> new HttpMessageConverters(new TextHtmlJackson2HttpMessageConverter());
    }

    /**
     * 增加text/html 的反序列化支持
     */
    public class TextHtmlJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
        TextHtmlJackson2HttpMessageConverter() {
            List<MediaType> mediaTypes = new ArrayList<>();
            mediaTypes.add(MediaType.valueOf(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8")); //关键
            setSupportedMediaTypes(mediaTypes);
        }
    }
}