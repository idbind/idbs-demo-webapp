package org.mitre.idbs_demo.config;

import org.mitre.openid.connect.web.UserInfoInterceptor;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcAutoConfigurationAdapter {
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/home").setViewName("home");
		registry.addViewController("/").setViewName("home");
		registry.addViewController("/login").setViewName("login");
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry intRegistry) {
		UserInfoInterceptor userInfo = new UserInfoInterceptor();
		intRegistry.addInterceptor(userInfo);
	}
}
