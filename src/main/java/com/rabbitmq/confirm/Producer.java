package com.rabbitmq.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

/**
 * Created by Administrator on 2018/12/16.
 */
public class Producer {

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

        // 制定我们的消息投递模式：消息的确认模式
        channel.confirmSelect();

        String exchangeName = "test_confirm_exchange";
        String routingKey = "confirm.save";

        // 发送消息
        String msg = "Hello RabbitMQ Send confirm message!";
        channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());

        // 添加一个确认监听
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long l, boolean b) throws IOException {
                System.out.println("------------no ack!---------------");
            }

            @Override
            public void handleNack(long l, boolean b) throws IOException {
                System.out.println("------------ack!---------------");
            }
        });

    }
}
