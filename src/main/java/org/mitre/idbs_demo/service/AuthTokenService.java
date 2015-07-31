package org.mitre.idbs_demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

import org.mitre.idbs_demo.model.TokenResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
		
		//String body = "client_id=client&client_secret=secret&grant_type=client_credentials&scope=org.mitre.idbind.query";
		
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, requestHeaders);
		
		RestTemplate restTemplate = new RestTemplate();
		/*List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new MappingJackson2HttpMessageConverter());
		messageConverters.add(new StringHttpMessageConverter());
		restTemplate.setMessageConverters(messageConverters);*/
		
		ResponseEntity<TokenResponse> result = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, TokenResponse.class);
		
		System.out.println(result.getStatusCode() + "\n" + result.getBody().toString() + "\n" + result.toString());
		return result.getBody();
		//return new TokenResponse("eyJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJjbGllbnQiLCJpc3MiOiJodHRwOlwvXC9sb2NhbGhvc3Q6ODA4MFwvb3BlbmlkLWNvbm5lY3Qtc2VydmVyLXdlYmFwcFwvIiwiZXhwIjoxNDM4MjkyMDU0LCJpYXQiOjE0MzgyODg0NTQsImp0aSI6ImViZmJjMGNiLTY2YjgtNDUzMC1hZmVlLWViOWEwYWI0NDNkOSJ9.1JKxZ3TZAQcSgi45U_VdZKGd4yQ76bDAgzoeXHJaRohjWuQph7Yo_4fE5CGjDvoDsmsUu_a9OY0oTjK-DwlSlQbHSkH56XLV_SaqIx7JwRfahNcLqDcgctuQxWd2ztPvF21r4l-HKFjECQ0b7YLnr1AoOTyChlEZv9-2ln6baSY", "Bearer");
	}
}
