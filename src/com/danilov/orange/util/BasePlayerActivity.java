package com.danilov.orange.util;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.internal.app.ActionBarImpl;
import com.actionbarsherlock.view.Menu;

public abstract class BasePlayerActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Typeface TF = Typeface.createFromAsset(getApplication().getAssets(),
                "fonts/logo.ttf");
		int actionBarTitle = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
		TextView actionBarTitleView = (TextView) getWindow().findViewById(actionBarTitle);
		if (actionBarTitleView == null) {
			Object actionBar = getSupportActionBar();
			if (actionBar instanceof ActionBarImpl) {
				ActionBarImpl actionBarImpl = (ActionBarImpl) actionBar;
				actionBarTitleView = actionBarImpl.getTitleTextView();
			}
        }
		if (actionBarTitleView != null) {
			actionBarTitleView.setTypeface(TF);
		}
	}

	@Override
	public abstract boolean onCreateOptionsMenu(Menu menu);

}
