package com.danilov.orange.util;

import java.util.List;
import java.util.Random;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.danilov.orange.R;
import com.danilov.orange.model.Album;
import com.danilov.orange.test.MockService;

public class PageFragment extends SherlockFragment {
  
  static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
  
  int pageNumber;
  int backColor;
  GridAdapter aa;
  private GridView mGridView;
  
  public static PageFragment newInstance(int page) {
    PageFragment pageFragment = new PageFragment();
    Bundle arguments = new Bundle();
    arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
    pageFragment.setArguments(arguments);
    return pageFragment;
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
    Log.d("PF", "onCreate");
    Random rnd = new Random();
    int layout = R.layout.grid_item;
    aa = new GridAdapter(getSherlockActivity(), layout);
    List<Album> albums = MockService.getAlbumList(5);
    for (Album album : albums) {
    	aa.add(album);
    }
    aa.buildCache();
    backColor = Color.argb(40, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment, null);
    Log.d("PF", "onCreateView");
    mGridView = (GridView)view.findViewById(R.id.grid_base);
    mGridView.setNumColumns(2);
    mGridView.setAdapter(aa);
    return view;
  }
}
