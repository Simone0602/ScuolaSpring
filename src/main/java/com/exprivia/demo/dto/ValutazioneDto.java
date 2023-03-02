package com.exprivia.demo.dto;

import java.time.LocalDate;

public class ValutazioneDto {

	private String materia;
	private float voto;
	private LocalDate data;

	public ValutazioneDto() {
		super();
	}

	public ValutazioneDto(String materia, float voto, LocalDate data) {
		super();
		this.materia = materia;
		this.voto = voto;
		this.data = data;
	}

	public float getVoto() {
		return voto;
	}

	public void setVoto(float voto) {
		this.voto = voto;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public String getMateria() {
		return materia;
	}

	public void setMateria(String materia) {
		this.materia = materia;
	}

}
