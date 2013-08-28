package com.danilov.orange.model;

import java.util.List;

public class Album {

	private String mAlbumID;
	private String mAlbumName = "";
	private String mArtistName = "";
	private int mSongsCount;
	private List<Song> mSongs;
	
	public Album(final String albumId, final String albumName, 
			final String artistName, final List<Song> songs) {
		mAlbumID = albumId;
		mAlbumName = albumName;
		mArtistName = artistName;
		mSongs = songs;
		mSongsCount = mSongs.size();
	}
	
	public void addSong(final Song song) {
		mSongs.add(song);
		mSongsCount = mSongs.size();
	}

	public String getAlbumID() {
		return mAlbumID;
	}

	public void setAlbumID(String mAlbumID) {
		this.mAlbumID = mAlbumID;
	}

	public String getAlbumName() {
		return mAlbumName;
	}

	public void setAlbumName(String mAlbumName) {
		this.mAlbumName = mAlbumName;
	}

	public String getArtistName() {
		return mArtistName;
	}

	public void setArtistName(String mArtistName) {
		this.mArtistName = mArtistName;
	}

	public List<Song> getSongs() {
		return mSongs;
	}

	public void setSongs(List<Song> mSongs) {
		this.mSongs = mSongs;
	}
	
	@Override
	public String toString() {
		return mArtistName + ": " + mAlbumName;
	}
	
	
}
