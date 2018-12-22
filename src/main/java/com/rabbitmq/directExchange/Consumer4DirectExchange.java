package com.rabbitmq.directExchange;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by Administrator on 2018/12/11.
 */
public class Consumer4DirectExchange {

    public static void main(String[] args) throws Exception{

        ConnectionFactory connectionFactory  = new ConnectionFactory();

        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);

        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();

        // 声明
        String exchangeName = "test_direct_exchange";
        String exchangeType = "direct";
        String queueName = "test_direct_queue";
        String routingKey = "test.direct";

        // 声明一个交换机
        channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);
        // 声明一个队列
        channel.queueDeclare(queueName, false, false, false, null);
        // 声明一个绑定关系
        channel.queueBind(queueName, exchangeName, routingKey);

        // durable是否持久化
        channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                StringBuilder stringBuilder = new StringBuilder();
                for (byte b: body) {
                    stringBuilder.append((char) b);
                }
                System.out.println(stringBuilder);
            }
        });

    }
}
