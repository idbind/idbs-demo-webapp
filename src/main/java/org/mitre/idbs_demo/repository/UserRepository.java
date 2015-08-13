package org.mitre.idbs_demo.repository;

import java.util.HashSet;
import java.util.Set;

import org.mitre.idbs_demo.model.User;
import org.mitre.openid.connect.model.UserInfo;

public class UserRepository {
	
	private static UserRepository userRepo;
	
	private Set<User> users;
	
	private User currentUser;
	
	private UserRepository() {
		users = new HashSet<User>();
		currentUser = null;
	}
	
	public static UserRepository getInstance() {
		if( userRepo == null )
			userRepo = new UserRepository();
		
		return userRepo;
	}
	
	public User addUser(String subject, String issuer, UserInfo info) {
		User u = new User(subject, issuer, info);
		users.add(u);
		return u;
	}
	
	public User getUserBySubjectIssuer(String subject, String issuer) {
		for( User u : users ) {
			if( u.verifyUser(subject, issuer) )
				return u;
		}
		return null;
	}
	
	public User getCurrentUser() {
		return currentUser;
	}
	
	public void setCurrentUser(User u) {
		currentUser = u;
	}
}
