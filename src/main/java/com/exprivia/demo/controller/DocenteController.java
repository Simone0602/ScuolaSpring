package com.exprivia.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exprivia.demo.service.DocenteService;

@RestController
@RequestMapping (value = "/doce")
public class DocenteController {
	
	private final DocenteService dservice;

	public DocenteController(DocenteService dservice) {
		this.dservice = dservice;
	}
	
	

}
