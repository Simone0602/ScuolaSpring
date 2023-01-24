package com.exprivia.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exprivia.demo.service.DocenteService;

@RestController
@RequestMapping(path = "docente")
public class DocenteController {
	
	private final DocenteService service;

	public DocenteController(DocenteService service) {
		this.service = service;
	}
	
	

}
