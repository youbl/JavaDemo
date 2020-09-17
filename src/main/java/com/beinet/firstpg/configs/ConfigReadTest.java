package com.beinet.firstpg.configs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@RefreshScope
@Data
public class ConfigReadTest {
    // 注：外部必须通过方法访问，如果是public这个field，外部访问会是null，读取不到配置值。
    @Value("${mike.app.iAmTest}")
    private String configField;
}
