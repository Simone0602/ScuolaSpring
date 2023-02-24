package com.exprivia.demo.auth;

public class AuthenticationResponse {
	private String token;
	private String utenza;

	public AuthenticationResponse(String token, String utenza) {
		super();
		this.token = token;
		this.utenza = utenza;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUtenza() {
		return utenza;
	}

	public void setUtenza(String utenza) {
		this.utenza = utenza;
	}
	
}
