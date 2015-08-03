package org.mitre.idbs_demo.repository;

import java.util.HashMap;
import java.util.Map;

import org.mitre.idbs_demo.model.TokenResponse;

public class TokenRepository {
	
	private static TokenRepository repo;
	
	private Map<String, TokenResponse> tokens;
	
	private TokenRepository() {
		tokens = new HashMap<String, TokenResponse>();
	}
	
	public static TokenRepository getInstance() {
		if( repo == null )
			repo = new TokenRepository();
		
		return repo;
	}
	
	public void saveToken(String key, TokenResponse token) {
		tokens.put(key, token);
	}
	
	public TokenResponse retrieveToken(String key) {
		return tokens.get(key);
	}
}
