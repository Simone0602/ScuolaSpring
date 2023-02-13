package com.exprivia.demo.dto;

import java.time.LocalDate;

public class ValutazioneDto {

	private int voto;
	private LocalDate data;

	public ValutazioneDto() {
		super();
	}

	public ValutazioneDto(int voto, LocalDate data) {
		super();
		this.voto = voto;
		this.data = data;
	}

	public int getVoto() {
		return voto;
	}

	public void setVoto(int voto) {
		this.voto = voto;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

}
