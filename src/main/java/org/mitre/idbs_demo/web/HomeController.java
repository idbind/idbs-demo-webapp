package org.mitre.idbs_demo.web;

import java.util.List;

import org.mitre.idbs_demo.model.Identity;
import org.mitre.idbs_demo.model.Photo;
import org.mitre.idbs_demo.model.TokenResponse;
import org.mitre.idbs_demo.model.User;
import org.mitre.idbs_demo.service.AuthTokenService;
import org.mitre.idbs_demo.service.BoundIdentityService;
import org.mitre.idbs_demo.service.ResourceService;
import org.mitre.idbs_demo.service.UserService;
import org.mitre.openid.connect.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	/*@Value( "${idbsQuery.issuer}" )
	private String issuer;
	@Value( "${idbsQuery.subject}" )
	private String subject;*/
	
	@Autowired
	private AuthTokenService tokenService;
	@Autowired
	private BoundIdentityService identityService;
	@Autowired
	private UserService userService;
	@Autowired
	private ResourceService resourceService;
	
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
	@RequestMapping(value = "/getBoundUsers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<User> getBoundUsers() {
		
		return userService.getBoundUsers();
	}
	
	@ResponseBody
	@RequestMapping(value = "/getUser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public User getCurrentUser() {
		
		return userService.getCurrentUser();
	}
	
	@ResponseBody
	@RequestMapping(value = "/getResources", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Photo> getResources() {
		
		return resourceService.getResources();
	}
	
	@ResponseBody
	@RequestMapping(value = "/addPhoto", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void addPhoto(@RequestParam("url") String url, @RequestParam("caption") String caption) {
		
		resourceService.addPhoto(url, caption);
		//return "/resources/home.html";
	}
}
