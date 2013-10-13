package com.danilov.orange.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.danilov.orange.interfaces.IImageFetcherCallback;
import com.danilov.orange.interfaces.Listable;
import com.danilov.orange.model.Album;
import com.danilov.orange.task.ImageFetcher;
import com.danilov.orange.util.MusicHolder.DataHolder;
import com.danilov.orange.views.AsyncDrawable;

public class GridAdapter extends ArrayAdapter<Listable> {
	
	private static final int VIEW_TYPE_COUNT = 2;
    private final int mLayoutId;
    private DataHolder[] mData;
    private Context mContext;
    
	public GridAdapter(final Context context, final int layoutId) {
        super(context, 0);
        mContext = context;
        mLayoutId = layoutId;
    }
	
	@Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // Recycle ViewHolder's items
        MusicHolder holder;
        // Retrieve the data holder
        final DataHolder dataHolder = mData[position];
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mLayoutId, parent, false);
            holder = new MusicHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MusicHolder)convertView.getTag();
        }
        // Set each album name (line one)
        holder.mLineOne.get().setText(dataHolder.mLineOne);
        // Set the artist name (line two)
        holder.mLineTwo.get().setText(dataHolder.mLineTwo);
        Listable listable = getItem(position); 
        /*Doing this to avoid setting image for reused image view after task complete
         * for example: we started task, but scrolled from that ImageView and this ImageView
         * being reused and filled from cache. When task ends it would be filled with the wrong image
         */
        holder.mImage.get().setTag(listable);
        loadBitmap(listable, holder.mImage.get());
        return convertView;
    }
	
	public void buildCache() {
        mData = new DataHolder[getCount()];
        for (int i = 0; i < getCount(); i++) {
            // Build the album
            final Listable listable = getItem(i);
            // Build the data holder
            mData[i] = new DataHolder();
            // Album names (line one)
            mData[i].mLineOne = listable.getFirstLine();
            // Album artist names (line two)
            mData[i].mLineTwo = listable.getSecondLine();
        }
    }
	
	public void loadBitmap(final Listable listable, final ImageView imageView) {
		final Bitmap bitmap = BitmapCache.getInstance().getBitmap(listable);
		if (cancelPotentialWork(listable, imageView)) {
			if (bitmap != null) {
				imageView.setImageBitmap(bitmap);
			} else { 
	            final ImageFetcher imageFetcher = new ImageFetcher(imageView);
	            final AsyncDrawable asyncDrawable =
	                    new AsyncDrawable(mContext.getResources(), null, imageFetcher);
	            imageView.setImageDrawable(asyncDrawable);
	            imageFetcher.execute(listable);
			}
        }
    }
	
	public static boolean cancelPotentialWork(final Listable lstble, final ImageView imageView) {
		
        final ImageFetcher imageFetcher = getImageFetcher(imageView);

        if (imageFetcher != null) {
            final Listable listable = imageFetcher.getListable();
            if (listable != lstble) {
                // Cancel previous task
                imageFetcher.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }
	
	private static ImageFetcher getImageFetcher(ImageView imageView) {
       if (imageView != null) {
           final Drawable drawable = imageView.getDrawable();
           if (drawable instanceof AsyncDrawable) {
               final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
               return asyncDrawable.getBitmapWorkerTask();
           }
        }
        return null;
    }
}
