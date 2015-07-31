package org.mitre.idbs_demo.web;

import org.mitre.idbs_demo.model.TokenResponse;
import org.mitre.idbs_demo.service.AuthTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController {
	
	@Autowired
	private AuthTokenService tokenService;
	
	/*@RequestMapping(value = "/")
	public ModelAndView homeView() {
		return new ModelAndView("home");
	}*/
	
	@RequestMapping(value = "/getToken", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public TokenResponse getAuthorizationToken() {
		
		//TODO: Where do these arguments come from?
		return tokenService.getAuthToken("client", "secret", "client_credentials", "org.mitre.idbind.query");
	}
}
