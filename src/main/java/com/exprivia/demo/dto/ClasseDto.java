package com.exprivia.demo.dto;

public class ClasseDto {
	private String sezione;
	private String cordinatore;
	private String aula;

	public ClasseDto() {
	}

	public ClasseDto(String sezione, String cordinatore, String aula) {
		this.sezione = sezione;
		this.cordinatore = cordinatore;
		this.aula = aula;
	}

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public String getCordinatore() {
		return cordinatore;
	}

	public void setCordinatore(String cordinatore) {
		this.cordinatore = cordinatore;
	}

	public String getAula() {
		return aula;
	}

	public void setAula(String aula) {
		this.aula = aula;
	}
}
