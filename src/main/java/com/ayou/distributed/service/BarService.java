package com.ayou.distributed.service;

import java.util.UUID;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ayou.distributed.event.EventManager;
import com.ayou.distributed.module.Event;

@Service
public class BarService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private EventManager eventManager;

	@Transactional
	@RabbitListener(queues = "foo-success-queue")
	public void handleFooSuccess(Event event) {
		try {
			String barId = UUID.randomUUID().toString();
			// 将Bar 对象插入模型表中
			jdbcTemplate.update("insert into bar(id,name) values(?,?)", barId, "bar");
			// 故意抛出异常
			//throw new RuntimeException();
		} catch (Exception e) {
			// 将Event对象写入失败对列中
			eventManager.sendEventQueue("bar-failure-queue", event);
			// 让事务进行回滚
			throw new AmqpRejectAndDontRequeueException(e);
		}
	}
}
