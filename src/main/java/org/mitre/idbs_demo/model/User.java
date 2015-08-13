package org.mitre.idbs_demo.model;

import java.util.ArrayList;
import java.util.List;

import org.mitre.openid.connect.model.UserInfo;

public class User {
	
	private String subject;
	private String issuer;
	private UserInfo info;
	private List<String> resources;
	
	public User(String subject, String issuer, UserInfo info) {
		this.subject = subject;
		this.issuer = issuer;
		this.info = info;
		resources = new ArrayList<String>();
	}
	
	public String getSubject() {
		return subject;
	}
	
	public String getIssuer() {
		return issuer;
	}
	
	public boolean verifyUser(String subject, String issuer) {
		if( this.subject.equals(subject) && this.issuer.equals(issuer) )
			return true;
		return false;
	}
	
	public UserInfo getUserInfo() {
		return info;
	}
	
	/* RESOURCE METHODS */
	public void addResource(String s) {
		resources.add(s);
	}
	
	public List<String> getResources() {
		return resources;
	}
}
