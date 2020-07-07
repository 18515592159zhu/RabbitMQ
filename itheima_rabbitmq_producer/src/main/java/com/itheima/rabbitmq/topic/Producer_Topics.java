package com.itheima.rabbitmq.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 通配符Topic的交换机类型为：topic
 */
public class Producer_Topics {

    //交换机名称
    public static final String TOPIC_EXCHAGE = "topic_exchange";
    //队列名称
    public static final String TOPIC_QUEUE_1 = "topic_queue_1";
    //队列名称
    public static final String TOPIC_QUEUE_2 = "topic_queue_2";

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

        /*
       exchangeDeclare(String exchange, BuiltinExchangeType type, boolean durable, boolean autoDelete, boolean internal, Map<String, Object> arguments)
       参数：
        1. exchange:交换机名称
        2. type:交换机类型
            DIRECT("direct"),：定向
            FANOUT("fanout"),：扇形（广播），发送消息到每一个与之绑定队列。
            TOPIC("topic"),通配符的方式
            HEADERS("headers");参数匹配

        3. durable:是否持久化
        4. autoDelete:自动删除
        5. internal：内部使用。 一般false
        6. arguments：参数
        */
        // 5、创建交换机
        channel.exchangeDeclare(TOPIC_EXCHAGE, BuiltinExchangeType.TOPIC);

        // 发送消息
        String body = "新增了商品。Topic模式；routing key 为 item.insert";
        channel.basicPublish(TOPIC_EXCHAGE, "item.insert", null, body.getBytes());
        System.out.println("已发送消息：" + body);

        // 发送消息
        body = "修改了商品。Topic模式；routing key 为 item.update";
        channel.basicPublish(TOPIC_EXCHAGE, "item.update", null, body.getBytes());
        System.out.println("已发送消息：" + body);

        // 发送消息
        body = "删除了商品。Topic模式；routing key 为 item.delete";
        channel.basicPublish(TOPIC_EXCHAGE, "item.delete", null, body.getBytes());
        System.out.println("已发送消息：" + body);

        // 关闭资源
        channel.close();
        connection.close();
    }
}