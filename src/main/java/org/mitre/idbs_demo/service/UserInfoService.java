package org.mitre.idbs_demo.service;

import org.mitre.openid.connect.client.UserInfoFetcher;
import org.mitre.openid.connect.model.OIDCAuthenticationToken;
import org.mitre.openid.connect.model.UserInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {
	
	private UserInfoFetcher fetcher = new UserInfoFetcher();
	
	public UserInfo getUserInfo() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		System.out.println("debug - "+authentication.toString());
		
		if(authentication == null || !(authentication instanceof OIDCAuthenticationToken)) return null;
		else {
			OIDCAuthenticationToken token = (OIDCAuthenticationToken) authentication;
			return fetcher.loadUserInfo(token);
		}
	}
}
