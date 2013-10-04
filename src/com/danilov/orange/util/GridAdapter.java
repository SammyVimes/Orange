package com.danilov.orange.util;

import android.content.Context;
import android.graphics.Bitmap;
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

public class GridAdapter extends ArrayAdapter<Listable> {
	
	private static final int VIEW_TYPE_COUNT = 2;
    private final int mLayoutId;
    private DataHolder[] mData;
    private BitmapHandler mHandler;
    private boolean[] hasCallback;
    
	public GridAdapter(final Context context, final int layoutId) {
        super(context, 0);
        // Get the layout Id
        mLayoutId = layoutId;
        mHandler = new BitmapHandler();
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
		holder.mImage.get().setImageBitmap(dataHolder.mImage);
        Listable listable = getItem(position); 
        if (!hasCallback[position]) {
    		hasCallback[position] = true;
            Bitmap bitmap = ImageFetcher.getInstance().getBitmap(listable, new ImageFetcherCallback(listable, dataHolder, holder));
            if (bitmap != null) {
            	dataHolder.mImage = bitmap;
            	holder.mImage.get().setImageBitmap(bitmap);
            }
    	}
        return convertView;
    }
	
	public void buildCache() {
		hasCallback = new boolean[getCount()];
		for (int i = 0; i < hasCallback.length; i++) {
			hasCallback[i] = false;
		}
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
	
	public static class BitmapHandler extends Handler {
		
		
		public BitmapHandler() {
		}
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			ImageFetcherCallback callback = (ImageFetcherCallback) msg.obj;
			callback.setImage();
		}
		
	}
	
	public class ImageFetcherCallback implements IImageFetcherCallback {

		private Listable mListable;
		private DataHolder mDataHolder;
		private MusicHolder mMusicHolder;
		private Bitmap mBitmap;
		
		public ImageFetcherCallback(final Listable listable,
									final DataHolder dataHolder,
									final MusicHolder musicHolder) {
			mDataHolder = dataHolder;
			mMusicHolder = musicHolder;
			mListable = listable;
		}

		@Override
		public void onImageFetched(final Bitmap bitmap) {
			mBitmap = bitmap;
			mHandler.sendMessage(mHandler.obtainMessage(0, this));
		}
		
		public void setImage() {
			mDataHolder.mImage = mBitmap;
			ImageView iv = mMusicHolder.mImage.get(); 
			if (iv != null) {
				iv.setImageBitmap(mBitmap);
			}
		}

		public Listable getListable() {
			return mListable;
		}
		
	}
	
}
