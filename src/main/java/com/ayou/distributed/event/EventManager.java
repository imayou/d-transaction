package com.ayou.distributed.event;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ayou.distributed.module.Event;

@Repository
public class EventManager {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void insertEvent(Event event) {
		jdbcTemplate.update("INSERT INTO EVENT(id,event_type,model_name,model_id,create_time) VALUES(?,?,?,?,?)",
				event.getId(), event.getEventType().toString(), event.getModelName(), event.getModelId(),
				event.getCreatedTime());
	}

	public String queryModelId(String eventId) {
		return jdbcTemplate.queryForObject("SELECT MODEL_ID FROM EVENT WHERE ID = ?", String.class, eventId);
	}

	public void sendEventQueue(String queueName, Event event) {
		rabbitTemplate.convertAndSend(queueName, event);
	}
}
