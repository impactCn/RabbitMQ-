package com.rabbitmq.limit;

import com.rabbitmq.client.*;

/**
 * Created by Administrator on 2018/12/16.
 */
public class Producer {

    public static void main(String[] args) throws Exception{

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();

        String exchangeName = "test_qos_exchange";
        String routingKey = "qos.save";

        String msg = "Hello RabbitMQ Return Message";
        for (int i = 0; i < 5; i++) {
            channel.basicPublish(exchangeName, routingKey, true, null, msg.getBytes());
        }

//        channel.basicPublish(exchange, routingKey, false, null, msg.getBytes());



    }
}
