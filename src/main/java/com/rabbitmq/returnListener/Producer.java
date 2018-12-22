package com.rabbitmq.returnListener;

import com.rabbitmq.client.*;

import java.io.IOException;

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

        String exchangeName = "test_return_exchange";
        String routingKey = "return.save";
        String routingKeyError = "abc.save";

        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int i, String s, String s1, String s2, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {

                System.out.println("--------------handle return--------------");
                System.out.println("replyCode；" + i);
                System.out.println("replyText：" + s);
                System.out.println("exchange："  + s1);
                System.out.println("routingKey：" + s2);
                System.out.println("basicProperties：" + basicProperties);
                System.out.println("body：" + new String(bytes));
            }
        });

        String msg = "Hello RabbitMQ Return Message";
        channel.basicPublish(exchangeName, routingKeyError, true, null, msg.getBytes());
//        channel.basicPublish(exchange, routingKey, false, null, msg.getBytes());



    }
}
