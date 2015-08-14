package org.mitre.idbs_demo.service;

import java.util.List;

import org.mitre.idbs_demo.model.Photo;
import org.mitre.idbs_demo.model.User;
import org.mitre.idbs_demo.repository.UserRepository;
import org.mitre.openid.connect.model.OIDCAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ResourceService {

	public List<Photo> getResources() {
		
		return UserRepository.getInstance().getCurrentUser().getResources();
	}
	
	public void addPhoto(String url, String caption, String author) {
		
		UserRepository.getInstance().getCurrentUser().addResource(new Photo(url, caption, author));
	}
}
