package org.mitre.idbs_demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenResponse {
	
	private String accessToken;
	private String tokenType;
	
	public TokenResponse() {}
	
	public TokenResponse(@JsonProperty("access_token") String accessToken, @JsonProperty("token_type") String tokenType) {
		this.accessToken = accessToken;
		this.tokenType = tokenType;
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public String getTokenType() {
		return tokenType;
	}
	
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	
	@Override
	public String toString() {
		return new String(tokenType + " " + accessToken);
	}
}
