package com.danilov.orange.task;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.danilov.orange.OrangeApplication;
import com.danilov.orange.interfaces.ITaskCallback;
import com.danilov.orange.model.Album;
import com.danilov.orange.model.ArtistProperty;
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
		List<Album> albums = getAllAlbums();
		List<ArtistProperty> artistProperty = MusicSort.sortByAuthors(albums);
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
	private List<Album> getAllAlbums() {
		String[] projection = new String[] { MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ARTIST,
				MediaStore.Audio.Albums.ALBUM};
		Cursor cursor = mActivity.managedQuery(
		        MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
		        projection,
		        null,
		        null,
		        null);

		List<Album> albums = new ArrayList<Album>();
		while(cursor.moveToNext()){
			String id = cursor.getString(0);
			String artist = cursor.getString(1);
			String albumName = cursor.getString(2);
			Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
			Uri uri = ContentUris.withAppendedId(sArtworkUri, Long.valueOf(id));
			String thumbnailPath =  uri.getPath();
			Album album = new Album(id, albumName, artist, uri);
			albums.add(album);
		}
		return albums;
	}
	
	private class NullCallback implements ITaskCallback {

		@Override
		public void onTaskComplete(final Object object) {
		}
	}
	

}
