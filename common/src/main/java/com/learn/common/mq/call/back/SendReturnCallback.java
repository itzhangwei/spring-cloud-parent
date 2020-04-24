package com.learn.common.mq.call.back;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title SendReturnCallback
 * @package com.learn.common.mq.call.back
 * @description 消息未 路由到队列监听类
 * @date 2020/4/24 5:47 下午
 */
@Component
@Slf4j(topic = "【SendReturnCallback】")
public class SendReturnCallback implements RabbitTemplate.ReturnCallback {
	
	@Override
	public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
		//TODO 做降级操作，记录日志等等
		log.error("消息发送到mq失败,message:{},replyCode:{},replyText:{},exchange:{},routingKey:{}",
				message, replyCode, replyText, exchange, routingKey);
	}
}
