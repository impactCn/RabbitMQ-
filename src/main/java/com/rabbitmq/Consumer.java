package com.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by Administrator on 2018/12/8.
 */
public class Consumer {

    public static void main(String[] args) throws Exception{
        // 创建一个ConnectionFactory, 并进行配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        // 通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();

        // 通过connection创建一个Channel
        Channel channel = connection.createChannel();

        // 创建一个队列
        String queueName = "test001";
        channel.queueDeclare(queueName, true, false, false, null);

        // 创建消费者

        // 设置Channel, 同时获取消息
        channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                StringBuilder stringBuilder = new StringBuilder();
                for (byte b : body) {
                    stringBuilder.append((char) b);
                }

                System.out.println(properties.getHeaders().get("my1"));
                System.out.println(properties.getHeaders().get("my2"));
                System.out.println(stringBuilder.toString());

//                String msg = new String(body, "utf-8");
//                System.out.println("msg:"+msg);
            }
        });
//        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//            String message = new String(delivery.getBody(), "UTF-8");
//            System.out.println(" [x] Received '" + message + "'");
//        };
//        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });




    }


}
