package com.danilov.orange;

import android.media.MediaPlayer;

public class Player {

	private MediaPlayer mMediaPlayer;
	
	private String songTitle;
	private String songPath;
	
	private boolean paused = true;
	
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
	
	public void play() {
		if (mMediaPlayer != null && paused) {
			mMediaPlayer.start();
			paused = !paused;
			return;
		} else if (mMediaPlayer != null) {
			release();
		}
		 
	}
	
	
	
	public void seek(int timeInMillis) {
        if(mMediaPlayer != null && mMediaPlayer.isPlaying()) {
        	mMediaPlayer.seekTo(timeInMillis);
        }
    }
	
	private void release() {
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}
	
	
}
