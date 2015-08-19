package org.mitre.idbs_demo.service;

import org.mitre.idbs_demo.model.TokenResponse;
import org.mitre.idbs_demo.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Value;
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
	
	@Value( "${app.rootUrl}" )
	private String rootUrl;
	
	@Value( "${tokenEndpoint}" )
	private String tokenEndpoint;
	
	@Value( "${clientParams.id}" )
	private String idParam;
	@Value( "${clientParams.secret}" )
	private String secretParam;
	@Value( "${clientParams.grantType}" )
	private String grantParam;
	@Value( "${clientParams.scope}" )
	private String scopeParam;
	
	@Value( "${repo.tokenKey}" )
	private String tokenKey;
	
	public TokenResponse getAuthToken(String id, String secret, String grantType, String scope) {
		
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add(idParam, id);
		params.add(secretParam, secret);
		params.add(grantParam, grantType);
		params.add(scopeParam, scope);
		
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, requestHeaders);
		
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<TokenResponse> result = restTemplate.exchange(rootUrl+tokenEndpoint, HttpMethod.POST, requestEntity, TokenResponse.class);
		
		TokenRepository.getInstance().saveToken(tokenKey, result.getBody());
		System.out.println(result.getBody());
		return result.getBody();
	}
}
