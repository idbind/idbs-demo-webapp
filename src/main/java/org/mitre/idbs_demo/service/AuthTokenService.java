package org.mitre.idbs_demo.service;

import org.mitre.idbs_demo.model.TokenResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthTokenService {
	
	public TokenResponse getAuthToken(String id, String secret, String grantType, String scope) {
		
		final String uri = "http://localhost:8080/openid-connect-server-webapp/token";
		
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("client_id", id);
		params.add("client_secret", secret);
		params.add("grant_type", grantType);
		params.add("scope", scope);
		
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, requestHeaders);
		
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<TokenResponse> result = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, TokenResponse.class);
		
		System.out.println(result.getStatusCode() + "\n" + result.getBody().toString() + "\n" + result.toString());
		return result.getBody();
	}
}
