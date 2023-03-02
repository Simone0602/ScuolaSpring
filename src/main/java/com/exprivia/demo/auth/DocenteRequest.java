package com.exprivia.demo.auth;

public class DocenteRequest {
	private String codiceFiscale;
	private String mail;
	private String password;
	
	public DocenteRequest(String codiceFiscale, String mail, String password) {
		super();
		this.codiceFiscale = codiceFiscale;
		this.mail = mail;
		this.password = password;
	}
	
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
	
}
