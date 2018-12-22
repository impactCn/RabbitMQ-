package com.rabbitmq.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2018/12/20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMQServiceTest {


    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void testAdmin() throws Exception {
        rabbitAdmin.declareExchange(new DirectExchange("test.direct", false, false));
        rabbitAdmin.declareExchange(new TopicExchange("test.topic", false, false));
        rabbitAdmin.declareExchange(new FanoutExchange("test.fanout", false, false));

        rabbitAdmin.declareQueue(new Queue("test.direct.queue", false));
        rabbitAdmin.declareQueue(new Queue("test.topic.queue", false));
        rabbitAdmin.declareQueue(new Queue("test.fanout.queue", false));

        rabbitAdmin.declareBinding(new Binding("test.direct.queue",
                Binding.DestinationType.QUEUE,
                "test.direct",
                "direct",
                new HashMap<>()));

        rabbitAdmin.declareBinding(BindingBuilder
                .bind(new Queue("test.topic.queue", false))     // 直接创建队列
                .to(new TopicExchange("test.topic", false, false)) // 直接创建交换机建立关联关系
                .with("user.#"));       // 指定路由

        rabbitAdmin.declareBinding(BindingBuilder
                .bind(new Queue("test.fanout.queue", false))
                .to(new FanoutExchange("test.fanout", false, false)));
    }

    @Test
    public void testSendMessage() throws Exception {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.getHeaders().put("desc", "信息描述...");
        messageProperties.getHeaders().put("type", "自定义消息类型");
        Message message = new Message("Hello RabbitMQ".getBytes(), messageProperties);

        rabbitTemplate.convertAndSend("test001", "spring.ampq", message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                System.err.println("--------添加额外的设置-----------");
                message.getMessageProperties().getHeaders().put("desc", "额外修改的信息描述");
                message.getMessageProperties().getHeaders().put("attr", "额外新加的属性");
                return message;
            }
        });
    }


}