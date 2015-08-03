package org.mitre.idbs_demo.model;

public class Identity {
	
	private String issuer;
	private String subject;
	
	public Identity() {}
	
	public Identity(String issuer, String subject) {
		this.issuer = issuer;
		this.subject = subject;
	}
	
	public String getIssuer() {
		return issuer;
	}
	
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
}
