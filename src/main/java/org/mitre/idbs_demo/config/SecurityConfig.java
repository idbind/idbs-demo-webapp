package org.mitre.idbs_demo.config;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/home", true)
				.successHandler(successHandler())
				.usernameParameter("user")
				.passwordParameter("pass")
				.failureUrl("/login")
				.failureHandler(failureHandler())
				.permitAll()
				.and()
				.logout()
				.permitAll()
				.and()
		    .authorizeRequests()
		    	.antMatchers("/home", "/getToken").permitAll();
		http
			.csrf().disable();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		 .withUser("user").password("password").authorities("ROLE_USER").and()
		 .withUser("admin").password("password").authorities("ROLE_ADMIN", "ROLE_USER");
	}
	
	@Bean
	public AuthenticationSuccessHandler successHandler() {
		return new AuthenticationSuccessHandler() {
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException {
				System.out.println("login succeeded");
				RedirectStrategy strategy = new DefaultRedirectStrategy();
				strategy.sendRedirect(request, response, "/home");
			}
		};
	}
	
	@Bean
	public AuthenticationFailureHandler failureHandler() {
		return new AuthenticationFailureHandler() {
			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
				System.out.println("login failed");
			}
		};
	}
}
