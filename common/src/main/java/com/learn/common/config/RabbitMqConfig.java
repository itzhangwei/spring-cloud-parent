package com.learn.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title RabbitMqConfig
 * @package com.learn.common.config
 * @description rabbitmq相关配置
 * @date 2020/4/22 2:07 下午
 */
@Slf4j(topic = "【rabbitmq 配置类】")
@Configuration
public class RabbitMqConfig {
	
	public static final String REQUEST_LOG_QUEUE = "REQUEST_LOG_QUEUE";
	
	public static final String ERROR_LOG_QUEUE = "ERROR_LOG_QUEUE";
	
	@Bean("requestLogQueue")
	public Queue getRequestLogQueue(){
		//true持久化队列
		return new Queue(REQUEST_LOG_QUEUE, true);
	}
	
	@Bean("errorLogQueue")
	public Queue getErrorLogQueue(){
		//true持久化队列
		return new Queue(ERROR_LOG_QUEUE, true);
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
	                                     RabbitTemplate.ConfirmCallback confirmCallback,
	                                     RabbitTemplate.ReturnCallback returnCallback){
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		
		// 设置序列化
		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
		
		//消息发送到交换机监听类
		rabbitTemplate.setConfirmCallback(confirmCallback);
		//消息未路由到队列监听类
		rabbitTemplate.setMandatory(true);
		rabbitTemplate.setReturnCallback(returnCallback);
		return rabbitTemplate;
	}
	
	@Bean
	public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer factory = new SimpleMessageListenerContainer();
		factory.setConnectionFactory(connectionFactory);
		
		final MessageListenerAdapter adapter = new MessageListenerAdapter();
		//指定Json转换器
		adapter.setMessageConverter(new Jackson2JsonMessageConverter());
		
		//设置处理器的消费消息的默认方法
//		adapter.setDefaultListenerMethod("");
		factory.setMessageListener(adapter);
		
		return factory;
	}
}
