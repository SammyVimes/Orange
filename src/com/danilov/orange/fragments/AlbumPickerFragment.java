package com.danilov.orange.fragments;

import java.util.List;

import com.danilov.orange.PlaylistPickerActivity;
import com.danilov.orange.interfaces.IFragmentCreateCallback;
import com.danilov.orange.model.Album;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
			mCallback = activity.new FragmentCreatedCallback();
		}
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
}
