package com.rebbitmq.directExchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Created by Administrator on 2018/12/11.
 */
public class Producer4DirectExchange {

    public static void main(String[] args) throws Exception{

        // 创建ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        // 创建Connection
        Connection connection = connectionFactory.newConnection();

        // 创建Channel
        Channel channel = connection.createChannel();

        // 声明
        String exchangeName = "test_direct_exchange";
        String routingKey = "test.direct";

        // 发送
        String msg = "Hello World RabbitMQ 4 Direct Exchange Message....";
        channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());

        connection.close();
        channel.close();

    }
}
