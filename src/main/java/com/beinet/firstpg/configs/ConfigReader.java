package com.beinet.firstpg.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigReader {
//    @Autowired
//    org.springframework.core.env.Environment env;

    /**
     * 通过 System 类读取配置
     * @param configName 配置名，如 "spring.application.name"
     * @param defaultValue 为空时的默认值
     * @return 配置值
     */
    public String getConfig(String configName, String defaultValue) {
        // env.getProperty(k) 等效于 System.getProperty(k)
        return System.getProperty(configName, defaultValue);
    }
    /**
     * 通过 System 类读取配置
     * @param configName 配置名，如 "spring.application.name"
     * @return 配置值
     */
    public String getConfig(String configName) {
        return getConfig(configName, "");
    }

    /**
     * 读取int值，为空时返回0
     * @param configName 配置名
     * @return 整数配置值
     */
    public int getInt(String configName) {
        return getInt(configName, 0);
    }

    /**
     * 读取int值，为空时返回defaultValue
     * @param configName 配置名
     * @param defaultValue 为空时的默认值
     * @return 整数配置值
     */
    public int getInt(String configName, int defaultValue) {
        String str = getConfig(configName);
        if (str == null || (str = str.trim()).length() == 0)
            return defaultValue;
        return Integer.parseInt(str);
    }


    /**
     * 读取int值，为空时返回false
     * @param configName 配置名
     * @return 整数配置值
     */
    public boolean getBool(String configName) {
        return getBool(configName, false);
    }

    /**
     * 读取int值，为空时返回defaultValue
     * @param configName 配置名
     * @param defaultValue 为空时的默认值
     * @return 整数配置值
     */
    public boolean getBool(String configName, boolean defaultValue) {
        String str = getConfig(configName);
        if (str == null || (str = str.trim()).length() == 0)
            return defaultValue;
        return str.equals("1") || str.toLowerCase().equals("true");
    }


    /**
     * 注解读取字符串，默认值空
     */
    @Value("${spring.application.name:}")
    public String applicationName;
    /**
     * 注解读取字符串，默认值里有特殊字符要用单引号括起，内部单引号用2个表示
     */
    @Value("${some.key:'abc\ndd\"ee\"''df'}")
    public String strWithQuote;
    /**
     * 注解读取整数，默认值不能为空，否则会抛异常
     */
    @Value("${some.key:-123}")
    public int intWithDefaultValue;
    /**
     * 注解读取整数，默认值可以为空，为空时返回null
     */
    @Value("${some.key:}")
    public Integer intWithNullValue;
    /**
     * 注解读取布尔值，默认值不能为空，否则会抛异常
     */
    @Value("${some.key:1}")
    public boolean booleanWithDefaultValue;
    /**
     * 注解读取布尔值，默认值可以为空，为空时返回null
     */
    @Value("${some.key:}")
    public Boolean booleanWithNullValue;
    /**
     * 注解读取数组，默认值可以为空，为空时返回长度为0的数组
     */
    @Value("${some.key:}")
    public int[] intArrWithDefaultValue;
}
