package com.ayou.distributed.controller;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ayou.distributed.service.FooService;

@RestController
public class FooController {
	@Autowired
	private FooService fooService;

	@GetMapping("/foo")
	public void foo(@PathParam("name") String name) {
		fooService.insertFoo(name);
	}
}
