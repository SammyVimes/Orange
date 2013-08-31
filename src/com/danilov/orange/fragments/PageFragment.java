package com.danilov.orange.fragments;

import java.util.List;
import java.util.Random;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.danilov.orange.R;
import com.danilov.orange.model.Album;
import com.danilov.orange.test.MockService;
import com.danilov.orange.util.GridAdapter;

public class PageFragment extends SherlockFragment {
	
  public static final int ALBUM_FRAGMENT_TYPE = 0;
  public static final int ARTIST_FRAGMENT_TYPE = 1;
  public static final int ALL_SONGS_FRAGMENT_TYPE = 2;
  public int mType;
  
  protected GridAdapter mAdapter;
  protected GridView mGridView;
  protected ProgressBar mProgressBar;
  
  public static PageFragment newInstance(final int type) {
	PageFragment fragment = null;
	switch (type) {
	case ALBUM_FRAGMENT_TYPE:
		fragment = new AlbumPickerFragment();
		break;
	case ARTIST_FRAGMENT_TYPE:
			
		break;
	case ALL_SONGS_FRAGMENT_TYPE:
		
		break;
	default:
		break;
	}
    return fragment;
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d("PF", "onCreate");
    int layout = R.layout.grid_item;
    mAdapter = new GridAdapter(getSherlockActivity(), layout);
  }
  
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
       Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.fragment, null);
	    Log.d("PF", "onCreateView");
	    mGridView = (GridView)view.findViewById(R.id.grid_base);
	    mGridView.setNumColumns(2);
	    mGridView.setAdapter(mAdapter);
	    mProgressBar = (ProgressBar)view.findViewById(R.id.progressBar);
	    return view;
    }
  
	public void setType(final int type) {
		mType = type;
	}
	
	public int getType() {
		return mType;
	}
  
}
