package com.rabbitmq.config;

import com.rabbitmq.adapter.MessageDelegate;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 * Created by Administrator on 2018/12/20.
 */
@Configuration
@ComponentScan({"com.rabbitmq.config.*"})
public class RabbitMQConfig {

    @Bean
    public ConnectionFactory connectionFactory() {

        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setAddresses("127.0.0.1:5672");
        cachingConnectionFactory.setUsername("guest");
        cachingConnectionFactory.setPassword("guest");
        cachingConnectionFactory.setVirtualHost("/");
        return cachingConnectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    /**
     * 针对消费者配置
     * 1、 设置交换机类型
     * 2、 将队列绑定到交换机
     * FanoutExchange：将消息发到所有的绑定队列，无routingKey的概念
     * HeadersExchange：通过添加属性key-value匹配
     * DirectExchange：按照routingKey分发到指定队列
     * TopicExchange：多关键字匹配
     *
     */

    @Bean
    public TopicExchange exchange001() {
        return new TopicExchange("topic001", true, false);
    }

    @Bean
    public Queue queue001() {
        return new Queue("queue001", true); // 队列持久化
    }

    @Bean
    public Binding binding001() {
        return BindingBuilder.bind(queue001()).to(exchange001()).with("spring.*");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        return rabbitTemplate;
    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
//        container.setQueueNames(queue001());
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(5);
        container.setDefaultRequeueRejected(false);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        container.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String s) {
                return s + "_" + UUID.randomUUID().toString();
            }
        });
//        container.setMessageListener(new ChannelAwareMessageListener() {
//            @Override
//            public void onMessage(Message message, Channel channel) throws Exception {
//                String msg = new String(message.getBody());
//                System.err.println(msg);
//            }
//        });

        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());

        container.setMessageListener(adapter);

        ContentTypeDelegatingMessageConverter converter = new ContentTypeDelegatingMessageConverter();

        Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter();


        return container;
    }

}
