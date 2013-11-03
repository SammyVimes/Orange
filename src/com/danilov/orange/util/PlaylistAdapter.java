package com.danilov.orange.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.danilov.orange.R;
import com.danilov.orange.interfaces.Listable;
import com.danilov.orange.model.PlayList;
import com.danilov.orange.model.Song;
import com.danilov.orange.util.MusicHolder.DataHolder;

public class PlaylistAdapter extends ArrayAdapter<Song>{


    private final int mLayoutId;
    private Context mContext;
    private PlayList mPlayList;
    private int currentSongPosition = 0;
	
	public PlaylistAdapter(Context context, int layoutId, PlayList playList) {
		super(context, 0);
        mContext = context;
        mLayoutId = layoutId;
        mPlayList = playList; 
	}
	
	private class ViewHolder {
		public ViewHolder(View convertView) {
			songName = (TextView) convertView.findViewById(R.id.songName);
			albumName = (TextView) convertView.findViewById(R.id.albumName);
		}
		TextView songName;
		TextView albumName;
	}
	
	@Override
    public int getCount() {
        return mPlayList.size();
    }

    @Override
    public Song getItem(int position) {
        return mPlayList.get(position);
    }
    
    public int getCurrentSongPosition() {
    	return currentSongPosition;
    }
	
	@Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Song song = mPlayList.get(position);
        Song curSong = mPlayList.getCurrentSong();
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mLayoutId, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // Set song name (line one)
        if (song == curSong) {
        	int color = mContext.getResources().getColor(R.color.orange);
        	currentSongPosition = position;
        	convertView.setBackgroundColor(color);
        } else {
        	convertView.setBackgroundDrawable(null);
        }
        holder.songName.setText(song.getTitle());
        // Set album name (line two)
        holder.albumName.setText(song.getAlbum());
        return convertView;
    }
	
	

}
