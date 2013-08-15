package com.danilov.orange;

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
        intentFilter.addAction(PLAY_TRACK);
        intentFilter.addAction(QUEUE_TRACK);
        intentFilter.addAction(PLAY_ALBUM);
        intentFilter.addAction(QUEUE_ALBUM);
        registerReceiver(broadcastReceiver, intentFilter);
        mPlayer = new Player(getApplicationContext());
	}
	
	public void seek(final int progress) {
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
	
	private class AudioPlayerBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            long id = intent.getLongExtra("id", -1);
            Log.d(TAG, "Received intent for action " + intent.getAction() + " for id: " + id);
//
//            if( PLAY_ALBUM.equals(action)) {
//                playAlbum(id);
//            } else if( QUEUE_ALBUM.equals(action)) {
//                queueAlbum(id);
//            } else if( PLAY_TRACK.equals(action)) {
//                playTrack(id);
//            } else if( QUEUE_TRACK.equals(action)) {
//                queueTrack(id);
//            } else {
//                Log.d(TAG, "Action not recognized: " + action);
//            }
        }

    }
	
	

}
