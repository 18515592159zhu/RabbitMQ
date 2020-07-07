package com.itheima.rabbitmq.routing;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 路由模式的交换机类型为：direct
 */
public class Producer_Routing {

    //交换机名称
    public static final String DIRECT_EXCHAGE = "direct_exchange";
    //队列名称
    public static final String DIRECT_QUEUE_INSERT = "direct_queue_insert";
    //队列名称
    public static final String DIRECT_QUEUE_UPDATE = "direct_queue_update";

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
        channel.exchangeDeclare(DIRECT_EXCHAGE, BuiltinExchangeType.DIRECT);

        /**
         参数1：队列名称
         参数2：是否定义持久化队列
         参数3：是否独占本次连接
         参数4：是否在不使用的时候自动删除队列
         参数5：队列其它参数
         */
        // 6、创建队列
        channel.queueDeclare(DIRECT_QUEUE_INSERT, true, false, false, null);
        channel.queueDeclare(DIRECT_QUEUE_UPDATE, true, false, false, null);

        /*
        queueBind(String queue, String exchange, String routingKey)
        参数：
            1. queue：队列名称
            2. exchange：交换机名称
            3. routingKey：路由键，绑定规则
                如果交换机的类型为fanout ，routingKey设置为""
         */
        // 7、绑定队列和交换机
        channel.queueBind(DIRECT_QUEUE_INSERT, DIRECT_EXCHAGE, "insert");
        channel.queueBind(DIRECT_QUEUE_UPDATE, DIRECT_EXCHAGE, "update");

        // 8、发送消息
        String body = "新增了商品。路由模式；routing key 为 insert ";

        /**
         参数1：交换机名称，如果没有指定则使用默认Default Exchage
         参数2：路由key,简单模式可以传递队列名称
         参数3：消息其它属性
         参数4：消息内容
         */
        channel.basicPublish(DIRECT_EXCHAGE, "insert", null, body.getBytes());
        System.out.println("已发送消息：" + body);

        // 发送消息
        body = "修改了商品。路由模式；routing key 为 update";

        /**
         参数1：交换机名称，如果没有指定则使用默认Default Exchage
         参数2：路由key,简单模式可以传递队列名称
         参数3：消息其它属性
         参数4：消息内容
         */
        channel.basicPublish(DIRECT_EXCHAGE, "update", null, body.getBytes());
        System.out.println("已发送消息：" + body);

        // 9、 释放资源
        channel.close();
        connection.close();
    }
}