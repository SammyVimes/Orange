package com.danilov.orange;

import java.util.List;

import com.danilov.orange.model.Album;

public class OrangeApplication {

	private static OrangeApplication mInstance;
	private List<Album> mAlbums;
	
	public static OrangeApplication getInstance() {
		if (mInstance == null) {
			mInstance = new OrangeApplication();
		}
		return mInstance;
	}
	
	public void setAlbums(final List<Album> albums) {
		mAlbums = albums;
	}
	
	public List<Album> getAlbums() {
		return mAlbums;
	}
	
}
