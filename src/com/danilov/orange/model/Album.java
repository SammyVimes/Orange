package com.danilov.orange.model;

import java.util.ArrayList;
import java.util.List;

import com.danilov.orange.interfaces.Listable;

public class Album implements Listable {

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
	
	public Album(final String albumId, final String albumName, 
			final String artistName) {
		mAlbumID = albumId;
		mAlbumName = albumName;
		mArtistName = artistName;
		mSongs = new ArrayList<Song>();
		mSongsCount = 0;
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
	
	public PlayList toPlayList() {
		PlayList playList = new PlayList(getSongs());
		return playList;
	}

	@Override
	public String getFirstLine() {
		return getArtistName();
	}

	@Override
	public String getSecondLine() {
		return getAlbumName();
	}
	
	
}
