package com.danilov.orange;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class AudioPlayerService extends Service implements OnCompletionListener {
	
	
	private static final String TAG = "AudioPlayerService";
	private final IBinder audioPlayerBinder = new AudioPlayerBinder();
	private Player mPlayer = new Player();
	
	public class AudioPlayerBinder extends Binder {
		
		public AudioPlayerService getService() {
			Log.v(TAG, "AudioPlayerBinder: getService() called");
			return AudioPlayerService.this;
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.v(TAG, "AudioPlayer: onBind() called");
        return audioPlayerBinder;
	}

	@Override
	public void onCompletion(MediaPlayer mMediaPlayer) {
		// TODO Auto-generated method stub
		
	}
	
	

}
