package com.rebbitmq.fanoutExchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Created by Administrator on 2018/12/12.
 */
public class Producer4FanoutExchange {

    public static void main(String[] args) throws Exception {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");


        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "test_fanout_exchange";

        for (int i = 0; i < 10; i++) {
            String msg = "Hello World RabbitMQ 4 FANOUT Exchange Message ...";
            channel.basicPublish(exchangeName, "abc", null, msg.getBytes());
        }

        channel.close();
        connection.close();
    }
}
