package com.danilov.orange.model;

import android.net.Uri;

public class Song {
	
	private String title;
	private String path;
	
	public Song(final String title, final String path) {
		this.title = title;
		this.path = path;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Uri getPath() {
		return Uri.parse(path);
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
