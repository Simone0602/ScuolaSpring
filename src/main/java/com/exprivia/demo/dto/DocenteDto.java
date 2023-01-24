package com.exprivia.demo.dto;

public class DocenteDto {

	private long id;
	private String nome;
	private String cognome;
	private String mail;
	private String pas;
	private String materia;
	
	public DocenteDto() {}

	public DocenteDto(String nome, String cognome, String mail, String pas, String materia) {
		this.nome = nome;
		this.cognome = cognome;
		this.mail = mail;
		this.pas = pas;
		this.materia = materia;
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

	public String getMateria() {
		return materia;
	}

	public void setMateria(String materia) {
		this.materia = materia;
	}

	@Override
	public String toString() {
		return "DocenteDto [nome=" + getNome() + ", cognome=" + getCognome() + ", mail=" + getMail() + ", pas=" + getPas() + ", materia="
				+ getMateria() + "]";
	}
	
	
	
}
