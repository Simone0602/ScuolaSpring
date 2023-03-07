package com.exprivia.demo.dto;

import java.util.List;

public class DocenteDto {

	private Long id;
	private String nome;
	private String cognome;
	private String mail;
	private String password;
	private List<String> materie;
	private String codiceFiscale;
	private List<String> sezioni;

	public DocenteDto() {
	}

	public DocenteDto(Long id, String nome, String cognome, String mail, String password, List<String> materie,
			String codiceFiscale, List<String> sezioni) {
		super();
		this.id = id;
		this.nome = nome;
		this.cognome = cognome;
		this.mail = mail;
		this.password = password;
		this.materie = materie;
		this.codiceFiscale = codiceFiscale;
		this.sezioni = sezioni;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getMaterie() {
		return materie;
	}

	public void setMaterie(List<String> materie) {
		this.materie = materie;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public List<String> getSezioni() {
		return sezioni;
	}

	public void setSezioni(List<String> sezioni) {
		this.sezioni = sezioni;
	}

}
