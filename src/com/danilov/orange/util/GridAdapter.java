package com.danilov.orange.util;

import com.danilov.orange.model.Album;
import com.danilov.orange.util.MusicHolder.DataHolder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class GridAdapter extends ArrayAdapter<Album> {
	
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
        return convertView;
    }
	
	public void buildCache() {
        mData = new DataHolder[getCount()];
        for (int i = 0; i < getCount(); i++) {
            // Build the album
            final Album album = getItem(i);

            // Build the data holder
            mData[i] = new DataHolder();
            // Album Id
            mData[i].mItemId = album.getAlbumID();
            // Album names (line one)
            mData[i].mLineOne = album.getArtistName();
            // Album artist names (line two)
            mData[i].mLineTwo = album.getAlbumName();
        }
    }
	
}
