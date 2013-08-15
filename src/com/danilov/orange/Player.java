package com.danilov.orange;

import java.io.IOException;

import com.danilov.orange.model.PlayList;
import com.danilov.orange.model.Song;
import com.danilov.orange.util.Utilities;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.util.Log;

public class Player implements OnCompletionListener {

	private static final String TAG = "Player";
	
	private Context context;
	
	private MediaPlayer mMediaPlayer;
	private PlayList mPlayList;
	private Song mCurrentSong;
	
	
	private boolean mPaused = false;
	
	public Player(final Context context) {
		mMediaPlayer = new MediaPlayer();
		this.context = context;
	}
	
	public void setPlayList(final PlayList playList) {
		this.mPlayList = playList;
	}
	
	public Song getCurrentSong() {
		return mCurrentSong;
	}
	
	public String getSongTitle() {
		return mCurrentSong.getTitle();
	}
	
	public Uri getSongPath() {
		return mCurrentSong.getPath();
	}
	
	public long getDuration() {
		return mMediaPlayer.getDuration();
	}
	
	public long getCurrentPosition() {
		return mMediaPlayer.getCurrentPosition();
	}
	
	public void nextSong() {
		mCurrentSong = mPlayList.next();
		play();
	}
	
	public void previousSong() {
		mCurrentSong = mPlayList.next();
		play();
	}
	
	public void play() {
		if (mMediaPlayer != null && mPaused) {
			mMediaPlayer.start();
			mPaused = false;
			return;
		} else if (mMediaPlayer != null) {
			release();
		}
		mCurrentSong = mPlayList.getCurrentSong();
		mMediaPlayer = new MediaPlayer();
		try {
			mMediaPlayer.setDataSource(context, mCurrentSong.getPath());
			mMediaPlayer.prepare();
			mMediaPlayer.start();
			mMediaPlayer.setOnCompletionListener(this);
		} catch (IOException e) {
			Log.e(TAG,"error trying to play " + mCurrentSong.getTitle() , e);
			Utilities.toaster(context, "error trying to play track: " + mCurrentSong.getTitle() + "\nError: " +
					e.getMessage());
		}
	}
	
	public void pause() {
		mPaused = true;
		mMediaPlayer.pause();
	}
	
	public boolean isPlaying() {
        if(mPlayList.isEmpty() || mMediaPlayer == null) {
            return false;
        }
        return mMediaPlayer.isPlaying();
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

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
