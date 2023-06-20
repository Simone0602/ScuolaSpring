package com.exprivia.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DocenteDto {

	private Long id;
	private String nome;
	private String cognome;
	private String mail;
	private String password;
	private List<String> materie;
	private String codiceFiscale;
	private List<String> sezioni;

}
