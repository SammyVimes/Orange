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
    
	public GridAdapter(final Context context, final int layoutId) {
        super(context, 0);
        // Get the layout Id
        mLayoutId = layoutId;
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
        String clazz = l.getClass().getCanonicalName();
        if (l.getClass().getCanonicalName().contains("Album")) {
        	Album a = (Album) getItem(position);
            BitmapHandler h = new BitmapHandler(dataHolder, holder);
            Bitmap bitmap = ImageFetcher.getInstance().getBitmap(a, new ImageFetcherCallback(a, h));
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
	
	public class BitmapHandler extends Handler {
		
		private DataHolder mDataHolder;
		private MusicHolder mMusicHolder;
		
		public BitmapHandler(final DataHolder dataHolder,
				final MusicHolder musicHolder) {
			mDataHolder = dataHolder;
			mMusicHolder = musicHolder;
		}
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bitmap bitmap = (Bitmap) msg.obj;
			mDataHolder.mImage = bitmap;
			mMusicHolder.mImage.get().setImageBitmap(bitmap);
		}
		
	}
	
	public class ImageFetcherCallback implements IImageFetcherCallback {

		private BitmapHandler mHandler;
		private Album mAlbum;
		
		public ImageFetcherCallback(final Album album, final BitmapHandler handler) {
			mAlbum = album;
			mHandler = handler;
		}

		@Override
		public void onImageFetched(final Bitmap bitmap) {
			mHandler.sendMessage(mHandler.obtainMessage(0, bitmap));
		}

		public Album getAlbum() {
			return mAlbum;
		}
		
	}
	
}
