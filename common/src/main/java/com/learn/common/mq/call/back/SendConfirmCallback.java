package com.learn.common.mq.call.back;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title SendConfirmCallback
 * @package com.learn.common.mq
 * @description 消息发送到交换机监听类
 * @date 2020/4/24 5:39 下午
 */
@Component
@Slf4j(topic = "mq消息发送到交换机成功回调函数【SendConfirmCallback】")
public class SendConfirmCallback implements RabbitTemplate.ConfirmCallback {
	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		//ack：true表示消息到了交换机，不表示消息进入了队列
		if (ack) {
			log.info("消息推送给mq成功");
			// TODO
		} else {
			log.error("消息推送给mq失败，cause:{}",cause);
			// TODO
		}
	}
}
