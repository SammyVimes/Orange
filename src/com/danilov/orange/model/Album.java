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

	public String getmAlbumID() {
		return mAlbumID;
	}

	public void setmAlbumID(String mAlbumID) {
		this.mAlbumID = mAlbumID;
	}

	public String getmAlbumName() {
		return mAlbumName;
	}

	public void setmAlbumName(String mAlbumName) {
		this.mAlbumName = mAlbumName;
	}

	public String getmArtistName() {
		return mArtistName;
	}

	public void setmArtistName(String mArtistName) {
		this.mArtistName = mArtistName;
	}

	public List<Song> getmSongs() {
		return mSongs;
	}

	public void setmSongs(List<Song> mSongs) {
		this.mSongs = mSongs;
	}
	
	
	
	
}
