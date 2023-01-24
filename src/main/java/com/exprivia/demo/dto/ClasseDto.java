package com.exprivia.demo.dto;

public class ClasseDto {
	private Long id;
	private String sezione;
	private String cordinatore;
	private String aula;

	public ClasseDto() {
	}

	public ClasseDto(Long id, String sezione, String cordinatore, String aula) {
		super();
		this.id = id;
		this.sezione = sezione;
		this.cordinatore = cordinatore;
		this.aula = aula;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "Classe [getId()=" + getId() + ", getSezione()=" + getSezione() + ", getCordinatore()="
				+ getCordinatore() + ", getAula()=" + getAula() + "]";
	}
}
