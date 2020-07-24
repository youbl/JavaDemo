package com.beinet.firstpg.mqDemo;

import com.beinet.firstpg.configs.ConfigHelper;
import com.beinet.firstpg.serializeDemo.SerializeHelper;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MyKafkaProducer {
    private static KafkaProducer<String, String> producer;
    private static String kafkaServer;
    private static String defaultTopic;

    static {
        kafkaServer = ConfigHelper.getConfig("spring.kafka.bootstrap-servers");
        defaultTopic = ConfigHelper.getConfig("spring.kafka.template.default-topic");

        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaServer);
        props.put("acks", ConfigHelper.getConfig("spring.kafka.producer.acks"));
        props.put("retries", ConfigHelper.getInt("spring.kafka.producer.retries"));
        props.put("batch.size", ConfigHelper.getInt("spring.kafka.producer.batch-size"));
//        props.put("linger.ms", 1);
        props.put("buffer.memory", ConfigHelper.getInt("spring.kafka.producer.buffer-memory"));
        props.put("key.serializer", ConfigHelper.getConfig("spring.kafka.producer.key-serializer"));
        props.put("value.serializer", ConfigHelper.getConfig("spring.kafka.producer.value-serializer"));

        producer = new KafkaProducer<String, String>(props);
    }

    // <editor-fold desc="管理相关方法">

    /**
     * 创建主题topic.
     * 协议参考： https://kafka.apache.org/protocol
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void createTopic() throws ExecutionException, InterruptedException {
        try (AdminClient client = createClient()) {
            CreateTopicsResult ret = client.createTopics(Arrays.asList(new NewTopic(defaultTopic, 1, (short) 1)));
            ret.all().get();
            ret.values().forEach((k, v) -> {
                System.out.println(k + "===" + v.isDone());
            });
        }
    }

    /**
     * 枚举所有的topic
     */
    public static void listTopics() throws ExecutionException, InterruptedException {
        try (AdminClient client = createClient()) {
            // 获取所有的topic
            ListTopicsResult result = client.listTopics();
            Collection<TopicListing> ret = result.listings().get();

            List<String> topics = new ArrayList<>(ret.size());
            ret.forEach(item -> {
                System.out.println(item);
                topics.add(item.name());
            });

            // 获取所有topic的详情
            DescribeTopicsResult result2 = client.describeTopics(topics);
            result2.all().get().forEach((k, v) -> {
                System.out.println(k + "=====" + v);
            });

        }
    }

    /**
     * 删除主题topic
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void listConsumerGroups() throws ExecutionException, InterruptedException {
        try (AdminClient client = createClient()) {
            ListConsumerGroupsResult ret = client.listConsumerGroups();
            Collection arr = ret.all().get();
            if(arr.size() == 0){
                System.out.println("no Consumer Group");
                return;
            }
            arr.forEach(item -> {
                System.out.println(item);
            });
        }
    }


    /**
     * 删除主题topic
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void deleteTopic() throws ExecutionException, InterruptedException {
        try (AdminClient client = createClient()) {
            List<String> arr = new ArrayList<>();
            arr.add(defaultTopic);
            DeleteTopicsResult ret = client.deleteTopics(arr);
            ret.all().get();
            ret.values().forEach((k, v) -> {
                System.out.println(k + "===" + v.isDone());
            });
        }
    }



    static AdminClient createClient(){
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaServer);
        return AdminClient.create(props);
    }


    // </editor-fold>



    public static void produce(String key, Object val) throws Exception {
        String strVal = SerializeHelper.serializeToStr(val);
        ProducerRecord<String, String> msg = new ProducerRecord<>(defaultTopic, key, strVal);
        Future ret = producer.send(msg);
        System.out.println("生产成功： " + ret.get());
    }
}
