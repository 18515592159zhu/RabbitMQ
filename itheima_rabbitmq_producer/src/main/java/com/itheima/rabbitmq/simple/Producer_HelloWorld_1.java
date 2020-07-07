package com.itheima.rabbitmq.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发送多条消息
 */
public class Producer_HelloWorld_1 {
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

        // 5、创建队列 queue
        /*
        queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)
        参数：
            1、queue：队列名称
            2、durable：是否持久化，当mq重启之后，还在
            3、exclusive：
                    * 是否独占。只能有一个消费者监听这队列，
                    * 当Connection关闭时，是否删除队列
            4、autoDelete：是否自动删除。当没有Consumer时，自动删除掉
            5、arguments：参数
         */
        // 定义队列，如果没有一个名字叫hello_world的队列，则会创建该队列，如果有则不会创建
        channel.queueDeclare("hello_world", true, false, false, null);


        // 发送消息
        /*
        basicPublish(String exchange, String routingKey, BasicProperties props, byte[] body)
        参数：
            1、exchange：交换机名称，如果没有指定，简单模式下交换机会使用默认的 ""
            2、routingKey：路由key,简单模式可以传递队列名称
            3、props：配置信息
            4、body：发送消息数据
         */
        for (int i = 1; i < 10; i++) {
            // 要发送的消息
            String body = "hello_world rabbitmq...." + i;

            channel.basicPublish("", "hello_world", null, body.getBytes());

            System.out.println("已发送消息：" + body);
        }

        // 释放资源
        channel.close();
        connection.close();
    }
}