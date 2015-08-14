package org.mitre.idbs_demo.service;

import java.util.ArrayList;
import java.util.List;

import org.mitre.idbs_demo.model.Identity;
import org.mitre.idbs_demo.model.Photo;
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
public class UserService {
	
	//TODO: this is only a band-aid
	@Autowired
	private DynamicServerConfigurationService serverConfigService;
	
	@Autowired
	private BoundIdentityService identityService;
	
	private UserInfoFetcher fetcher = new UserInfoFetcher();
	
	private UserRepository repo = UserRepository.getInstance();
	
	public User getCurrentUser() {
		
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
				u.addResource(new Photo("http://i.imgur.com/Q4bI5.gif", "Surprised", "Demo User"));
				u.addResource(new Photo("http://i.imgur.com/o7z5Y2K.gif", "Mlem Mlem", "Demo User"));
			}
			else {
				u.addResource(new Photo("http://i.imgur.com/i8tiqGI.gif", "Happy Doge", "Demo Admin"));
				u.addResource(new Photo("http://i.imgur.com/HSOeg.gif", "Hurr durr hurr", "Demo Admin"));
			}
			
			repo.setCurrentUser(u);
			return u;
		}
	}
	
	public List<User> getBoundUsers() {
		
		List<Identity> ids = identityService.getIdentities();
		List<User> boundUsers = new ArrayList<User>();
		
		for( Identity id : ids ) {
			User u = repo.getUserBySubjectIssuer(id.getSubject(), id.getIssuer());
			if( u != null && !u.equals(repo.getCurrentUser()) ) boundUsers.add(u);
		}
		
		return boundUsers;
	}
}
