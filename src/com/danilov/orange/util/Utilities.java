package com.danilov.orange.util;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.danilov.orange.model.Album;
import com.danilov.orange.model.Song;

public class Utilities {
	
	 /**
     * Function to convert milliseconds time to
     * Timer Format
     * Hours:Minutes:Seconds
     * */
    public static String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";
 
        // Convert total duration into time
           int hours = (int)( milliseconds / (1000*60*60));
           int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
           int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
           // Add hours if there
           if(hours > 0){
               finalTimerString = hours + ":";
           }
 
           // Prepending 0 to seconds if it is one digit
           if(seconds < 10){
               secondsString = "0" + seconds;
           }else{
               secondsString = "" + seconds;}
 
           finalTimerString = finalTimerString + minutes + ":" + secondsString;
 
        // return timer string
        return finalTimerString;
    }
 
    /**
     * Function to get Progress percentage
     * @param currentDuration
     * @param totalDuration
     * */
    public static int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage = (double) 0;
 
        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);
 
        // calculating percentage
        percentage =(((double)currentSeconds)/totalSeconds)*100;
 
        // return percentage
        return percentage.intValue();
    }
 
    /**
     * Function to change progress to timer
     * @param progress -
     * @param totalDuration
     * returns current duration in milliseconds
     * */
    public static int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double)progress) / 100) * totalDuration);
 
        // return current duration in milliseconds
        return currentDuration * 1000;
    }
    
    public static void toaster(final Context context, final String message) {
    	Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
    
    public static void sleep(int timeInMillis) {
        try {
            Thread.sleep(timeInMillis);
        } catch (InterruptedException e) { }
    }
    
    private final static List<Song> sEmptyList = new LinkedList<Song>();
    
    public static List<Song> getSongListForAlbum(Context context, Album album) {
        final String[] ccols = new String[] { 
        		MediaStore.Audio.Media._ID,
        		MediaStore.Audio.Media.TITLE,
        		MediaStore.Audio.Media.DATA,
        		MediaStore.Audio.Media.DURATION};
        long id = Long.valueOf(album.getAlbumID());
        String where = MediaStore.Audio.Media.ALBUM_ID + "=" + id + " AND " + 
                MediaStore.Audio.Media.IS_MUSIC + "=1";
        Cursor cursor = query(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                ccols, where, null, MediaStore.Audio.Media.TRACK);

        if (cursor != null) {
        	List<Song> list = getSongListForCursor(cursor, album);
            cursor.close();
            return list;
        }
        return sEmptyList;
    }
    
    public static List<Song> getSongListForCursor(Cursor cursor, Album album) {
        if (cursor == null) {
            return sEmptyList;
        }
        int len = cursor.getCount();
        List<Song> list = new LinkedList<Song>();
        cursor.moveToFirst();
        while(cursor.moveToNext()){
			String id = cursor.getString(0);
			String albumName = album.getAlbumName();
			String artist = album.getArtistName();
			String title = cursor.getString(1);
			String path = cursor.getString(2);
			Song song = new Song(id, artist, title, path);
			song.setAlbum(albumName);
			list.add(song);
		}
        return list;
    }
    
    public static Cursor query(Context context, Uri uri, String[] projection,
            String selection, String[] selectionArgs, String sortOrder, int limit) {
        try {
            ContentResolver resolver = context.getContentResolver();
            if (resolver == null) {
                return null;
            }
            if (limit > 0) {
                uri = uri.buildUpon().appendQueryParameter("limit", "" + limit).build();
            }
            return resolver.query(uri, projection, selection, selectionArgs, sortOrder);
         } catch (UnsupportedOperationException ex) {
            return null;
         }
    }
    
    public static Cursor query(Context context, Uri uri, String[] projection,
            String selection, String[] selectionArgs, String sortOrder) {
        return query(context, uri, projection, selection, selectionArgs, sortOrder, 0);
    }
    
}
