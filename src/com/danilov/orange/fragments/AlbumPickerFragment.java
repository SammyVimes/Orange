package com.danilov.orange.fragments;

import java.util.List;

import com.danilov.orange.model.Album;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class AlbumPickerFragment extends PageFragment{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("AF", "onCreate");
		super.onCreate(savedInstanceState);
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
