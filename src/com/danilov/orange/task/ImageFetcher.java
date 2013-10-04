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
import com.danilov.orange.interfaces.Listable;
import com.danilov.orange.model.Album;
import com.danilov.orange.model.Song;
import com.danilov.orange.util.GridAdapter.ImageFetcherCallback;

public class ImageFetcher {
	
	private volatile List<IImageFetcherCallback> mCallbacksList = new LinkedList<IImageFetcherCallback>();
	private Map<Listable, Bitmap> mBitmapMap = new HashMap<Listable, Bitmap>();
	private static List<Listable> mProperty = null;
	private static ImageFetcher mInstance;
	
	public static ImageFetcher getInstance() {
		if (mProperty == null) {
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
		mProperty = null;
	}
	
	/*CALL IT FIRST*/
	public static void setProperty(final List<Listable> listables) {
		mProperty = listables;
	}
	
	private ImageFetcher() {
	}
	
	public void fetch() {
		ImageFetcherTask task = new ImageFetcherTask();
		task.execute();
	}
	
	public Bitmap getBitmap(final Listable listable, final IImageFetcherCallback callback) {
		Bitmap bitmap = null;
		synchronized (mCallbacksList) {
			bitmap = mBitmapMap.get(listable);
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
			for (Listable listable : mProperty) {
				MediaMetadataRetriever mmr = new MediaMetadataRetriever();
				Song s = listable.getSongs().get(0);
			    mmr.setDataSource(s.getPath().toString());
			    byte[] artBytes =  mmr.getEmbeddedPicture();
			    if(artBytes != null) {
			        InputStream is = new ByteArrayInputStream(mmr.getEmbeddedPicture());
			        Bitmap bm = BitmapFactory.decodeStream(is);
			        mBitmapMap.put(listable, bm);
			    }
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void ob) {
			synchronized (mCallbacksList) {
				for (IImageFetcherCallback callback : mCallbacksList) {
					ImageFetcherCallback cb = (ImageFetcherCallback) callback;
					callback.onImageFetched(mBitmapMap.get(cb.getListable()));
				}
				mProperty = null;
				mCallbacksList.clear();
			}
	    }
	}

}
