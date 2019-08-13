package com.baidu;

import java.util.Map;

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
	public static Token newTokenFromMap(Map data){
		Token token2 = new Token();
		token2.setAccess_token((String) data.get("access_token"));
		token2.setRefresh_token((String) data.get("refresh_token"));
		token2.setScope((String) data.get("scope"));
		return token2;
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
