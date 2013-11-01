package com.danilov.orange.model;

import java.util.LinkedList;
import java.util.List;

import android.net.Uri;

import com.danilov.orange.interfaces.Listable;

public class ArtistProperty implements Listable{
	
	private String mArtistName = "";
	private int mSongsCount;
	private List<Song> mSongs;
	private List<Album> mAlbums;
	private Uri mThumbnailPath = null;
	
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
	
	public void setSongs(final List<Song> songs) {
		mSongs = songs;
		mSongsCount = mSongs.size();
	}
	
	public List<Song> getSongs() {
		return mSongs;
	}
	
	public void addSong(final Song song) {
		mSongs.add(song);
		mSongsCount = mSongs.size();
	}
	
	public void addAlbum(final Album album) {
		if (mAlbums == null) {
			mAlbums = new LinkedList<Album>();
		}
		mAlbums.add(album);
		mSongsCount += album.getSongsCount();
	}
	
	public List<Album> getAlbums() {
		return mAlbums;
	}

	@Override
	public String getFirstLine() {
		return getAtristName();
	}

	@Override
	public String getSecondLine() {
		return Integer.toString(getSongsCount());
	}
	
	public PlayList toPlayList() {
		PlayList playList = new PlayList(this, getSongs());
		return playList;
	}

	@Override
	public Uri getThumbnailPath() {
		return mThumbnailPath;
	}
	
	public void setThumbnailPath(final Uri path) {
		mThumbnailPath = path;
	}
	
	
}
