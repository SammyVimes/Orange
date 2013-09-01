package com.danilov.orange.util;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {
	
	List<Fragment> mFragments;

	public PagerAdapter(final FragmentManager fm, final List<Fragment> fragments) {
		super(fm);
		mFragments = fragments;
	}
	
	public PagerAdapter(final FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(final int position) {
		return mFragments.get(position);
	}
	
	@Override
	public int getCount() {
		return mFragments.size();
	}
	
	public List<Fragment> getFragments() {
		return mFragments;
	}

}
