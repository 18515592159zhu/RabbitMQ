package com.itheima.rabbitmq.ps;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发布与订阅使用的交换机类型为：fanout
 */
public class Producer_PubSub {

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

        /*
        exchangeDeclare(String exchange, BuiltinExchangeType type, boolean durable, boolean autoDelete, boolean internal, Map<String, Object> arguments)
        参数：
            1、exchange：交换机名称
            2、type：交换机类型
                    DIRECT("direct"),：定向
                    FANOUT("fanout"),：扇形（广播），发送消息到每一个与之绑定队列。
                    TOPIC("topic"),通配符的方式
                    HEADERS("headers");参数匹配

            3、durable：是否持久化
            4、autoDelete：自动删除
            5、internal：内部使用。 一般false
            6、arguments：参数

         */
        // 5、创建交换机
        channel.exchangeDeclare(FANOUT_EXCHAGE, BuiltinExchangeType.FANOUT);

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
        channel.queueDeclare(FANOUT_QUEUE_1, true, false, false, null);
        channel.queueDeclare(FANOUT_QUEUE_2, true, false, false, null);

        // 7、绑定队列和交换机
        /*
        queueBind(String queue, String exchange, String routingKey)
        参数：
            1. queue：队列名称
            2. exchange：交换机名称
            3. routingKey：路由键，绑定规则
                如果交换机的类型为fanout ，routingKey设置为""
         */
        channel.queueBind(FANOUT_QUEUE_1, FANOUT_EXCHAGE, "");
        channel.queueBind(FANOUT_QUEUE_2, FANOUT_EXCHAGE, "");

        // 8、发送消息
        /*
        basicPublish(String exchange, String routingKey, BasicProperties props, byte[] body)
        参数：
            1、exchange：交换机名称，如果没有指定，简单模式下交换机会使用默认的 ""
            2、routingKey：路由key,简单模式可以传递队列名称
            3、props：配置信息
            4、body：发送消息数据
         */
        for (int i = 1; i <= 10; i++) {
            String body = "Hi rabbitmq...发布订阅模式--" + i;
            channel.basicPublish(FANOUT_EXCHAGE, "", null, body.getBytes());
            System.out.println("已发送消息：" + body);
        }
        // 9、释放资源
        channel.close();
        connection.close();
    }
}