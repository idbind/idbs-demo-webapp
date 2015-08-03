package org.mitre.idbs_demo.service;

import java.util.Arrays;

import org.mitre.idbs_demo.model.Identity;
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
public class BoundIdentityService {
	
	@Value( "${idbsQueryEndpoint}" )
	private String queryEndpoint;
	
	@Value( "${repo.tokenKey}" )
	private String tokenKey;
	
	@Value( "${idbsQueryParams.issuer}" )
	private String issuerParam;
	
	@Value( "${idbsQueryParams.subject}" )
	private String subjectParam;
	
	public Identity[] getIdentities(String issuer, String subject) {
		
		TokenResponse authToken = TokenRepository.getInstance().retrieveToken(tokenKey);
		
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		requestHeaders.add("Authorization", authToken.toString());
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add(issuerParam, issuer);
		params.add(subjectParam, subject);
		
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, requestHeaders);
		
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<Identity[]> result = restTemplate.exchange(queryEndpoint, HttpMethod.POST, requestEntity, Identity[].class);
		
		System.out.println(result.getStatusCode() + "\n" + result.getBody().toString() + "\n" + result.toString());
		return result.getBody();
	}
}
