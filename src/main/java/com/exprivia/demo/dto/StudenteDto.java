package com.exprivia.demo.dto;

public class StudenteDto {

	private long id;
	private String nome;
	private String cognome;
	private String mail;
	private String pas;
	private String userCode;
	private String sezione;

	public StudenteDto() {
	}

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public String getPas() {
		return pas;
	}

	public void setPas(String pas) {
		this.pas = pas;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	@Override
	public String toString() {
		return "Studente [getId()=" + getId() + ", getNome()=" + getNome() + ", getCognome()=" + getCognome()
				+ ", getMail()=" + getMail() + ", getPas()=" + getPas() + ", getUser()=" + getUserCode() + "]";
	}
}
