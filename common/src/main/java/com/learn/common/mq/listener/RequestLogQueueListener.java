package com.learn.common.mq.listener;

import com.alibaba.fastjson.JSON;
import com.learn.common.config.RabbitMqConfig;
import com.learn.common.db.dao.tools.RequestLogDao;
import com.learn.common.entity.db.tools.RequestLog;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title RequestLogQueueListener
 * @package com.learn.common.mq.listener
 * @description 请求日志队列监听类
 * @date 2020/4/23 4:22 下午
 */
@Slf4j(topic = "💐💐请求日志队列消费者监听【RequestLogQueueListener】💐💐")
@Component
public class RequestLogQueueListener {
	
	private final RequestLogDao requestLogDao;
	
	private final RabbitTemplate rabbitTemplate;
	
	public RequestLogQueueListener(RequestLogDao requestLogDao,
	                               RabbitTemplate rabbitTemplate) {
		this.requestLogDao = requestLogDao;
		this.rabbitTemplate = rabbitTemplate;
	}
	
	@RabbitHandler
	@RabbitListener(queues = RabbitMqConfig.REQUEST_LOG_QUEUE)
	public void process(String content, Channel channel, Message message) throws IOException {
		try {
			log.info("{}", message);
			RequestLog requestLog = JSON.parseObject(message.getBody(), RequestLog.class);
			final int exist = requestLogDao.existTable();
			if (exist == 0) {
				requestLogDao.createTable("t_request_log");
			}
			// 日志入库操作
				this.requestLogDao.insert(requestLog);
			
			// 手动ack
			channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("消费这监听异常，转入错误队列");
			//转入异常队列
			rabbitTemplate.convertAndSend(RabbitMqConfig.ERROR_LOG_QUEUE,message);
			channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
		}
		/**
		 * requeue:是否重新回到消息队列中
		 * channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
		 *  // ========消费失败处理方式：1、重新入栈消费 (重复消费错误数据会死循环)=======
		 *       // channel.basicRecover(false); // 重新压入MQ，参数表示是否能被其它消费者消费，效果类似第三种处理方式开启重新入栈的场景,不同的是它会触发 ListenerContainerConsumerFailedEvent
		 *
		 *       // ========消费失败处理方式：2、转到其它队列，比如延迟队列进行特殊处理;然后继续消费下一条消息。（推荐做法）=======
		 *       rabbitTemplate.convertAndSend(RabbitDemoConfig.DEMO_DEAD_LETTER_ROUTING_KEY, demo);
		 *       channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		 *
		 *       // ========消费失败处理方式：3、拒绝并重新入栈(重复消费错误数据会死循环)=======
		 *       // channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
		 *       // // 第二个参数表示是否重新入栈,为false会直接丢弃当前消息；为true时会重新放入原消息队列位置，重新消费。
		 *
		 *       // ========消费失败处理方式：4、抛异常,启用了最大重试次数后会被阻塞到unacked消息中=======
		 *       // throw new IOException(e); //根据application.properties
		 *       // 配置的最大重试次数进行重试，超过的话进入unacked状态。由于本消息未应答，因此下一条消息会被本消息阻塞，不会继续处理。会导致 Ready 消息堆积。
		 */
	}
}
