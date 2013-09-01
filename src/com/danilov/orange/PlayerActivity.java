package com.danilov.orange;

import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.IntentSender.SendIntentException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import com.danilov.orange.task.CacheCreateTask;
import com.danilov.orange.util.BasePlayerActivity;
import com.danilov.orange.util.IntentActions;
import com.danilov.orange.util.Utilities;

public class PlayerActivity extends BasePlayerActivity implements OnClickListener{

	static final int UPDATE_INTERVAL = 250;
	
	private AudioPlayerService mAudioPlayerService;
	private Intent mAudioPlayerServiceIntent;
	private UpdateCurrentTrackTask updateCurrentTrackTask;
	private Player mPlayer;
	
    private Timer waitForAudioPlayertimer;
	private Handler handler = new Handler();
    
	private ImageButton btnPlayPause;
	private ImageButton btnRight;
	private ImageButton btnLeft;
	private TextView time;
	private TextView songTitle;
	private SeekBar timeLine;
	private PlayerState state = PlayerState.PAUSED;
	
	public enum PlayerState {
		PLAYING, PAUSED;
	}
	
	private static final String TAG = "PlayerActivity";
	

    private BroadcastReceiver audioPlayerBroadcastReceiver = new AudioPlayerBroadcastReceiver();
    private ServiceConnection serviceConnection = new AudioPlayerServiceConnection();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		btnPlayPause = (ImageButton) findViewById(R.id.btnPlayPause);
		btnRight = (ImageButton) findViewById(R.id.btnRight);
		btnLeft = (ImageButton) findViewById(R.id.btnLeft);
		time = (TextView) findViewById(R.id.time);
		songTitle = (TextView) findViewById(R.id.songTitle);
		timeLine = (SeekBar) findViewById(R.id.timeLine);
	}
	
	public void initControls() {
		btnPlayPause.setOnClickListener(this);
		btnRight.setOnClickListener(this);
		btnLeft.setOnClickListener(this);
		timeLine.setOnSeekBarChangeListener(new TimeLineChangeListener());
	}
	

	@Override
    protected void onResume() {
        super.onResume();
        //bind to service
        mAudioPlayerServiceIntent = new Intent(this, AudioPlayerService.class);
        bindService(mAudioPlayerServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        waitForAudioPlayertimer = new Timer();
    	audioPlayerBroadcastReceiver = new AudioPlayerBroadcastReceiver();
        IntentFilter filter = new IntentFilter(IntentActions.INTENT_FROM_SERVICE_PLAY_PAUSE);
        registerReceiver(audioPlayerBroadcastReceiver, filter );
        refreshScreen();
    }
	
	@Override
    protected void onPause() {
        unregisterReceiver(audioPlayerBroadcastReceiver);
        audioPlayerBroadcastReceiver = null;
        waitForAudioPlayertimer.cancel();
        if (updateCurrentTrackTask != null) {
        	updateCurrentTrackTask.cancel(true);
        	updateCurrentTrackTask.stop();
        }
        updateCurrentTrackTask = null;
        unbindService(serviceConnection);
        super.onPause();
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
                timeLine.setMax((int) mPlayer.getDuration());
                timeLine.setProgress((int) elapsedMillis);
                time.setText(Utilities.milliSecondsToTimer(elapsedMillis));
                songTitle.setText(song.getTitle());
                if (mPlayer.isPlaying()) {
                	if (state == PlayerState.PAUSED) {
                		updatePlayPauseButtonState();
                	}
                } else {
                	if (state == PlayerState.PLAYING) {
                		updatePlayPauseButtonState();
                	}
                }
            }
        });
    }
	
	private class TimeLineChangeListener implements SeekBar.OnSeekBarChangeListener {
        private Timer delayedSeekTimer;
        private int progressFromUser = 0;
        
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser) {
                Log.d(TAG,"TimeLineChangeListener progress received from user: "+progress);
                progressFromUser = progress;
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
                    Intent intent = new Intent(IntentActions.INTENT_SEEK);
                    intent.putExtra(IntentActions.INTENT_EXTRA_INTEGER_SEEK, progress);
                    sendIntentToService(intent);
                }
            }, 5);
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            Log.d(TAG,"TimeLineChangeListener started tracking touch");
            if (updateCurrentTrackTask != null) {
            	updateCurrentTrackTask.pause();
            }
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            Log.d(TAG,"TimeLineChangeListener stopped tracking touch");
            scheduleSeek(progressFromUser);
            if (updateCurrentTrackTask != null) {
            	updateCurrentTrackTask.unPause();
            }
        }
        
    }
	
	private class UpdateCurrentTrackTask extends AsyncTask<Void, Song, Void> {

        public boolean stopped = false;
        public boolean paused = false;
        
        @Override
        protected Void doInBackground(Void... params) {
            while(!stopped) {
                if(!paused) {
                    Song currentSong = mPlayer.getCurrentSong();
                    if (currentSong != null ) {
                        publishProgress(currentSong);
                    } else {
                    	pause();
                    }
                }
                Utilities.sleep(350);
            }
            Log.d(TAG, this.toString() + ": AsyncTask stopped");
            return null;
        }
        
        @Override
        protected void onProgressUpdate(Song... song) {
            if( stopped || paused ) {
                return; //to avoid glitches
            }
        	if (!mPlayer.isPlaying()) {
        		pause();
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
	
	public void onNotPlaying() {
		time.setText("-:-");
		songTitle.setText("-");
	}
	
	private final class AudioPlayerServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName className, IBinder baBinder) {
            Log.d(TAG,"AudioPlayerServiceConnection: Service connected");
            initControls();
            mAudioPlayerService = ((AudioPlayerService.AudioPlayerBinder) baBinder).getService();
            mPlayer = mAudioPlayerService.getPlayer();
            if (mPlayer.getPlayList() == null) {
	            PlayList list = new PlayList();
	            mPlayer.setPlayList(list);
            }
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
    		state = PlayerState.PLAYING;
        } else {
        	btnPlayPause.setImageResource(R.drawable.btn_play);
    		state = PlayerState.PAUSED;
        }
    }
	
	private class AudioPlayerBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG,"AudioPlayerBroadCastReceiver.onReceive action=" + intent.getAction());
            if(IntentActions.INTENT_FROM_SERVICE_PLAY_PAUSE.equals( intent.getAction())) {
                updatePlayPauseButtonState();
            }
        }
    }
	

	 private void onClickPlayPause() {
        if (mPlayer.isPlaying() ) {
            updateCurrentTrackTask.pause();
        } else {
            updateCurrentTrackTask.unPause();
        }
        sendIntentToService(new Intent(IntentActions.INTENT_PLAY_PAUSE));
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
	        case R.id.btnPlayPause:
	            onClickPlayPause();
	            break;
	        case R.id.btnRight:
	            nextSong();
	            updatePlayPauseButtonState();
	            break;
	        case R.id.btnLeft:
	            Intent intent = new Intent(this, PlaylistPickerActivity.class);
	            startActivity(intent);
	            break;
        }
	}
	
	private void refreshScreen() {
        if(mAudioPlayerService == null) {
            updateScreenAsync();
        } else {
            updatePlayQueue();
        }
    }
	
	private void nextSong() {
		updateCurrentTrackTask.unPause();
		sendIntentToService(new Intent(IntentActions.INTENT_NEXT_SONG));
	}
	
	private void previousSong() {
		updateCurrentTrackTask.unPause();
		sendIntentToService(new Intent(IntentActions.INTENT_PREVIOUS_SONG));
	}
	
	private void sendIntentToService(final Intent intent) {
		sendBroadcast(intent);
	}
    
    private void updateScreenAsync() {
        waitForAudioPlayertimer.scheduleAtFixedRate( new TimerTask() {
            
            public void run() {
                Log.d(TAG,"updateScreenAsync running timmer");
                if(mAudioPlayerService != null) {
                    waitForAudioPlayertimer.cancel();
                    handler.post( new Runnable() {
                        public void run() {
                            updatePlayQueue();
                        }
                    });
                }
            }
            }, 10, UPDATE_INTERVAL);
    }
	
	public void updatePlayQueue() {
        updatePlayPauseButtonState();
        if(updateCurrentTrackTask == null) {
            updateCurrentTrackTask = new UpdateCurrentTrackTask();
            updateCurrentTrackTask.execute();
        } else {
            Log.e(TAG, "updateCurrentTrackTask is not null" );
        }
    }

}
