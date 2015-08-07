package org.mitre.idbs_demo.service;

import org.mitre.openid.connect.client.UserInfoFetcher;
import org.mitre.openid.connect.client.service.impl.DynamicServerConfigurationService;
import org.mitre.openid.connect.config.ServerConfiguration;
import org.mitre.openid.connect.model.OIDCAuthenticationToken;
import org.mitre.openid.connect.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {
	
	//TODO: this is only a band-aid
	@Autowired
	private DynamicServerConfigurationService serverConfigService;
	
	private UserInfoFetcher fetcher = new UserInfoFetcher();
	
	public UserInfo getUserInfo() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		System.out.println("debug - "+authentication.toString());
		
		if(authentication == null || !(authentication instanceof OIDCAuthenticationToken)) return null;
		else {
			OIDCAuthenticationToken token = (OIDCAuthenticationToken) authentication;
			
			ServerConfiguration serverConfig = serverConfigService.getServerConfiguration(token.getIssuer());
			OIDCAuthenticationToken newToken = new OIDCAuthenticationToken(token.getIssuer(), token.getSub(),
				serverConfig, token.getIdTokenValue(), token.getAccessTokenValue(), token.getRefreshTokenValue());
			newToken.setAuthenticated(true);
			
			UserInfo info = fetcher.loadUserInfo(newToken);
			info.setSource(null);
			return info;
		}
	}
}
