package com.danilov.orange.model;

import java.util.LinkedList;
import java.util.List;

import com.danilov.orange.interfaces.Listable;

public class PlayList {
	
	private String name = "";
	
	private List<Song> playList;
	private Listable mListable;
	private Song mCurrentSong;
	private int currentSongPosition = 0;
	
	public PlayList() {
		playList = new LinkedList<Song>();
	}
	
	public PlayList(final Listable listable, List<Song> songs) {
		List<Song> playList = songs;
		this.mListable = listable;
		this.playList = playList;
		if (this.playList == null) {
			this.playList = new LinkedList<Song>();
		}
	}
	
	public Song next() {
		currentSongPosition++;
		if (currentSongPosition >= playList.size()) {
			currentSongPosition = 0;
			mCurrentSong = playList.get(0);
		} else {
			mCurrentSong = playList.get(currentSongPosition);
		}
		return mCurrentSong;
	}
	
	public Song previous() {
		currentSongPosition--;
		if (currentSongPosition < 0) {
			currentSongPosition = playList.size() - 1; 
		}
		mCurrentSong = playList.get(currentSongPosition);
		return mCurrentSong;
	}
	
	public void restart() {
		currentSongPosition = 0;
		if (!isEmpty()) {
			mCurrentSong = playList.get(0);
		}
	}
	
	public Song get(final int i) {
		return playList.get(i);
	}
	
	public Song getCurrentSong() {
		return mCurrentSong;
	}
	
	public int getCurrentSongPosition() {
		return currentSongPosition;
	}
	
	public Song setCurrentSong(final int pos) {
		if (pos >= 0 && pos < playList.size()) {
			currentSongPosition = pos;
			mCurrentSong = playList.get(pos);
			return mCurrentSong;
		}
		return null;
	}
	
	public Song setCurrentSong(final Song song) {
		int i = 0;
		for (Song _song : playList) {
			if (song == _song) {
				mCurrentSong = song;
				currentSongPosition = i;
				return mCurrentSong;
			}
			i++;
		}
		return null;
	}
	
	public int size() {
		return playList.size();
	}
	
	public void add(final Song song) {
		playList.add(song);
	}
	
	public boolean isEmpty() {
		return playList.size() == 0 ? true : false;
	}
	
	public Listable getListable() {
		return mListable;
	}
	
	public void toXML() {
		
	}
	
}
