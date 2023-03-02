package com.exprivia.demo.dto;

import java.time.LocalDate;

public class AssenzaDto {
	private LocalDate giornataAssenza;
	private boolean giustificata;
	
	public AssenzaDto() {}
	
	public AssenzaDto(LocalDate giornataAssenza, boolean giustificata) {
		super();
		this.giornataAssenza = giornataAssenza;
		this.giustificata = giustificata;
	}

	public LocalDate getGiornataAssenza() {
		return giornataAssenza;
	}

	public void setGiornataAssenza(LocalDate giornataAssenza) {
		this.giornataAssenza = giornataAssenza;
	}

	public boolean isGiustificata() {
		return giustificata;
	}

	public void setGiustificata(boolean giustificata) {
		this.giustificata = giustificata;
	}
}
