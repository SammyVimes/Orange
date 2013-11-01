package com.danilov.orange.fragments;

import java.util.ArrayList;
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
import com.danilov.orange.model.ArtistProperty;
import com.danilov.orange.model.Song;
import com.danilov.orange.util.IntentActions;
import com.danilov.orange.util.Utilities;

public class ArtistPropertyPickerFragment extends PageFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("APPF", "onCreate");
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
		this.setType(ARTIST_PROPERTY_FRAGMENT_TYPE);
		mCallback.onFragmentCreated(this);
		return view;
	}
	
	public void setArtistProperty (final List<ArtistProperty> artistProperties) {
		for (ArtistProperty artistProperty : artistProperties) {
			mAdapter.add(artistProperty);
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
			intent.setAction(IntentActions.INTENT_SET_PLAYLIST_FROM_ARTIST_PROPERTY);
			ArtistProperty artistProperty = (ArtistProperty)mAdapter.getItem(position);
			List<Album> albums = artistProperty.getAlbums();
			List<Song> songs = new ArrayList<Song>();
			for (Album album : albums) {
				List<Song> albumsSongs = null;
				if (album.getSongs() == null) {
					albumsSongs = album.getSongs();
				} else {
					albumsSongs = Utilities.getSongListForAlbum(getActivity(), album);
				}
				songs.addAll(Utilities.getSongListForAlbum(getActivity(), album));
			}
			artistProperty.setSongs(songs);
			intent.putExtra(IntentActions.INTENT_EXTRA_INTEGER_ARTIST_PROPERTY, position);
			getActivity().sendBroadcast(intent);
			getActivity().finish();
		}
		
	}
}
