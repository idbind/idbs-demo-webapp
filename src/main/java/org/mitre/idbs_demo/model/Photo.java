package org.mitre.idbs_demo.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Photo {

	private String url;
	private String caption;
	private Date dateAdded;
	
	private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	
	public Photo(String url, String caption) {
		this.url = url;
		this.caption = caption;
		this.dateAdded = new Date();
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getCaption() {
		return caption;
	}
	
	public String getDateAdded() {
		return dateFormat.format(dateAdded);
	}
}
