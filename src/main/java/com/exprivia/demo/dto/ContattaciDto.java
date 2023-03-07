package com.exprivia.demo.dto;

public class ContattaciDto {
	private String mail;
	private String descrizione;
	
	public ContattaciDto(String mail, String descrizione) {
		super();
		this.mail = mail;
		this.descrizione = descrizione;
	}
	
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
}
