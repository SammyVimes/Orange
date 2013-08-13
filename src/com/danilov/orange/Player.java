package com.danilov.orange;

import android.media.MediaPlayer;

public class Player {

	private MediaPlayer mMediaPlayer;
	private String songTitle;
	private String songPath;
	
	public Player() {
		mMediaPlayer = new MediaPlayer();
	}
	
	public void playSong(final String songTitle, final String songPath) {
		
	}
	
	public String getSongTitle() {
		return songTitle;
	}
	
	public String getSongPath() {
		return songPath;
	}
	
	public long getDuration() {
		return mMediaPlayer.getDuration();
	}
	
	public long getCurrentPosition() {
		return mMediaPlayer.getCurrentPosition();
	}
	
	
}
