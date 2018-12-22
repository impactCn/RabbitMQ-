package com.rabbitmq;

import com.rabbitmq.client.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/12/8.
 */
public class Producer {

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

        Map<String, Object> heads = new HashMap<>();
        heads.put("my1", "111");
        heads.put("my2", "222");

        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .deliveryMode(2)
                .contentType("UTF-8")
                .expiration("15000")
                .headers(heads)
                .build();

        // 通过Channel发送数据
        for (int i = 0; i < 5; i++) {
            String msg = "Hello RabbitMQ";
            channel.basicPublish("", "test001", properties, msg.getBytes());
        }


//        // 关闭相关的连接
        channel.close();
        connection.close();
    }
}
