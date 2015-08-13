package org.mitre.idbs_demo.service;

import org.mitre.idbs_demo.model.User;
import org.mitre.idbs_demo.repository.UserRepository;
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
	
	private UserRepository repo = UserRepository.getInstance();
	
	public UserInfo getUserInfo() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		System.out.println("debug-"+authentication.toString());
		
		if(authentication == null || !(authentication instanceof OIDCAuthenticationToken)) return null;
		else {
			OIDCAuthenticationToken token = (OIDCAuthenticationToken) authentication;
			
			ServerConfiguration serverConfig = serverConfigService.getServerConfiguration(token.getIssuer());
			OIDCAuthenticationToken newToken = new OIDCAuthenticationToken(token.getIssuer(), token.getSub(),
				serverConfig, token.getIdTokenValue(), token.getAccessTokenValue(), token.getRefreshTokenValue());
			newToken.setAuthenticated(true);
			
			UserInfo info = fetcher.loadUserInfo(newToken);
			info.setSource(null);
			
			User u = repo.addUser(token.getSub(), token.getIssuer(), info);
			
			/* TEST DATA */
			if( u.getUserInfo().getName().equals("Demo User") ) {
				u.addResource("Demo User test 1");
				u.addResource("Demo User test 2");
			}
			else {
				u.addResource("Demo Admin test 1");
				u.addResource("Demo Admin test 2");
			}
			
			repo.setCurrentUser(u);
			return info;
		}
	}
}
