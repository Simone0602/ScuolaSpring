package com.exprivia.demo.service;

import org.springframework.stereotype.Service;

import com.exprivia.demo.repository.DocenteRepo;

@Service
public class DocenteService {

	private final DocenteRepo drepository;

	public DocenteService(DocenteRepo drepository) {
		this.drepository = drepository;
	}
	
	
	
}
