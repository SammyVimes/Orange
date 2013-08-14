package com.danilov.orange;

import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.danilov.orange.model.PlayList;
import com.danilov.orange.model.Song;
import com.danilov.orange.util.BasePlayerActivity;
import com.danilov.orange.util.Utilities;

public class PlayerActivity extends BasePlayerActivity implements OnClickListener{

	private AudioPlayerService mAudioPlayerService;
	private Intent mAudioPlayerServiceIntent;
	private UpdateCurrentTrackTask updateCurrentTrackTask;
	private Player mPlayer;
	
	private ImageButton btnPlayPause;
	private ImageButton btnRight;
	private ImageButton btnLeft;
	private TextView time;
	private SeekBar timeLine;
	
	private static final String TAG = "PlayerActivity";
	

    private BroadcastReceiver audioPlayerBroadcastReceiver = new AudioPlayerBroadCastReceiver();
    private ServiceConnection serviceConnection = new AudioPlayerServiceConnection();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		btnPlayPause = (ImageButton) findViewById(R.id.btnPlayPause);
		btnRight = (ImageButton) findViewById(R.id.btnRight);
		btnLeft = (ImageButton) findViewById(R.id.btnLeft);
		time = (TextView) findViewById(R.id.time);
		timeLine = (SeekBar) findViewById(R.id.timeLine);
        timeLine.setOnSeekBarChangeListener(new TimeLineChangeListener());
        initButtons();
        if( updateCurrentTrackTask == null) {
            updateCurrentTrackTask = new UpdateCurrentTrackTask();
            updateCurrentTrackTask.execute();
        } else {
            Log.e(TAG, "updateCurrentTrackTask is not null" );
        }
	}
	
	private void initButtons() {
		btnPlayPause.setOnClickListener(this);
		btnRight.setOnClickListener(this);
		btnLeft.setOnClickListener(this);
	}
	

	@Override
    protected void onResume() {
        super.onResume();
        //bind to service
        mAudioPlayerServiceIntent = new Intent(this, AudioPlayerService.class);
        bindService(mAudioPlayerServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    	
    	audioPlayerBroadcastReceiver = new AudioPlayerBroadCastReceiver();
        IntentFilter filter = new IntentFilter(AudioPlayerService.UPDATE_PLAYLIST);
        registerReceiver(audioPlayerBroadcastReceiver, filter );
    }
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.player, menu);
		return true;
	}
	
	private void updatePlayPanel(final Song song) {
		
        runOnUiThread(new Runnable() {
            public void run() {
                long elapsedMillis = mPlayer.getCurrentPosition();
                String message = song.getTitle() + " - " + Utilities.milliSecondsToTimer(elapsedMillis);
                timeLine.setMax((int) mPlayer.getDuration());
                timeLine.setProgress((int) elapsedMillis);
                time.setText(message);
            }
        });
    }
	
	private class TimeLineChangeListener implements SeekBar.OnSeekBarChangeListener {
        private Timer delayedSeekTimer;
        
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if( fromUser ) {
                Log.d(TAG,"TimeLineChangeListener progress received from user: "+progress);
                
                scheduleSeek(progress);
                
                return;
            }
        }

        
        private void scheduleSeek(final int  progress) {
            if( delayedSeekTimer != null) {
                delayedSeekTimer.cancel();
            }
            delayedSeekTimer = new Timer();
            delayedSeekTimer.schedule(new TimerTask() {
                
                @Override
                public void run() {
                    Log.d(TAG,"Delayed Seek Timer run");
                    mAudioPlayerService.seek(progress);
                    updatePlayPanel(mPlayer.getCurrentSong());
                }
            }, 170);
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            Log.d(TAG,"TimeLineChangeListener started tracking touch");
            updateCurrentTrackTask.pause();
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            Log.d(TAG,"TimeLineChangeListener stopped tracking touch");
            updateCurrentTrackTask.unPause();
        }
        
    }
	
	private class UpdateCurrentTrackTask extends AsyncTask<Void, Song, Void> {

        public boolean stopped = true;
        public boolean paused = true;
        
        @Override
        protected Void doInBackground(Void... params) {
            while(!stopped) {
                if(!paused) {
                    Song currentTrack = mPlayer.getCurrentSong();
                    if( currentTrack != null ) {
                        publishProgress(currentTrack);
                    }
                }
                Utilities.sleep(250);
            }
            
            Log.d(TAG,"AsyncTask stopped");
            
            return null;
        }
        
        @Override
        protected void onProgressUpdate(Song... song) {
            if( stopped || paused ) {
                return; //to avoid glitches
            }
            updatePlayPanel(song[0]);
        }

        public void stop() {
            stopped = true;
        }
        
        public void pause() {
            this.paused = true;
        }

        public void unPause() {
            this.paused = false;
        }
    }
	
	private final class AudioPlayerServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName className, IBinder baBinder) {
            Log.d(TAG,"AudioPlayerServiceConnection: Service connected");
            mAudioPlayerService = ((AudioPlayerService.AudioPlayerBinder) baBinder).getService();
            mPlayer = mAudioPlayerService.getPlayer();
            PlayList list = new PlayList();
            list.add(new Song("girl", "/mnt/sdcard/audio/girl.mp3"));
            list.restart();
            mPlayer.setPlayList(list);
            startService(mAudioPlayerServiceIntent);
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.d(TAG,"AudioPlayerServiceConnection: Service disconnected");
            mAudioPlayerService = null;
        }
    }
	
	private void updatePlayPauseButtonState() {
        if(mPlayer.isPlaying()) {
        	btnPlayPause.setImageResource(R.drawable.btn_stop);
        } else {
        	btnPlayPause.setImageResource(R.drawable.btn_play);
        }
    }
	
	private class AudioPlayerBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG,"AudioPlayerBroadCastReceiver.onReceive action=" + intent.getAction());
            if( AudioPlayerService.UPDATE_PLAYLIST.equals( intent.getAction())) {
                //updatePlayQueue();
            }
        }
    }
	

	 private void onClickPlayPause() {
        if (mPlayer.isPlaying() ) {
            mPlayer.pause();
        } else {
            mPlayer.play();
        }
        updatePlayPauseButtonState();
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
	        case R.id.btnPlayPause:
	            onClickPlayPause();
	            break;
	        case R.id.btnRight:
	            mPlayer.nextSong();
	            updatePlayPauseButtonState();
	            break;
	        case R.id.btnLeft:
	        	mPlayer.previousSong();
	            break;
        }
	}

}
