package com.danilov.orange.model;

import java.util.LinkedList;
import java.util.List;

import com.danilov.orange.interfaces.Listable;

public class ArtistProperty implements Listable{
	
	private String mArtistName = "";
	private int mSongsCount;
	private List<Song> mSongs;
	
	public ArtistProperty(final String artistName) {
		this(artistName, new LinkedList<Song>());
	}
	
	public ArtistProperty(final String artistName, final LinkedList<Song> songs) {
		mArtistName = artistName;
		mSongs = songs;
		mSongsCount = mSongs.size();
	}
	
	public String getAtristName() {
		return mArtistName;
	}
	
	public void setArtistName(final String artistName) {
		mArtistName = artistName;
	}
	
	public int getSongsCount() {
		return mSongsCount;
	}
	
	public void setSongs(final LinkedList<Song> songs) {
		mSongs = songs;
		mSongsCount = mSongs.size();
	}
	
	public void addSong(final Song song) {
		mSongs.add(song);
		mSongsCount = mSongs.size();
	}

	@Override
	public String getFirstLine() {
		return getAtristName();
	}

	@Override
	public String getSecondLine() {
		return Integer.toString(getSongsCount());
	}
	
	
}
