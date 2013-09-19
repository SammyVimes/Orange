package com.danilov.orange.task;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.util.Log;

import com.danilov.orange.interfaces.IImageFetcherCallback;
import com.danilov.orange.model.Album;
import com.danilov.orange.model.Song;
import com.danilov.orange.util.GridAdapter.ImageFetcherCallback;

public class ImageFetcher {
	
	private volatile List<IImageFetcherCallback> mCallbacksList = new LinkedList<IImageFetcherCallback>();
	private Map<Album, Bitmap> mBitmapMap = new HashMap<Album, Bitmap>();
	private static List<Album> mAlbums = null;
	private static ImageFetcher mInstance;
	
	public static ImageFetcher getInstance() {
		if (mAlbums == null) {
			Log.d("ImageFetcher", "Tried to get instance with mAlbums not set");
			return null;
		}
		if (mInstance == null) {
			mInstance = new ImageFetcher();
		}
		return mInstance;
	}
	
	public static void deleteInstance() {
		mInstance = null;
	}
	
	public void freeBitmaps() {
		mBitmapMap = null;
		mAlbums = null;
	}
	
	/*CALL IT FIRST*/
	public static void setAlbums(final List<Album> albums) {
		mAlbums = albums;
	}
	
	private ImageFetcher() {
	}
	
	public void fetch() {
		ImageFetcherTask task = new ImageFetcherTask();
		task.execute();
	}
	
	public Bitmap getBitmap(final Album album, final IImageFetcherCallback callback) {
		Bitmap bitmap = null;
		synchronized (mCallbacksList) {
			bitmap = mBitmapMap.get(album);
			if (bitmap == null) {
				addCallback(callback);
			}
		}
		return bitmap;
	}
	
	public void addCallback(final IImageFetcherCallback callback) {
		mCallbacksList.add(callback);
	}
	
	private class ImageFetcherTask extends AsyncTask<Void, Void, Void> {

		@SuppressLint("NewApi")
		@Override
		protected Void doInBackground(Void... params) {
			for (Album album : mAlbums) {
				MediaMetadataRetriever mmr = new MediaMetadataRetriever();
				Song s = album.getSongs().get(0);
			    mmr.setDataSource(s.getPath().toString());
			    byte[] artBytes =  mmr.getEmbeddedPicture();
			    if(artBytes != null) {
			        InputStream is = new ByteArrayInputStream(mmr.getEmbeddedPicture());
			        Bitmap bm = BitmapFactory.decodeStream(is);
			        mBitmapMap.put(album, bm);
			    }
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void ob) {
			synchronized (mCallbacksList) {
				for (IImageFetcherCallback callback : mCallbacksList) {
					ImageFetcherCallback cb = (ImageFetcherCallback) callback;
					callback.onImageFetched(mBitmapMap.get(cb.getAlbum()));
				}
				mCallbacksList.clear();
			}
	    }
	}

}
