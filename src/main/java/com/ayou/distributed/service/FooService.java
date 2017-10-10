package com.ayou.distributed.service;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ayou.distributed.event.EventManager;
import com.ayou.distributed.module.Event;
import com.ayou.distributed.module.EventType;

@Service
public class FooService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private EventManager eventManager;

	@Transactional
	public void insertFoo(String name) {
		String fooId = UUID.randomUUID().toString();
		try {
			// 将Foo对象插入模型表中
			jdbcTemplate.update("INSERT INTO FOO (id,name) VALUES (?,?)", fooId, name);
		} finally {
			// 创建一个Event对象
			Event event = new Event(EventType.CREATE, "Foo", fooId);
			// 将Event对象插入事件表中
			eventManager.insertEvent(event);
			// 将Event写入成功对列中
			eventManager.sendEventQueue("foo-success-queue", event);
		}
	}

	@Transactional
	@RabbitListener(queues = "bar-failure-queue")
	public void handleBarFailure(Event event) {
		// 根据Event ID 从模型表中获取Foo ID
		String fooId = eventManager.queryModelId(event.getId());
		// 根据Event ID 从模型表中删除对应的记录
		jdbcTemplate.update("DELETE FROM FOO WHERE ID = ?", fooId);
	}

}
