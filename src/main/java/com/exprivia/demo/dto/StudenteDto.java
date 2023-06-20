package com.exprivia.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudenteDto {

	private long id;
	private String nome;
	private String cognome;
	private String mail;
	private String password;
	private String userCode;
	private String sezione;
}
