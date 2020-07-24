package com.beinet.firstpg.mqTest;

import com.beinet.firstpg.BaseTest;
import com.beinet.firstpg.mqDemo.RabbitMQPool;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RabbitMQTest extends RabbitMQTestNoSpring {

    @Autowired
    RabbitMQPool pool1;

    @Override
    RabbitMQPool getPool() {
        return pool1;
    }

    /**
     * 创建交换器和队列、绑定的测试代码
     */
    @Test
    public void CreateTest() {
        super.CreateTest();
    }


    @Test
    public void PublishTest() throws Exception {
        super.PublishTest();
    }
}
