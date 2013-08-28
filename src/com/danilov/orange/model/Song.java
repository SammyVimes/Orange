package com.danilov.orange.model;

import android.net.Uri;

public class Song {
	
	private String mId;
	private String mTitle;
	private String mPath;
	private String mArtist;
	private String mAlbum;
	
	public Song(final String id, final String artist, final String title, final String path) {
		this.mId = id;
		this.mArtist = artist;
		this.mTitle = title;
		this.mPath = path;
	}
	
	public String getArtist() {
		return mArtist;
	}
	
	public void setArtist(final String artist) {
		this.mArtist = artist;
	}
	
	public String getAlbum() {
		return mAlbum;
	}
	
	public void setAlbum(final String album) {
		this.mAlbum = album;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}
	public Uri getPath() {
		return Uri.parse(mPath);
	}

	public void setPath(String path) {
		this.mPath = path;
	}
	
	@Override
	public String toString() {
		return mArtist + " - " + mTitle;
	}
	
}
