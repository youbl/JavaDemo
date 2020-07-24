package com.beinet.firstpg.mqDemo;

import com.beinet.firstpg.serializeDemo.SerializeHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.impl.recovery.AutorecoveringChannel;
import lombok.Data;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

@Component
public class RabbitMQPool {

    AmqpAdmin admin;
    RabbitTemplate rabbitTemplate;



    /**
     * 注入用的构造函数
     *
     * @param admin          创建交换器或队列的辅助类对象
     * @param rabbitTemplate 生产消费的辅助类对象
     */
    @Autowired
    public RabbitMQPool(AmqpAdmin admin, RabbitTemplate rabbitTemplate) {
        this.admin = admin;
        this.rabbitTemplate = rabbitTemplate;

        init();
    }

    /**
     * 根据参数自行构建，不依赖注入
     *
     * @param host     主机IP或主机名
     * @param port     端口，小于1时，默认5672
     * @param username 用户名
     * @param password 密码
     */
    public RabbitMQPool(String host, int port, String username, String password) {
        if (port <= 0) port = 5672;
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(host + ":" + String.valueOf(port));
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("/");

        rabbitTemplate = new RabbitTemplate(connectionFactory);
        admin = new RabbitAdmin(connectionFactory);

        init();
    }

    private void init() {
        // 设置生产者Confirm方法，在配置里开启 publisher-confirms: true,或这里执行setPublisherConfirms
        ConnectionFactory conn = rabbitTemplate.getConnectionFactory();
        if(conn instanceof CachingConnectionFactory)
            ((CachingConnectionFactory)conn).setPublisherConfirms(true);

        // confirm(@Nullable CorrelationData var1, boolean var2, @Nullable String var3)
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            // 发送到交换器失败时，ack为false。
            // 注意：这里仅检验是否发送到交换器，如果没有队列跟交换器绑定，这里也是true
            if (ack) {
            }
            System.out.println(ack);
            System.out.println(correlationData);
            System.out.println(cause);
        });
    }
    // <editor-fold desc="创建交换器或队列的方法">


    /**
     * 创建Exchange.
     * 程序启动时调用一次此方法即可。
     *
     * @param exchangeName 要创建的交换器名
     * @param exchangeType >fanout：广播消息给Exchage绑定的所有队列；
     *                     direct：直接转发给RouteKey指定的队列；
     *                     topic：转发给匹配主题的队列；
     * @param durable      是否持久化
     * @param autoDelete   是否自动删除.
     *                     注：设置为true时，当有Queue或其它Exchange绑定到这个Exchange上后，
     *                     在这个Exchange的所有绑定都取消后，会自动删除这个Exchange
     * @param arguments    其它参数
     */
    public void exchangeDeclare(String exchangeName, String exchangeType, boolean durable,
                                boolean autoDelete, Map<String, Object> arguments) {
        if (exchangeName == null || exchangeName.length() == 0)
            throw new IllegalArgumentException("exchangeName can't be empty.");

        Exchange exg;
        switch (exchangeType) {
            case "direct":
                exg = new DirectExchange(exchangeName, durable, autoDelete, arguments);
                break;
            case "topic":
                exg = new TopicExchange(exchangeName, durable, autoDelete, arguments);
                break;
            default: // "fanout"
                exg = new FanoutExchange(exchangeName, durable, autoDelete, arguments);
                break;
        }
        admin.declareExchange(exg);
    }


    /**
     * 创建Queue，如果提供了参数 bindExchangeName，则把创建的Queue绑定到这个Exchange。
     * 程序启动时调用一次此方法即可
     *
     * @param queueName        要创建的队列名
     * @param bindExchangeName 要绑定到的交换器名，为空时不进行绑定
     * @param durable          是否持久化
     * @param messageTtl       消息生存时长，毫秒，0表示无限时长
     * @param messageMaxLen    消息队列最大长度，0表示无限长度
     * @param autoDeleteTime   自动删除时间(ms)，在该时间段内如果没有被重新声明，且没有调用过get命令，将会删除
     * @param routingKey       绑定到队列上的路由Key，可选。特殊业务中可能需要通过路由Key来归类不同的队列
     * @param deadExchange     设置了messageTtl时，过期的死信要转发到的exg
     * @param deadRoutingKey   设置了messageTtl时，过期的死信要转发的routing-key
     */
    public void QueueDeclareAndBind(String queueName, String bindExchangeName, boolean durable,
                                    int messageTtl, int messageMaxLen, int autoDeleteTime, String routingKey,
                                    String deadExchange, String deadRoutingKey) {
        if (queueName == null || queueName.length() == 0)
            throw new IllegalArgumentException("queueName can't be empty.");
        if (messageTtl != 0 && messageTtl < 1000)
            throw new IllegalArgumentException("messageTtl must bigger than 1000ms");
        if (messageMaxLen < 0)
            throw new IllegalArgumentException("messageMaxLen must bigger than 0");

        Map<String, Object> arg = new HashMap<>();
        if (messageTtl >= 1000) {
            arg.put("x-message-ttl", messageTtl);
        }
        if (messageMaxLen > 0) {
            arg.put("x-max-length", messageMaxLen);
        }
        if (autoDeleteTime > 0) {
            arg.put("x-expires", autoDeleteTime);
        }
        if (deadExchange != null && deadExchange.length() > 0) {
            arg.put("x-dead-letter-exchange", deadExchange);
            if (deadRoutingKey != null && deadRoutingKey.length() > 0) {
                arg.put("x-dead-letter-routing-key", deadRoutingKey);
            }
        }
        Queue queue = new Queue(queueName, durable, false, false, arg);
        admin.declareQueue(queue);

        // 绑定到Exchange
        if (bindExchangeName != null && bindExchangeName.length() > 0) {
            Binding(bindExchangeName, queueName, routingKey, null);
        }
    }

    /**
     * 把指定的交换器和队列进行绑定
     *
     * @param exchangeName 交换器名
     * @param queueName    队列名
     * @param routingKey   路由key
     * @param arguments    参数
     */
    public void Binding(String exchangeName, String queueName, String routingKey, Map<String, Object> arguments) {
        routingKey = routingKey == null ? "" : routingKey;
        Binding bind = new Binding(queueName, Binding.DestinationType.QUEUE, exchangeName, routingKey, arguments);
        admin.declareBinding(bind);
    }
    // </editor-fold>


    // <editor-fold desc="生产者方法，即发送消息的方法">

    /**
     * 提交持久化消息到指定的Exchage
     *
     * @param exchangeName 交换器名，为空表示使用没名字的默认交换器
     * @param message      消息对象
     */
    public void publish(String exchangeName, Object message) throws JsonProcessingException {
        publish(exchangeName, message, null, 1, null);
    }


    /**
     * 提交持久化消息到指定的Exchage
     *
     * @param exchangeName 交换器名，为空表示使用没名字的默认交换器
     * @param message      消息对象
     * @param routingKey   路由的Key
     */
    public void publish(String exchangeName, Object message, String routingKey) throws JsonProcessingException {
        publish(exchangeName, message, null, 1, routingKey);
    }

    /**
     * 提交持久化消息到指定的Exchage
     *
     * @param exchangeName 交换器名，为空表示使用没名字的默认交换器
     * @param message      消息对象
     * @param headers      消息的头信息
     */
    public void publish(String exchangeName, Object message, Map<String, Object> headers) throws JsonProcessingException {
        publish(exchangeName, message, headers, 1, null);
    }

    /**
     * 提交持久化消息到指定的Exchage
     *
     * @param exchangeName 交换器名，为空表示使用没名字的默认交换器
     * @param message      消息对象
     * @param headers      消息的头信息
     * @param routingKey   路由的Key
     */
    public void publish(String exchangeName, Object message, Map<String, Object> headers, String routingKey) throws JsonProcessingException {
        publish(exchangeName, message, headers, 1, routingKey);
    }

    /**
     * 提交消息到指定的Exchage
     *
     * @param exchangeName 交换器名，为空表示使用没名字的默认交换器
     * @param message      消息对象
     * @param headers      消息的头信息
     * @param deliveryMode 是否持久化 1:不持久化 2：持久化
     * @param routingKey   路由的Key
     */
    public void publish(String exchangeName, Object message, Map<String, Object> headers, int deliveryMode, String routingKey) throws JsonProcessingException {
        MessageProperties properties = new MessageProperties();

//        // 设置时间戳和消息id
//        properties.setTimestamp(new Date());
//        properties.setMessageId(UUID.randomUUID().toString());

        properties.setDeliveryMode(MessageDeliveryMode.fromInt(deliveryMode));
        if (headers != null)
            for (Map.Entry<String, Object> entry : headers.entrySet())
                properties.setHeader(entry.getKey(), entry.getValue());

        byte[] arr = SerializeHelper.serialize(message);
        Message mqMessage = MessageBuilder.withBody(arr)
                .andProperties(properties)
                .build();
        exchangeName = exchangeName == null ? "" : exchangeName;
        routingKey = routingKey == null ? "" : routingKey;
        rabbitTemplate.send(exchangeName, routingKey, mqMessage);
    }

    // </editor-fold>


    // <editor-fold desc="消费者方法，即监听处理消息的方法">

    /**
     * 启动消息监听
     *
     * @param queueName     要监听的队列名
     * @param prefetchCount 预取消息数，小于1时取值默认5
     * @param callback      消息处理方法
     * @param objClass      消息对象类型
     * @param <T>           对象
     */
    public <T> void waitMsg(String queueName, int prefetchCount, MsgCallback<T> callback, Class<T> objClass) {
        prefetchCount = prefetchCount <= 0 ? 5 : prefetchCount;

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(rabbitTemplate.getConnectionFactory());
        container.setQueueNames(queueName);
        container.setPrefetchCount(prefetchCount);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); // 设置ack模式为手工确认

