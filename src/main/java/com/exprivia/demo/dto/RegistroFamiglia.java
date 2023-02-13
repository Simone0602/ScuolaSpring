package com.exprivia.demo.dto;

import java.util.HashMap;
import java.util.List;

import com.exprivia.demo.enums.Materie;

public class RegistroFamiglia {
	private HashMap<Materie, List<ValutazioneDto>> listaVoti_materie;

	public RegistroFamiglia() {
	}

	public RegistroFamiglia(StudenteDto studenteDto, HashMap<Materie, List<ValutazioneDto>> listaVoti_materie) {
		super();
		this.listaVoti_materie = listaVoti_materie;
	}

	public HashMap<Materie, List<ValutazioneDto>> getListaVoti_materie() {
		return listaVoti_materie;
	}

	public void setListaVoti_materie(HashMap<Materie, List<ValutazioneDto>> listaVoti_materie) {
		this.listaVoti_materie = listaVoti_materie;
	}

}
