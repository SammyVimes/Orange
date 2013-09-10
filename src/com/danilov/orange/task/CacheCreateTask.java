package com.danilov.orange.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.danilov.orange.OrangeApplication;
import com.danilov.orange.interfaces.ITaskCallback;
import com.danilov.orange.interfaces.Listable;
import com.danilov.orange.model.Album;
import com.danilov.orange.model.ArtistProperty;
import com.danilov.orange.model.Song;
import com.danilov.orange.util.MusicSort;

public class CacheCreateTask extends AsyncTask<Void, Void, Object>{
	
	public static final String ALBUMS = "ALBUMS";
	public static final String ARTIST_PROPERTY = "ARTIST_PROPERTY";
	
	private ITaskCallback mCallback;
	private Activity mActivity;
	
	public CacheCreateTask(final Activity activity, final ITaskCallback callback) {
		this.mCallback = callback;
		if (mCallback == null) {
			mCallback = new NullCallback();
		}
		this.mActivity = activity;
	}
	
	@Override
	protected Object doInBackground(Void... arg0) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Song> allSongs = getAllSongs();
		List<Album> albums = MusicSort.sortByAlbums(allSongs);
		List<ArtistProperty> artistProperty = MusicSort.sortByAuthors(allSongs);
		OrangeApplication.getInstance().setAlbums(albums);
		OrangeApplication.getInstance().setArtistProperty(artistProperty);
		map.put(ALBUMS, albums);
		map.put(ARTIST_PROPERTY, artistProperty);
		return map;
	}
	
	@Override
	protected void onPreExecute() {
		
    }
	
	@Override
	protected void onPostExecute(Object result) {
		mCallback.onTaskComplete(result);
    }
	
	
	@SuppressWarnings("deprecation")
	private List<Song> getAllSongs() {
		String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

		String[] projection = {
		        MediaStore.Audio.Media._ID,
		        MediaStore.Audio.Media.ALBUM,
		        MediaStore.Audio.Media.ARTIST,
		        MediaStore.Audio.Media.TITLE,
		        MediaStore.Audio.Media.DATA,
		        MediaStore.Audio.Media.DURATION
		};

		Cursor cursor = mActivity.managedQuery(
		        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
		        projection,
		        selection,
		        null,
		        null);

		List<Song> songs = new ArrayList<Song>();
		while(cursor.moveToNext()){
			String id = cursor.getString(0);
			String album = cursor.getString(1);
			String artist = cursor.getString(2);
			String title = cursor.getString(3);
			String path = cursor.getString(4);
			Song song = new Song(id, artist, title, path);
			song.setAlbum(album);
			songs.add(song);
		}
		return songs;
	}
	
	private class NullCallback implements ITaskCallback {

		@Override
		public void onTaskComplete(final Object object) {
		}
	}
	

}