// https://docs.spring.io/spring-amqp-net/docs/1.0.x/api/html/Spring.Messaging.Amqp.Rabbit~Spring.Messaging.Amqp.Rabbit.Listener.AbstractMessageListenerContainer~ExposeListenerChannel.html
//        container.setExposeListenerChannel(true); // 通道是否向监听器公开，默认true，如果设置false，就不能设置AcknowledgeMode.MANUAL
//        container.setConcurrentConsumers(1);  // 设置启动几个消费者，默认1个

        HandleListener listener = new HandleListener<T>();
        listener.setCallback(callback);
        listener.setObjClass(objClass);
        container.setMessageListener(listener);//监听处理类
        container.start();
    }

    @Data
    static class HandleListener<T> implements ChannelAwareMessageListener {
        private MsgCallback<T> callback;
        private Class<T> objClass;

        @Override
        public void onMessage(Message message, Channel channel) throws Exception {
            MessageProperties properties = message.getMessageProperties();
            try {
                T obj = SerializeHelper.deserialize(message.getBody(), objClass);
                callback.callback(obj, properties.getReceivedRoutingKey(), properties.getHeaders());
            } finally {
                // 手工ack 参数2：multiple：是否批量.true:将一次性ack所有小于deliveryTag的消息
                channel.basicAck(properties.getDeliveryTag(), false);
            }
        }
    }
    // </editor-fold>
}