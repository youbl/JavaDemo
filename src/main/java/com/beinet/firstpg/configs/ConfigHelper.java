package com.beinet.firstpg.configs;

import com.beinet.firstpg.fileDemo.FileHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;

/**
 * 自定义Yml配置读取类
 */
public class ConfigHelper {
    /**
     * resources下的资源路径
     */
    private static String configFile = "/application.yml";
    private static Map<String, String> configs;

    static {
        try (InputStream is = ConfigHelper.class.getResourceAsStream(configFile)) {
            String yml = FileHelper.read(is);
            configs = YmlHelper.parse(yml);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回所有配置
     *
     * @return 配置
     */
    public static Map<String, String> getConfigs() {
        return configs;
    }

    /**
     * 通过 System 类读取配置
     *
     * @param configName   配置名，如 "spring.application.name"
     * @param defaultValue 为空时的默认值
     * @return 配置值
     */
    public static String getConfig(String configName, String defaultValue) {
        return configs.getOrDefault(configName, defaultValue);
    }

    /**
     * 通过 System 类读取配置
     *
     * @param configName 配置名，如 "spring.application.name"
     * @return 配置值
     */
    public static String getConfig(String configName) {
        return getConfig(configName, "");
    }

    /**
     * 读取int值，为空时返回0
     *
     * @param configName 配置名
     * @return 整数配置值
     */
    public static int getInt(String configName) {
        return getInt(configName, 0);
    }

    /**
     * 读取int值，为空时返回defaultValue
     *
     * @param configName   配置名
     * @param defaultValue 为空时的默认值
     * @return 整数配置值
     */
    public static int getInt(String configName, int defaultValue) {
        String str = getConfig(configName);
        if (str == null || (str = str.trim()).length() == 0)
            return defaultValue;
        return Integer.parseInt(str);
    }


    /**
     * 读取int值，为空时返回false
     *
     * @param configName 配置名
     * @return 整数配置值
     */
    public static boolean getBool(String configName) {
        return getBool(configName, false);
    }

    /**
     * 读取int值，为空时返回defaultValue
     *
     * @param configName   配置名
     * @param defaultValue 为空时的默认值
     * @return 整数配置值
     */
    public static boolean getBool(String configName, boolean defaultValue) {
        String str = getConfig(configName);
        if (str == null || (str = str.trim()).length() == 0)
            return defaultValue;
        return str.equals("1") || str.toLowerCase().equals("true");
    }

}
