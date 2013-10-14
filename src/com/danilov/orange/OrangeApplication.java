package com.danilov.orange;

import java.util.List;

import android.content.Context;

import com.danilov.orange.model.Album;
import com.danilov.orange.model.ArtistProperty;

public class OrangeApplication {

	private static OrangeApplication mInstance;
	private static Context mContext;
	private List<Album> mAlbums;
	private List<ArtistProperty> mArtistProperty;
	
	public static OrangeApplication getInstance() {
		if (mInstance == null) {
			mInstance = new OrangeApplication();
		}
		return mInstance;
	}
	
	private OrangeApplication() {
	}
	
	public static Context getContext() {
		return mContext;
	}
	
	public static void setContext(final Context context) {
		mContext = context;
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
