package com.danilov.orange.task;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.danilov.orange.OrangeApplication;
import com.danilov.orange.interfaces.Listable;
import com.danilov.orange.util.BitmapCache;
import com.danilov.orange.util.MusicHolder;

public class ImageFetcher extends AsyncTask<Void, Void, Bitmap>{
	
    private final WeakReference<ImageView> imageViewReference;
    private Listable mListable;
	
	
	public ImageFetcher(final MusicHolder holder) {
		imageViewReference = holder.mImage;
	}
	
	public void setListable(final Listable listable) {
		mListable = listable;
	}

	@Override
	protected Bitmap doInBackground(Void... params) {
		Uri uri = mListable.getThumbnailPath();
		ContentResolver res = OrangeApplication.getContext().getContentResolver();
		InputStream in = null;
		try {
			in = res.openInputStream(uri);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Bitmap bm = BitmapFactory.decodeStream(in);
	    BitmapCache.getInstance().addBitmapToCache(mListable, bm);
	    return bm;
	}
	
	@Override
    protected void onPostExecute(Bitmap bitmap) {
		if (isCancelled()) {
            return;
        }
		
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
            	imageView.setImageBitmap(bitmap);
            }
        }
        ImageFetcherExecutor.onFetchFinished();
    }
	
	public Listable getListable() {
		return mListable;
	}

}
