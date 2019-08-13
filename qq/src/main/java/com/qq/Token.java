package com.qq;

public class Token {
	private String access_token;
	private String refresh_token;
	private String scope = "read";

	public String getAccess_token() {
		return access_token;
	}

	public Token setAccess_token(String access_token) {
		this.access_token = access_token;
		return this;
	}



  	public String getRefresh_token() {
		return refresh_token;
	}

	public Token setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
		return this;
	}

	public String getScope() {
		return scope;
	}

	public Token setScope(String scope) {
		this.scope = scope;
		return this;
	}


}
