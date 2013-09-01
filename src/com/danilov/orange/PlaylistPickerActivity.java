package com.danilov.orange;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.danilov.orange.fragments.AlbumPickerFragment;
import com.danilov.orange.fragments.PageFragment;
import com.danilov.orange.interfaces.ITaskCallback;
import com.danilov.orange.interfaces.IFragmentCreateCallback;
import com.danilov.orange.model.Album;
import com.danilov.orange.task.CacheCreateTask;
import com.danilov.orange.util.BasePlayerActivity;
import com.danilov.orange.util.PagerAdapter;

public class PlaylistPickerActivity extends BasePlayerActivity {
	
	private ViewPager mPager;
	private PagerAdapter mAdapter;
	private List<Fragment> mFragments;
	private boolean restoring = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playlist_picker);
		mPager = (ViewPager) findViewById(R.id.pager);
		mFragments = new ArrayList<Fragment>();
		FragmentCreatedCallback callback = new FragmentCreatedCallback();
		if (savedInstanceState == null) {
			mFragments.add(PageFragment.newInstance(PageFragment.ALBUM_FRAGMENT_TYPE, callback));
			mAdapter = new PagerAdapter(getSupportFragmentManager(), mFragments);
			mPager.setAdapter(mAdapter);
		} else {
			restoring = true;
		}
		initActionBarTabs();
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	
	private void initActionBarTabs() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		MyTabListener tabListener = new MyTabListener();
	    for (int i = 0; i < 1; i++) {
	        actionBar.addTab(actionBar.newTab()
	                        .setText("Tab " + (i + 1))
	                        .setTabListener(tabListener));
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private class CacheTaskCallback implements ITaskCallback {

		@Override
		public void onTaskComplete(final Object object) {
			AlbumPickerFragment albumPickerFragment = getAlbumPickerFragment();
			List<Album> albums = (List<Album>) object;
			albumPickerFragment.setAlbums(albums);
		}
		
	}
	
	private AlbumPickerFragment getAlbumPickerFragment() {
		AlbumPickerFragment fragment = null;
		List<Fragment> fragments = mAdapter.getFragments();
		for (Fragment tmp : fragments) {
			if (((PageFragment)tmp).getType() == PageFragment.ALBUM_FRAGMENT_TYPE) {
				fragment = (AlbumPickerFragment) tmp;
				break;
			}
		}
		return fragment;
	}
	
	private void onAllFragmentsCreated() {
		if (restoring) {
			mAdapter = new PagerAdapter(getSupportFragmentManager(), mFragments);
			mPager.setAdapter(mAdapter);
		}
		List<Album> albums = OrangeApplication.getInstance().getAlbums();
		if (albums == null) {
			CacheCreateTask tmp = new CacheCreateTask(this, new CacheTaskCallback());
			tmp.execute();
		} else {
			AlbumPickerFragment albumPickerFragment = getAlbumPickerFragment();
			albumPickerFragment.setAlbums(albums);
		}
	}
	
	public class FragmentCreatedCallback implements IFragmentCreateCallback {
		
		private int createdViews = 0;
		private int viewsToCreate = 1;
		
		@Override
		public void onFragmentCreated(final Fragment fragment) {
			createdViews++;
			PageFragment newFragment = (PageFragment) fragment;
			if (restoring) {
				mFragments.add(newFragment);
			}
			if (createdViews >= viewsToCreate) {
				onAllFragmentsCreated();
			}
		}
		
	}
	
	private class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int pageNumber) {
			getSupportActionBar().setSelectedNavigationItem(pageNumber);
		}
		
	}
	
	private class MyTabListener implements ActionBar.TabListener {

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			mPager.setCurrentItem(tab.getPosition());
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}
       
	}

}
