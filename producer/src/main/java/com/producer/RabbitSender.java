package com.producer;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Administrator on 2018/12/22.
 */
@Component
public class RabbitSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(@Nullable CorrelationData correlationData, boolean b, @Nullable String s) {
            System.err.println("correlationData：" + correlationData);
            System.err.println("ack：" + b);
            if (!b) {
                System.err.println("异常处理....");
            }
        }
    };

    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(org.springframework.amqp.core.Message message, int i, String s, String s1, String s2) {
            System.err.println("return exchange ：" + s1 + "，routingKey：" + s2);
        }
    };


    public void send(Object message, Map<String, Object> properties) throws Exception {

        MessageHeaders messageHeaders = new MessageHeaders(properties);
        Message msg = MessageBuilder.createMessage(message, messageHeaders);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.convertAndSend("exchange-1", "springboot");
    }
}
