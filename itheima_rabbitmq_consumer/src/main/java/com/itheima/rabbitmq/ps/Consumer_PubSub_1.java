package com.itheima.rabbitmq.ps;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer_PubSub_1 {

    //交换机名称
    public static final String FANOUT_EXCHAGE = "fanout_exchange";
    //队列名称
    public static final String FANOUT_QUEUE_1 = "fanout_queue_1";
    //队列名称
    public static final String FANOUT_QUEUE_2 = "fanout_queue_2";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 1、创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 2、设置参数
        // 主机地址：默认为 localhost
        connectionFactory.setHost("192.168.198.128");

        // 连接端口：默认为 5672
        connectionFactory.setPort(5672);

        // 虚拟主机名称：默认为 /
        connectionFactory.setVirtualHost("/itcast");

        // 连接用户名：默认为 guest
        connectionFactory.setUsername("heima");

        // 连接密码：默认为 guest
        connectionFactory.setPassword("heima");

        // 3、创建连接
        Connection connection = connectionFactory.newConnection();

        // 4、创建 channel
        Channel channel = connection.createChannel();

        // 声明交换机
        channel.exchangeDeclare(FANOUT_EXCHAGE, BuiltinExchangeType.FANOUT);

        // 声明（创建）队列
        /**
         * 参数1：队列名称
         * 参数2：是否定义持久化队列
         * 参数3：是否独占本次连接
         * 参数4：是否在不使用的时候自动删除队列
         * 参数5：队列其它参数
         */
        channel.queueDeclare(FANOUT_QUEUE_1, true, false, false, null);

        // 队列绑定交换机
        channel.queueBind(FANOUT_QUEUE_1, FANOUT_EXCHAGE, "");

        // 接收消息
        Consumer consumer = new DefaultConsumer(channel) {
            /*
                回调方法，当收到消息后，会自动执行该方法

                1. consumerTag：标识
                2. envelope：获取一些信息，交换机，路由key...
                3. properties:配置信息
                4. body：数据
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                // 路由key
                System.out.println("路由key为：" + envelope.getRoutingKey());
                // 交换机
                System.out.println("交换机为：" + envelope.getExchange());
                // 消息id
                System.out.println("消息id为：" + envelope.getDeliveryTag());
                // 收到的消息
                System.out.println("消费者1-接收到的消息为：" + new String(body, "utf-8"));
            }
        };

        // 监听消息
        /**
         * 参数1：队列名称
         * 参数2：是否自动确认，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置为false则需要手动确认
         * 参数3：消息接收到后回调
         */
        channel.basicConsume(FANOUT_QUEUE_1, true, consumer);

        // 关闭资源？ 不要
    }
}
