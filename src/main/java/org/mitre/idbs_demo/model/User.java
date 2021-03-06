package org.mitre.idbs_demo.model;

import java.util.ArrayList;
import java.util.List;

import org.mitre.openid.connect.model.UserInfo;

public class User {
	
	private String subject;
	private String issuer;
	private UserInfo info;
	private List<Photo> resources;
	private boolean showContent;
	
	public User(String subject, String issuer, UserInfo info) {
		this.subject = subject;
		this.issuer = issuer;
		this.info = info;
		resources = new ArrayList<Photo>();
		showContent = false;
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
	public void addResource(Photo p) {
		resources.add(p);
	}

	public void deleteResource(Photo p) {
		System.out.println(resources.remove(p));
	}
	
	public List<Photo> getResources() {
		return resources;
	}
	
	public boolean getShowContent() {
		return showContent;
	}
	
	public void setShowContent(boolean showContent) {
		this.showContent = showContent;
	}
	
	@Override
	public boolean equals(Object obj) {
		User u = (User) obj;
		return (this.subject.equals(u.getSubject()) && this.issuer.equals(u.getIssuer()));
	}
}
