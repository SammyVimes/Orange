package com.danilov.orange.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.danilov.orange.interfaces.Listable;

public class BitmapCache {
	
	private static BitmapCache mInstance;
	LruCache<Listable, Bitmap> mCache;
	
	public static BitmapCache getInstance() {
		if (mInstance == null) {
			mInstance = new BitmapCache();
		}
		return mInstance;
	}
	
	private BitmapCache() {
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 5;
		mCache = new LruCache<Listable, Bitmap>(cacheSize) {
	        @Override
	        protected int sizeOf(Listable key, Bitmap bitmap) {
	            // The cache size will be measured in kilobytes rather than
	            // number of items.
	            return BitmapUtils.getSizeInKilobytes(bitmap);
	        }
	    };
	}
	
	
	
	public void addBitmapToCache(Listable key, Bitmap bitmap) {
	    if (getBitmap(key) == null && bitmap != null) {
	    	mCache.put(key, bitmap);
	    }
	}
	
	public Bitmap getBitmap(final Listable key) {
		return mCache.get(key);
	}

}
