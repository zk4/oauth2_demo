package com.qq;

import java.util.concurrent.ConcurrentHashMap;

public class ClientBean {
	private Integer id;
	private String secrectKey;
	private String grantType;
	private String redirectUrl;
	private ConcurrentHashMap<Integer, Token> tokens;

	public ConcurrentHashMap<Integer, Token> getTokens() {
		return tokens;
	}

	public ClientBean setTokens(ConcurrentHashMap<Integer, Token> tokens) {
		this.tokens = tokens;
		return this;
	}

	public Integer getId() {
		return id;
	}

	public ClientBean setId(Integer id) {
		this.id = id;
		return this;
	}

	public String getSecrectKey() {
		return secrectKey;
	}

	public ClientBean setSecrectKey(String secrectKey) {
		this.secrectKey = secrectKey;
		return this;
	}

	public String getGrantType() {
		return grantType;
	}

	public ClientBean setGrantType(String grantType) {
		this.grantType = grantType;
		return this;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ClientBean)) return false;

		ClientBean that = (ClientBean) o;

		if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
		if (getSecrectKey() != null ? !getSecrectKey().equals(that.getSecrectKey()) : that.getSecrectKey() != null)
			return false;
		if (getGrantType() != null ? !getGrantType().equals(that.getGrantType()) : that.getGrantType() != null)
			return false;
		return getRedirectUrl() != null ? getRedirectUrl().equals(that.getRedirectUrl()) : that.getRedirectUrl() == null;
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getSecrectKey() != null ? getSecrectKey().hashCode() : 0);
		result = 31 * result + (getGrantType() != null ? getGrantType().hashCode() : 0);
		result = 31 * result + (getRedirectUrl() != null ? getRedirectUrl().hashCode() : 0);
		return result;
	}

	public ClientBean setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
		return this;
	}
}
