package com.danilov.orange;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.danilov.orange.util.BasePlayerActivity;
import com.danilov.orange.util.PageFragment;
import com.danilov.orange.util.PagerAdapter;

public class PlaylistPickerActivity extends BasePlayerActivity {
	
	private ViewPager mPager;
	private PagerAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playlist_picker);
		mPager = (ViewPager) findViewById(R.id.pager);
		List<Fragment> fragments = new ArrayList<Fragment>();
		for (int i = 0; i < 2; i++) {
			fragments.add(PageFragment.newInstance(i));
		}
		initActionBarTabs();
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageSelected(int pageNumber) {
				getSupportActionBar().setSelectedNavigationItem(pageNumber);
			}
		    });
		mAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
		mPager.setAdapter(mAdapter);
	}
	
	private void initActionBarTabs() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    ActionBar.TabListener tabListener = new ActionBar.TabListener() {

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
	       
	    };

	    for (int i = 0; i < 2; i++) {
	        actionBar.addTab(
	                actionBar.newTab()
	                        .setText("Tab " + (i + 1))
	                        .setTabListener(tabListener));
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// TODO Auto-generated method stub
		return false;
	}

}
