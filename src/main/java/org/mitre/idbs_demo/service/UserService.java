package org.mitre.idbs_demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	private Map<String, Photo[]> initTestData() {
		Map<String, Photo[]> testData = new HashMap<String, Photo[]>();
		testData.put("user", new Photo[]{new Photo("http://i.imgur.com/Q4bI5.gif", "Surprised", "Demo User"),
				   new Photo("http://i.imgur.com/o7z5Y2K.gif", "Mlem Mlem", "Demo User")});
		testData.put("admin", new Photo[]{new Photo("http://i.imgur.com/i8tiqGI.gif", "Happy Doge", "Demo Admin"),
							   new Photo("http://i.imgur.com/HSOeg.gif", "Hurr durr hurr", "Demo Admin")});
		return testData;
	}
	
	public User getCurrentUser() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication == null || !(authentication instanceof OIDCAuthenticationToken)) return null;
		else {
			OIDCAuthenticationToken token = (OIDCAuthenticationToken) authentication;
			
			User curUser = repo.getUserBySubjectIssuer(token.getSub(), token.getIssuer());
			if( curUser != null ) return curUser;
			
			ServerConfiguration serverConfig = serverConfigService.getServerConfiguration(token.getIssuer());
			OIDCAuthenticationToken newToken = new OIDCAuthenticationToken(token.getIssuer(), token.getSub(),
				serverConfig, token.getIdTokenValue(), token.getAccessTokenValue(), token.getRefreshTokenValue());
			newToken.setAuthenticated(true);
			
			UserInfo info = fetcher.loadUserInfo(newToken);
			info.setSource(null);
			
			User u = repo.addUser(token.getSub(), token.getIssuer(), info);
			
			/* TEST DATA */
			Map<String, Photo[]> testData = initTestData();
			Photo[] photos = testData.get(u.getUserInfo().getPreferredUsername());
			for( Photo p : photos ) {
				u.addResource(p);
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
