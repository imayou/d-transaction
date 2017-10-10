package com.ayou.distributed;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.ayou" })
public class Application {
	@Bean
	public Queue successQueue() {
		return new Queue("foo-success-queue");
	}

	@Bean
	public Queue failureQueue() {
		return new Queue("bar-failure-queue");
	}

	@Bean
	public Jackson2JsonMessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
