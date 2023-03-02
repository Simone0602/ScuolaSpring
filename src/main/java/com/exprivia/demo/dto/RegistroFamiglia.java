package com.exprivia.demo.dto;

import java.util.List;

public class RegistroFamiglia {
	private List<ValutazioneDto> listaVoti;

	public RegistroFamiglia() {
	}

	public RegistroFamiglia(StudenteDto studenteDto, List<ValutazioneDto> listaVoti) {
		super();
		this.listaVoti = listaVoti;
	}

	public List<ValutazioneDto> getListaVoti() {
		return listaVoti;
	}

	public void setListaVoti(List<ValutazioneDto> listaVoti) {
		this.listaVoti = listaVoti;
	}

}
