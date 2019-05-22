package com.beinet.firstpg.configTest;

import com.beinet.firstpg.BaseTest;
import com.beinet.firstpg.configs.ConfigHelper;
import org.junit.Test;

import java.util.Map;

public class ConfigHelperTest extends BaseTest {

    @Test
    public void Test1(){
        out("读取不存在的key：aaa.bbb: " + ConfigHelper.getConfig("aaa.bbb"));

        for(Map.Entry<String, String> entry : ConfigHelper.getConfigs().entrySet())
            out(entry.getKey() + ":" + entry.getValue());
    }
}
