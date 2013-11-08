package com.danilov.orange;

import java.lang.reflect.Field;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.NotificationCompat;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.danilov.orange.model.Song;
import com.danilov.orange.util.IntentActions;

public class NotificationHelper {
	
	private static final int MUSIC_SERVICE = 1;
	
	private AudioPlayerService mService;
	
    private Notification mNotification = null;
	
    private RemoteViews mNotificationView;
    
    private NotificationManager mNotificationManager;
    
    public NotificationHelper(final AudioPlayerService service) {
        mService = service;
        mNotificationManager = (NotificationManager)service
                .getSystemService(Context.NOTIFICATION_SERVICE);
    }
    
    public void buildNotification(final Song song) {
    	if (song == null) {
    		return;
    	}
    	mNotificationView = new RemoteViews(mService.getPackageName(),
                R.layout.notification);
    	initLayout(song);
    	Bitmap bmp = ((BitmapDrawable)mService.getResources().getDrawable(R.drawable.ic_launcher)).getBitmap();
    	mNotification = new NotificationCompat.Builder(mService)
				        .setSmallIcon(R.drawable.ic_launcher)
				        .setLargeIcon(bmp)
				        .setContentIntent(getPendingIntent())
				        .setContent(mNotificationView)
				        .getNotification();
    	mNotification.contentView = mNotificationView;
    	boolean hasLargeIcon = true; 
		try {
			Class clazz = Class.forName("android.app.Notification;");
	    	Field field = clazz.getField("largeIcon");
	    	if (field == null) {
	    		hasLargeIcon = false;
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	if (!hasLargeIcon) {
    		mNotificationView.setImageViewResource(R.id.bigIcon, R.drawable.ic_launcher);
    	}
    	mNotification.flags = mNotification.flags | Notification.FLAG_ONGOING_EVENT;
    	if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
    		initPlaybackActions();
    	}
//    	initPlaybackActions();
    	mService.startForeground(MUSIC_SERVICE, mNotification);
    }
    
    private PendingIntent getPendingIntent() {
        return PendingIntent.getActivity(mService, 0, new Intent().setClass(mService, PlayerActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0);
    }
    
    public void updateNotification(final boolean isPlaying) {
    	if (mNotification != null && mNotificationManager != null) {
    		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
    	    	mNotificationView.setImageViewResource(R.id.notification_play,
    	                isPlaying ? R.drawable.btn_playback_pause : R.drawable.btn_playback_play);
    	    	mNotificationManager.notify(MUSIC_SERVICE, mNotification);
        	} else {
        		if (isPlaying) {
        	    	mNotificationManager.notify(MUSIC_SERVICE, mNotification);
        		} else {
        			removeNotification();
        		}
        	}
    	}
    }
    
    private void initPlaybackActions() {
        // Play and pause
    	mNotificationView.setOnClickPendingIntent(R.id.notification_play,
                retreivePlaybackActions(1));

        // Skip tracks
    	mNotificationView.setOnClickPendingIntent(R.id.notification_next,
                retreivePlaybackActions(2));

        // Previous tracks
    	mNotificationView.setOnClickPendingIntent(R.id.notification_previous,
                retreivePlaybackActions(3));

        // Stop and collapse the notification
    	mNotificationView.setOnClickPendingIntent(R.id.notification_collapse,
                retreivePlaybackActions(4));

        // Update the play button image
    	mNotificationView.setImageViewResource(R.id.notification_play,
                R.drawable.btn_playback_pause);
    }
    
    private void initLayout(final Song song) {
    	if (song != null) {
        	String songName = song.getTitle();
        	String artistName = song.getArtist();
            // Track name (line one)
	        mNotificationView.setTextViewText(R.id.notification_line_one, songName);
	        // Artist name (line two)
	        mNotificationView.setTextViewText(R.id.notification_line_two, artistName);
    	}
    }
   
    //uses for killing notification in android.sdk < 11, cause it aint have no 
    //close button
    public void removeNotification() {
    	mService.stopForeground(true);
    }
    
    private final PendingIntent retreivePlaybackActions(final int which) {
        Intent action;
        PendingIntent pendingIntent;
        final ComponentName serviceName = new ComponentName(mService, AudioPlayerService.class);
        switch (which) {
            case 1:
                // Play and pause
                action = new Intent(IntentActions.INTENT_PLAY_PAUSE);
                pendingIntent = PendingIntent.getBroadcast(mService, 1, action, 0);
                return pendingIntent;
            case 2:
                // Skip tracks
                action = new Intent(IntentActions.INTENT_NEXT_SONG);
                pendingIntent = PendingIntent.getBroadcast(mService, 2, action, 0);
                return pendingIntent;
            case 3:
                // Previous tracks
                action = new Intent(IntentActions.INTENT_PREVIOUS_SONG);
                pendingIntent = PendingIntent.getBroadcast(mService, 3, action, 0);
                return pendingIntent;
            case 4:
                // Stop and collapse the notification
                action = new Intent(IntentActions.INTENT_STOP);
                pendingIntent = PendingIntent.getBroadcast(mService, 4, action, 0);
                return pendingIntent;
            default:
                break;
        }
        return null;
    }
    
}
