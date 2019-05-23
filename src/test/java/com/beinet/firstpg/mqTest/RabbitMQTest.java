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
public class RabbitMQTest extends BaseTest {

    @Autowired
    RabbitMQPool pool;
    static String exchangeName = "youblExg";
    static String queueName = "youblQueue";
    static String routeKey = "abc";

    /**
     * 创建交换器和队列、绑定的测试代码
     */
    @Test
    public void CreateTest() {
        boolean durable = true;
        // Exchange不存在或参数一致时，创建，不抛异常
        pool.exchangeDeclare(exchangeName, "fanout", durable, true, null);
        // Queue不存在或参数一致时，创建，不抛异常
        pool.QueueDeclareAndBind(queueName, exchangeName, durable, 0, 0, 0, null, null, null);
        // 重复绑定，参数一致，不抛异常
        pool.Binding(exchangeName, queueName, null, null);

        durable = false;
        try {
            // Exchange已存在时，重复创建，参数不一致，会抛异常
            pool.exchangeDeclare(exchangeName, "fanout", durable, true, null);
            Assert.assertTrue(false); // 不应该执行到这里
        } catch (org.springframework.amqp.AmqpIOException e) {
        }
        try {
            // Queue已存在时，重复创建，参数不一致，会抛异常
            pool.QueueDeclareAndBind(queueName, exchangeName, durable, 0, 0, 0, null, null, null);
            Assert.assertTrue(false); // 不应该执行到这里
        } catch (org.springframework.amqp.AmqpIOException e) {
        }

        // 重复绑定，路由参数不一致，不抛异常，多一个绑定而已
        pool.Binding(exchangeName, queueName, routeKey, null);

        try {
            // 绑定到不存在的交换器，抛异常
            pool.Binding("noExistsExchange", queueName, null, null);
            Assert.assertTrue(false); // 不应该执行到这里
        } catch (org.springframework.amqp.AmqpIOException e) {
        }

        try {
            // 绑定到不存在的队列，抛异常
            pool.Binding(exchangeName, "noExistsQueue", null, null);
            Assert.assertTrue(false); // 不应该执行到这里
        } catch (org.springframework.amqp.AmqpIOException e) {
        }
    }


    @Test
    public void PublishTest() throws InterruptedException {
        // 先开启队列监听
        startWaitMsg();

        // 请先运行 RabbitMQTest.CreateTest()，以确保交换器和队列存在
        MqMsg msg = new MqMsg();
        msg.setId(169);
        msg.setName("我是名字name");
        msg.setDesc("我是测试用的消息");
        pool.publish(exchangeName, msg);

        Map<String, Object> header = new HashMap<>();
        header.put("abc", "123");
        pool.publish(exchangeName, msg, header, 1, routeKey);

        // startWaitMsg不会阻塞进程，所以这里休眠20秒，看消费效果
        Thread.sleep(20000);
    }

    void startWaitMsg() {
        new Thread(() -> {
            pool.waitMsg(queueName, 3, (msg, routeKey, headers) -> {
                System.out.println("------" + msg + "------");
            }, MqMsg.class);
        }).start();
    }

    @Data
    static class MqMsg {
        private int id;
        private String name;
        private String desc;
    }
}
