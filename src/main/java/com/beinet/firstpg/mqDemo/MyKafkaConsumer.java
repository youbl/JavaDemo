package com.beinet.firstpg.mqDemo;

import com.beinet.firstpg.configs.ConfigHelper;
import com.beinet.firstpg.serializeDemo.SerializeHelper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MyKafkaConsumer {
    private static KafkaConsumer<String, String> consumer;

    static {

    }

    public static <T> void consume(Class<T> objClass) {
        String kafkaServer = ConfigHelper.getConfig("spring.kafka.bootstrap-servers");
        String defaultTopic = ConfigHelper.getConfig("spring.kafka.template.default-topic");

        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaServer);
//        props.put("group.id", ConfigHelper.getConfig("spring.kafka.consumer.group-id"));
        props.put("enable.auto.commit", ConfigHelper.getConfig("spring.kafka.consumer.enable-auto-commit"));
        props.put("auto.commit.interval.ms", ConfigHelper.getConfig("spring.kafka.consumer.auto-commit-interval"));
        props.put("auto.offset.reset", ConfigHelper.getConfig("spring.kafka.consumer.auto-offset-reset"));
        props.put("key.deserializer", ConfigHelper.getConfig("spring.kafka.consumer.key-deserializer"));
        props.put("value.deserializer", ConfigHelper.getConfig("spring.kafka.consumer.value-deserializer"));

        consumer = new KafkaConsumer<String, String>(props);
        TopicPartition partition1 = new TopicPartition(defaultTopic, 0);
        TopicPartition partition2 = new TopicPartition("mytest2", 0);
        consumer.assign(Arrays.asList(partition1, partition2));
//        consumer.subscribe(Arrays.asList(defaultTopic, "mytest2"));

        Duration duration = Duration.ofMillis(100);
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(duration);
            for (ConsumerRecord<String, String> record : records) {
                String value = record.value();
//                T obj = SerializeHelper.deserialize(value, objClass);
                System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), value);
                System.out.println();
            }
        }
    }


//    public static<T> void testKafka() throws ExecutionException, InterruptedException {
//        String  kafkaServer = "10.2.0.32:9092";
//        String defaultTopic = "mytesttopic";
//
//        // send message
//        Properties props = new Properties();
//        props.put("bootstrap.servers", kafkaServer);
//        props.put("acks", "all");
//        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        KafkaProducer producer = new KafkaProducer<String, String>(props);
//        ProducerRecord<String, String> msg = new ProducerRecord<>(defaultTopic, "a1", "test12");
//        Future ret = producer.send(msg);
//        System.out.println("send ok： " + ret.get());
//
//        // recieve message
//        props = new Properties();
//        props.put("bootstrap.servers", kafkaServer);
////        props.put("group.id", "testaaafda4aaa");
//        props.put("enable.auto.commit", "false");
//        props.put("auto.offset.reset", "earliest");
//        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//
//        KafkaConsumer consumer = new KafkaConsumer<String, String>(props);
//        TopicPartition partition1 = new TopicPartition(defaultTopic, 0);
////        TopicPartition partition2 = new TopicPartition(defaultTopic, 1);
////        TopicPartition partition3 = new TopicPartition("mytest2", 0);
//
//        consumer.assign(Arrays.asList(partition1));
////        consumer.subscribe(Arrays.asList(defaultTopic));
////        Thread.sleep(10000);
//        Duration duration = Duration.ofMillis(100);
//        while (true) {
////            consumer.position(partition1);
//            ConsumerRecords<String, String> records = consumer.poll(duration);
//            System.out.println(records.count());
//        }
//    }
}