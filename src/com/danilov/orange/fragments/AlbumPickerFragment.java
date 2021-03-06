package com.danilov.orange.fragments;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.danilov.orange.PlaylistPickerActivity;
import com.danilov.orange.model.Album;
import com.danilov.orange.model.Song;
import com.danilov.orange.util.IntentActions;
import com.danilov.orange.util.Utilities;

public class AlbumPickerFragment extends PageFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("AF", "onCreate");
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
       Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		if (mCallback instanceof NullCallback) {
			PlaylistPickerActivity activity = (PlaylistPickerActivity) getActivity(); 
			mCallback = activity.getCallback();
		}
		mGridView.setOnItemClickListener(new MyItemClickListener());
		this.setType(ALBUM_FRAGMENT_TYPE);
		mCallback.onFragmentCreated(this);
		return view;
	}
	
	public void setAlbums(final List<Album> albums) {
		for (Album album : albums) {
			mAdapter.add(album);
		}
		mAdapter.notifyDataSetChanged();
		mAdapter.buildCache();
		mProgressBar.setVisibility(View.GONE);
	}
	
	public class MyItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent();
			intent.setAction(IntentActions.INTENT_SET_PLAYLIST_FROM_ALBUM);
			Album album = (Album)mAdapter.getItem(position);
			List<Song> songs = Utilities.getSongListForAlbum(getActivity(), album);
			album.setSongs(songs);
			intent.putExtra(IntentActions.INTENT_EXTRA_INTEGER_ALBUM, position);
			getActivity().sendBroadcast(intent);
			getActivity().finish();
		}
		
	}
}
