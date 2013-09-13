package com.danilov.orange.util;

import com.danilov.orange.interfaces.IImageFetcherCallback;
import com.danilov.orange.interfaces.Listable;
import com.danilov.orange.model.Album;
import com.danilov.orange.task.ImageFetcher;
import com.danilov.orange.util.MusicHolder.DataHolder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class GridAdapter extends ArrayAdapter<Listable> {
	
	private static final int VIEW_TYPE_COUNT = 2;
    private final int mLayoutId;
    private DataHolder[] mData;
    private BitmapHandler mHandler;
    
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
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mLayoutId, parent, false);
            holder = new MusicHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MusicHolder)convertView.getTag();
        }

        // Retrieve the data holder
        final DataHolder dataHolder = mData[position];

        // Set each album name (line one)
        holder.mLineOne.get().setText(dataHolder.mLineOne);
        // Set the artist name (line two)
        holder.mLineTwo.get().setText(dataHolder.mLineTwo);
        Listable l = getItem(position); 
        if (l.getClass().getCanonicalName().contains("Album")) {
        	Album album = (Album) getItem(position);
            Bitmap bitmap = ImageFetcher.getInstance().getBitmap(album, new ImageFetcherCallback(album, dataHolder, holder));
            if (bitmap != null) {
            	holder.mImage.get().setImageBitmap(bitmap);
            }
        }
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

		private Album mAlbum;
		private DataHolder mDataHolder;
		private MusicHolder mMusicHolder;
		private Bitmap mBitmap;
		
		public ImageFetcherCallback(final Album album,
									final DataHolder dataHolder,
									final MusicHolder musicHolder) {
			mDataHolder = dataHolder;
			mMusicHolder = musicHolder;
			mAlbum = album;
		}

		@Override
		public void onImageFetched(final Bitmap bitmap) {
			mBitmap = bitmap;
			mHandler.sendMessage(mHandler.obtainMessage(0, this));
		}
		
		public void setImage() {
			mDataHolder.mImage = mBitmap;
			mMusicHolder.mImage.get().setImageBitmap(mBitmap);
		}

		public Album getAlbum() {
			return mAlbum;
		}
		
	}
	
}
