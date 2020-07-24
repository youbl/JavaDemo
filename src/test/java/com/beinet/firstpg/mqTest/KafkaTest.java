package com.beinet.firstpg.mqTest;

import com.beinet.firstpg.BaseTest;
import com.beinet.firstpg.configs.ConfigHelper;
import com.beinet.firstpg.mqDemo.MyKafkaConsumer;
import com.beinet.firstpg.mqDemo.MyKafkaProducer;
import com.beinet.firstpg.mqDemo.RabbitMQPool;
import lombok.Data;
import org.apache.kafka.common.errors.TopicExistsException;
import org.apache.kafka.common.errors.UnknownTopicOrPartitionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.beinet.firstpg.mqTest.RabbitMQTestNoSpring.routeKey;


public class KafkaTest extends BaseTest {

    @Test
    public void ListTest() throws ExecutionException, InterruptedException {
        MyKafkaProducer.listConsumerGroups();
        MyKafkaProducer.listTopics();
    }

    @Test
    public void DeleteTest() throws InterruptedException, ExecutionException {
        try {
            MyKafkaProducer.deleteTopic();
            System.out.println("删除成功");
        } catch (ExecutionException exp) {
            if (!(exp.getCause() instanceof UnknownTopicOrPartitionException))
                throw exp;
            String msg = "topic不存在 " + exp.getClass();
            System.out.println(msg);
        }
    }

    @Test
    public void PublishTest() throws Exception {
        try {
            MyKafkaProducer.createTopic();
        } catch (Exception exp) {
            if (!(exp.getCause() instanceof TopicExistsException))
                throw exp;
            String msg = "topic已存在，无法创建 " + exp.getClass();
            System.out.println(msg);
        }


        // 先开启队列监听线程
//        startWaitMsg();
        MyKafkaProducer.produce("a1", "159");

        MqMsg msg = new MqMsg();
        msg.setId(169);
        msg.setName("我是名字name222");
        msg.setDesc("我是测试用的消息333");
        MyKafkaProducer.produce("a1", msg);

        msg.setDesc("I'm message 2333.");
        MyKafkaProducer.produce("a2", msg);

        // startWaitMsg不会阻塞进程，所以这里休眠20秒，看消费效果
        Thread.sleep(200000);
    }

    @Test
    public void startWaitMsg() throws InterruptedException, ExecutionException {
//        MyKafkaConsumer.testKafka();

        new Thread(() -> {
            MyKafkaConsumer.consume(MqMsg.class);
        }).start();

        Thread.sleep(2000000);
    }

    @Data
    static class MqMsg {
        private int id;
        private String name;
        private String desc;
    }
}
