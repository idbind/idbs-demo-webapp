package org.mitre.idbs_demo.web;

import org.mitre.idbs_demo.model.Identity;
import org.mitre.idbs_demo.model.TokenResponse;
import org.mitre.idbs_demo.service.AuthTokenService;
import org.mitre.idbs_demo.service.BoundIdentityService;
import org.mitre.idbs_demo.service.UserInfoService;
import org.mitre.openid.connect.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
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
	
	@RequestMapping({"", "/", "/home"})
    public String home() {
        return "/resources/home.html";
    }
	
	@RequestMapping("/login")
    public String login() {
        return "/resources/public/login.html";
    }
	
	@ResponseBody
	@RequestMapping(value = "/getToken", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public TokenResponse getAuthorizationToken() {
		
		return tokenService.getAuthToken(clientId, clientSecret, clientGrant, clientScope);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getIdentities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Identity[] getBoundIdentities() {
		
		return identityService.getIdentities(issuer, subject);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getUserInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserInfo getUserInfo() {
		
		return userInfoService.getUserInfo();
	}
}
