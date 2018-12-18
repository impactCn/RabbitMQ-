package com.rebbitmq.topicExchange;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by Administrator on 2018/12/11.
 */
public class Consumer4TopicExchange {

    public static void main(String[] args) throws Exception{

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String exchangeName = "test_topic_exchange";
        String exchangeType = "topic";
        String queueName = "test_topic_queue";

        // String routingKey = "user.*"
        String routingKey = "user.#";

        // 声明一个交换机
        channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

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
