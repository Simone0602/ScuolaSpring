package com.exprivia.demo.controller;

import java.io.UnsupportedEncodingException;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exprivia.demo.dto.ContattaciDto;
import com.exprivia.demo.mail.SendMailService;

@AllArgsConstructor
@RestController
@RequestMapping(path = "contattaci")
public class ContattaciController {
	
	private final SendMailService mailService;
	
	@PostMapping(path = "/send-mail")
	public ResponseEntity<String> sendSupportMail(@RequestBody ContattaciDto contattaciDto){
		try {
			String message = mailService.sendSupportMail(contattaciDto);
			return new ResponseEntity<>(message, HttpStatus.OK);
		} catch (UnsupportedEncodingException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
