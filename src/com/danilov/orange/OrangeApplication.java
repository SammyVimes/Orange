package com.danilov.orange;

import java.util.List;

import com.danilov.orange.model.Album;
import com.danilov.orange.model.ArtistProperty;

public class OrangeApplication {

	private static OrangeApplication mInstance;
	private List<Album> mAlbums;
	private List<ArtistProperty> mArtistProperty;
	
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
	
	
	public void setArtistProperty(final List<ArtistProperty> artistProperty) {
		mArtistProperty = artistProperty;
	}
	
	public List<ArtistProperty> getArtistProperty() {
		return mArtistProperty;
	}
 }
