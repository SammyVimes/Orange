package com.danilov.orange;

import com.danilov.orange.util.IntentActions;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class AudioPlayerService extends Service{
	
	public static final String INTENT_BASE_NAME = "com.augusto.mymediaplayer.AudioPlayer";
    public static final String UPDATE_PLAYLIST = INTENT_BASE_NAME + ".PLAYLIST_UPDATED";
    public static final String QUEUE_TRACK = INTENT_BASE_NAME + ".QUEUE_TRACK";
    public static final String PLAY_TRACK = INTENT_BASE_NAME + ".PLAY_TRACK";
    public static final String QUEUE_ALBUM = INTENT_BASE_NAME + ".QUEUE_ALBUM";
	public static final String PLAY_ALBUM = INTENT_BASE_NAME + ".PLAY_ALBUM";
	
	
	private static final String TAG = "AudioPlayerService";
	private final IBinder audioPlayerBinder = new AudioPlayerBinder();
    private AudioPlayerBroadcastReceiver broadcastReceiver = new AudioPlayerBroadcastReceiver();
	private Player mPlayer;
	
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
	public void onCreate() {
		Log.v(TAG, "AudioPlayerService:onCreate() called");
		IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(IntentActions.INTENT_NEXT_SONG);
        intentFilter.addAction(IntentActions.INTENT_PLAY_PAUSE);
        intentFilter.addAction(IntentActions.INTENT_PREVIOUS_SONG);
        intentFilter.addAction(IntentActions.INTENT_SEEK);
        registerReceiver(broadcastReceiver, intentFilter);
        mPlayer = new Player(getApplicationContext(), new CompletionListener());
	}
	
	public void seek(final int progress) {
		if (mPlayer.getPlayList().isEmpty()) {
			return;
		}
		if (!mPlayer.isPlaying()) {
			mPlayer.play(false);
		}
		mPlayer.seek(progress);
	}
	
	@Override
	public void onDestroy() {
		unregisterReceiver(broadcastReceiver);
		Log.d(TAG, "Service: onDestroy() called");
		super.onDestroy();
	}
	
	public Player getPlayer() {
		return mPlayer;
	}
	
	private void sendIntent(final String action) {
		Intent intent = new Intent(action);
		sendBroadcast(intent);
	}
	
	private void playPause() {
		if (mPlayer.getPlayList().isEmpty()) {
			return;
		}
		if (mPlayer.isPlaying()) {
			mPlayer.pause();
		} else {
			mPlayer.play(false);
		}
		sendIntent(IntentActions.INTENT_FROM_SERVICE_PLAY_PAUSE);
	}
	
	private void nextSong() {
		if (mPlayer.getPlayList().isEmpty()) {
			return;
		}
		mPlayer.nextSong();
		sendIntent(IntentActions.INTENT_FROM_SERVICE_PLAY_PAUSE);
	}
	
	private void previousSong() {
		if (mPlayer.getPlayList().isEmpty()) {
			return;
		}
		mPlayer.previousSong();
		sendIntent(IntentActions.INTENT_FROM_SERVICE_PLAY_PAUSE);
	}
	
	private class AudioPlayerBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            long id = intent.getLongExtra("id", -1);
            Log.d(TAG, "Received intent for action " + intent.getAction() + " for id: " + id);
            if (IntentActions.INTENT_PLAY_PAUSE.equals(action)) {
            	playPause();
            } else if (IntentActions.INTENT_NEXT_SONG.equals(action)) {
            	nextSong();
            } else if (IntentActions.INTENT_PREVIOUS_SONG.equals(action)) {
            	previousSong();
            } else if (IntentActions.INTENT_SEEK.equals(action)) {
            	int progress = intent.getIntExtra(IntentActions.INTENT_EXTRA_INTEGER_SEEK, 0);
            	seek(progress);
            }
        }

    }
	
	public class CompletionListener {
		
		public void onCompletion() {
			nextSong();
		}
		
	}
	
	

}
