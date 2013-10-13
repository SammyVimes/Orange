package com.danilov.orange.task;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.danilov.orange.interfaces.Listable;
import com.danilov.orange.model.Song;
import com.danilov.orange.util.BitmapCache;

public class ImageFetcher extends AsyncTask<Listable, Void, Bitmap>{
	
    private final WeakReference<ImageView> imageViewReference;
    private Listable mListable;
	
	
	public ImageFetcher(final ImageView imageView) {
		imageViewReference = new WeakReference<ImageView>(imageView);
	}

	@Override
	protected Bitmap doInBackground(Listable... params) {
		mListable = params[0];
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
		Song s = mListable.getSongs().get(0);
	    mmr.setDataSource(s.getPath().toString());
	    byte[] artBytes =  mmr.getEmbeddedPicture();
	    Bitmap bm = null;
	    if(artBytes != null) {
	        InputStream is = new ByteArrayInputStream(mmr.getEmbeddedPicture());
	        bm = BitmapFactory.decodeStream(is);
	    }
	    BitmapCache.getInstance().addBitmapToCache(mListable, bm);
	    return bm;
	}
	
	@Override
    protected void onPostExecute(Bitmap bitmap) {
		if (isCancelled()) {
            bitmap = null;
        }
		
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            /*Check if we got reused ImageView, that is now used for 
             * another purpose
             * */
            if (imageView != null) {
                Listable listable = (Listable) imageView.getTag();
                if (listable == mListable) {
                	imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
	
	public Listable getListable() {
		return mListable;
	}

}
