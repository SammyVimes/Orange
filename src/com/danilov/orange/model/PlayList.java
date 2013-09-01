package com.danilov.orange.model;

import java.util.LinkedList;
import java.util.List;

public class PlayList {
	
	private String name = "";
	
	private List<Song> playList;
	private Song mCurrentSong;
	private int currentSongPosition = 0;
	
	public PlayList() {
		playList = new LinkedList<Song>();
	}
	
	public PlayList(List<Song> playList) {
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
	
	public void restart() {
		currentSongPosition = 0;
		if (!isEmpty()) {
			mCurrentSong = playList.get(0);
		}
	}
	
	public Song getCurrentSong() {
		return mCurrentSong;
	}
	
	public void add(final Song song) {
		playList.add(song);
	}
	
	public boolean isEmpty() {
		return playList.size() == 0 ? true : false;
	}
	
	public void toXML() {
		
	}
	
}
