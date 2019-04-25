package com.beinet.firstpg.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigReader {
    /**
     * 读取字符串，默认值空
     */
    @Value("${spring.application.name:}")
    public String applicationName;
    /**
     * 读取字符串，默认值里有特殊字符要用单引号括起，内部单引号用2个表示
     */
    @Value("${some.key:'abc\ndd\"ee\"''df'}")
    public String strWithQuote;
    /**
     * 读取整数，默认值不能为空，否则会抛异常
     */
    @Value("${some.key:-123}")
    public int intWithDefaultValue;
    /**
     * 读取整数，默认值可以为空，为空时返回null
     */
    @Value("${some.key:}")
    public Integer intWithNullValue;
    /**
     * 读取布尔值，默认值不能为空，否则会抛异常
     */
    @Value("${some.key:1}")
    public boolean booleanWithDefaultValue;
    /**
     * 读取布尔值，默认值可以为空，为空时返回null
     */
    @Value("${some.key:}")
    public Boolean booleanWithNullValue;
    /**
     * 读取数组，默认值可以为空，为空时返回长度为0的数组
     */
    @Value("${some.key:}")
    public int[] intArrWithDefaultValue;
}
