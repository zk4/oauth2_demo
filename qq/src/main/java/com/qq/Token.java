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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Token)) return false;

		Token token = (Token) o;

		if (!getAccess_token().equals(token.getAccess_token())) return false;
		if (!getRefresh_token().equals(token.getRefresh_token())) return false;
		return getScope().equals(token.getScope());
	}

	@Override
	public int hashCode() {
		int result = getAccess_token().hashCode();
		result = 31 * result + getRefresh_token().hashCode();
		result = 31 * result + getScope().hashCode();
		return result;
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
