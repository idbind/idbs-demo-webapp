package org.mitre.idbs_demo.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mitre.jose.keystore.JWKSetKeyStore;
import org.mitre.jwt.signer.service.impl.DefaultJWTSigningAndValidationService;
import org.mitre.jwt.signer.service.impl.JWKSetCacheService;
import org.mitre.oauth2.model.ClientDetailsEntity.AuthMethod;
import org.mitre.oauth2.model.RegisteredClient;
import org.mitre.openid.connect.client.OIDCAuthenticationFilter;
import org.mitre.openid.connect.client.OIDCAuthenticationProvider;
import org.mitre.openid.connect.client.service.impl.DynamicRegistrationClientConfigurationService;
import org.mitre.openid.connect.client.service.impl.DynamicServerConfigurationService;
import org.mitre.openid.connect.client.service.impl.HybridIssuerService;
import org.mitre.openid.connect.client.service.impl.PlainAuthRequestUrlBuilder;
import org.mitre.openid.connect.client.service.impl.StaticAuthRequestOptionsService;
import org.mitre.openid.connect.client.service.impl.StaticClientConfigurationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import com.google.common.collect.Sets;
import com.nimbusds.jose.JWSAlgorithm;

@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Value( "${authentication.entryPoint}" )
	private String authEntryPoint;
	
	@Value( "${issuerService.loginUrl}" )
	private String issuerLoginUrl;
	
	@Value( "${staticClient.id}" )
	private String staticId;
	@Value( "${staticClient.secret}" )
	private String staticSecret;
	@Value( "${staticClient.name}" )
	private String staticName;
	@Value( "#{'${staticClient.scope}'.split(',')}" )
	private List<String> staticScope;
	@Value( "#{'${staticClient.redirectUris}'.split(',')}" )
	private List<String> staticRedirects;
	@Value( "${staticClient.jwksUri}" )
	private String staticJwks;
	@Value( "${staticClient.introspection}" )
	private String staticInstrospection;
	
	@Value( "${client1.uri}" )
	private String client1;
	
	@Value( "${dynamicClient.name}" )
	private String dynamicName;
	@Value( "#{'${dynamicClient.scope}'.split(',')}" )
	private List<String> dynamicScope;
	@Value( "#{'${dynamicClient.redirectUris}'.split(',')}" )
	private List<String> dynamicRedirects;
	@Value( "${dynamicClient.jwksUri}" )
	private String dynamicJwks;
	@Value( "${dynamicClient.introspection}" )
	private String dynamicIntrospection;
	
	@Value( "${signerService.defaultId}" )
	private String defaultSignerId;
	@Value( "${signerService.defaultAlgorithm}" )
	private String defaultAlgorithm;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin()
				.loginPage("/resources/public/login.html")
				.permitAll()
				.and()
				.logout()
				.logoutSuccessUrl("/login")
				.permitAll()
				.and()
		    .authorizeRequests()
		    	.antMatchers("/","/home","/login","/resources/public/**","/resources/css/**","/resources/js/**").permitAll()
		    	.and()
		    .authorizeRequests()
		    	.anyRequest().authenticated()
		    	.and()
		    .addFilterBefore(openIdConnectAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class);
		http
			.csrf().disable();
	}
	
	@Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(openIdConnectAuthenticationProvider());
    }
	
    @Bean
    public LoginUrlAuthenticationEntryPoint authenticationEntryPoint() {
    	return new LoginUrlAuthenticationEntryPoint(authEntryPoint);
    }
	
	@Bean
	public OIDCAuthenticationFilter openIdConnectAuthenticationFilter() throws Exception {
		OIDCAuthenticationFilter filter = new OIDCAuthenticationFilter();
		
		filter.setAuthenticationManager(authenticationManager());
		
		filter.setIssuerService(hybridIssuerService());
		filter.setClientConfigurationService(dynamicClientConfigurationService());
		filter.setServerConfigurationService(dynamicServerConfigurationService());
		filter.setAuthRequestOptionsService(staticAuthRequestOptionsService());
		filter.setAuthRequestUrlBuilder(plainAuthRequestUrlBuilder());
		
		return filter;
	}
	
	@Bean
	public OIDCAuthenticationProvider openIdConnectAuthenticationProvider() {
		OIDCAuthenticationProvider authenticationProvider = new OIDCAuthenticationProvider();
		
		return authenticationProvider;
	}
	
	@Bean
	public HybridIssuerService hybridIssuerService() {
		HybridIssuerService issuerService = new HybridIssuerService();
		issuerService.setLoginPageUrl(issuerLoginUrl);
		return issuerService;
	}
	
	@Bean
	@Primary
	public DynamicServerConfigurationService dynamicServerConfigurationService() {
		return new DynamicServerConfigurationService();
	}
	
	@Bean
	public StaticClientConfigurationService staticClientConfigurationService() {
		StaticClientConfigurationService clientConfigurationService = new StaticClientConfigurationService();
		
		RegisteredClient client = new RegisteredClient();
		client.setClientId(staticId);
		client.setClientSecret(staticSecret);
		client.setClientName(staticName);
		client.setScope(Sets.newHashSet(staticScope));
		client.setTokenEndpointAuthMethod(AuthMethod.SECRET_BASIC);
		client.setRedirectUris(Sets.newHashSet(staticRedirects));
		client.setRequestObjectSigningAlg(JWSAlgorithm.RS256);
		client.setJwksUri(staticJwks);
		client.setAllowIntrospection(Boolean.parseBoolean(staticInstrospection));
		
		Map<String, RegisteredClient> clients = new HashMap<String, RegisteredClient>();
		clients.put(client1, client);
		clientConfigurationService.setClients(clients);
		
		/*
		 * Registered Client Service. Uncomment this to save dynamically registered clients out to a 
		 * file on disk (indicated by the filename property) or replace this with another implementation 
		 * of RegisteredClientService. This defaults to an in-memory implementation of RegisteredClientService 
		 * which will forget and re-register all clients on restart.
		 */
		// clientConfigurationService.setRegisteredClientService(new JsonFileRegisteredClientService("/tmp/simple-web-app-clients.json"));
		
		return clientConfigurationService;
	}
	
	@Bean
	@Primary
	public DynamicRegistrationClientConfigurationService dynamicClientConfigurationService() {
		DynamicRegistrationClientConfigurationService clientConfigurationService = new DynamicRegistrationClientConfigurationService();
		
		RegisteredClient client = new RegisteredClient();
		client.setClientName(dynamicName);
		client.setScope(Sets.newHashSet(dynamicScope));
		client.setTokenEndpointAuthMethod(AuthMethod.SECRET_BASIC);
		client.setRedirectUris(Sets.newHashSet(dynamicRedirects));
		client.setRequestObjectSigningAlg(JWSAlgorithm.RS256);
		client.setJwksUri(dynamicJwks);
		client.setAllowIntrospection(Boolean.parseBoolean(dynamicIntrospection));
		
		clientConfigurationService.setTemplate(client);
		
		/*
		 * Registered Client Service. Uncomment this to save dynamically registered clients out to a 
		 * file on disk (indicated by the filename property) or replace this with another implementation 
		 * of RegisteredClientService. This defaults to an in-memory implementation of RegisteredClientService 
		 * which will forget and re-register all clients on restart.
		 */
		// clientConfigurationService.setRegisteredClientService(new JsonFileRegisteredClientService("/tmp/simple-web-app-clients.json"));
		
		return clientConfigurationService;
	}
	
	@Bean
	public StaticAuthRequestOptionsService staticAuthRequestOptionsService() {
		return new StaticAuthRequestOptionsService();
	}
	
	@Bean
	public PlainAuthRequestUrlBuilder plainAuthRequestUrlBuilder() {
		return new PlainAuthRequestUrlBuilder();
	}
	
	@Bean
	public JWKSetCacheService jwkSetCacheService() {
		return new JWKSetCacheService();
	}
	
	@Bean
	public DefaultJWTSigningAndValidationService defaultSignerService() throws Exception {
		JWKSetKeyStore defaultKeyStore = new JWKSetKeyStore();
		defaultKeyStore.setLocation(new ClassPathResource("keystore.jwks"));
		
		DefaultJWTSigningAndValidationService signerService = new DefaultJWTSigningAndValidationService(defaultKeyStore);
		signerService.setDefaultSignerKeyId(defaultSignerId);
		signerService.setDefaultSigningAlgorithmName(defaultAlgorithm);
		
		return signerService;
	}
}
