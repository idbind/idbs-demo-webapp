package org.mitre.idbs_demo.web;

import org.mitre.idbs_demo.model.Identity;
import org.mitre.idbs_demo.model.TokenResponse;
import org.mitre.idbs_demo.service.AuthTokenService;
import org.mitre.idbs_demo.service.BoundIdentityService;
import org.mitre.idbs_demo.service.UserInfoService;
import org.mitre.openid.connect.client.UserInfoFetcher;
import org.mitre.openid.connect.model.OIDCAuthenticationToken;
import org.mitre.openid.connect.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController {
	
	@Value( "${client.id}" )
	private String clientId;
	@Value( "${client.secret}" )
	private String clientSecret;
	@Value( "${client.grantType}" )
	private String clientGrant;
	@Value( "${client.scope}" )
	private String clientScope;
	
	@Value( "${idbsQuery.issuer}" )
	private String issuer;
	@Value( "${idbsQuery.subject}" )
	private String subject;
	
	@Autowired
	private AuthTokenService tokenService;
	
	@Autowired
	private BoundIdentityService identityService;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@RequestMapping(value = "/getToken", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public TokenResponse getAuthorizationToken() {
		
		return tokenService.getAuthToken(clientId, clientSecret, clientGrant, clientScope);
	}
	
	@RequestMapping(value = "/getIdentities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Identity[] getBoundIdentities() {
		
		return identityService.getIdentities(issuer, subject);
	}
	
	@RequestMapping(value = "/getUserInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserInfo getUserInfo() {
		
		return userInfoService.getUserInfo();
	}
}
