package com.rabbitmq.confirm;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by Administrator on 2018/12/16.
 */
public class Consumer {
    public static void main(String[] args) throws Exception{

        // 创建ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        // 获取Connect
        Connection connection = connectionFactory.newConnection();

        // 获取channel
        Channel channel = connection.createChannel();

        String exchangeName = "test_confirm_exchange";
        String routingKey = "confirm.save";
        String queueName = "test_confirm_queue";

        // 声明交换机和队列 然后进行绑定设置，最后制定路由Key
        channel.exchangeDeclare(exchangeName, "topic", true);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        channel.basicConsume(queueName,true, new DefaultConsumer(channel) {
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
