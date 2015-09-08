package org.mitre.idbs_demo.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Photo {

	private String url;
	private String caption;
	private String author;
	private String dateAdded;
	
	private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	
	public Photo(String url, String caption, String author) {
		this.url = url;
		this.caption = caption;
		this.author = author;
		this.dateAdded = dateFormat.format(new Date());
	}
	
	/* For JSON mapping */
	public Photo() {}
	
	public String getUrl() {
		return url;
	}
	
	public String getCaption() {
		return caption;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getDateAdded() {
		return dateAdded;
	}
	
	@Override
	public String toString() {
		return "URL: "+url+"\nCaption: "+caption+"\nAuthor: "+author+"\nDate: "+dateAdded;
	}
	
	@Override
	public boolean equals(Object obj) {
		Photo p = (Photo)obj;
		return (p.url.equals(url) && p.caption.equals(caption) && p.author.equals(author) && p.dateAdded.equals(dateAdded));
	}
}
