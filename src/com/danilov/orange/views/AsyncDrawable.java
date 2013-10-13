package com.danilov.orange.views;

import java.lang.ref.WeakReference;

import com.danilov.orange.task.ImageFetcher;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class AsyncDrawable extends BitmapDrawable {
	
	private final WeakReference<ImageFetcher> imageFetcherReference;

    public AsyncDrawable(Resources res, Bitmap bitmap,
            ImageFetcher imageFetcher) {
        super(res, bitmap);
        imageFetcherReference = new WeakReference<ImageFetcher>(imageFetcher);
    }

    public ImageFetcher getBitmapWorkerTask() {
        return imageFetcherReference.get();
    }
}
