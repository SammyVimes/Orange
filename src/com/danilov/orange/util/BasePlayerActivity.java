package com.danilov.orange.util;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;

public abstract class BasePlayerActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Typeface TF = Typeface.createFromAsset(getApplication().getAssets(),
                "fonts/logo.ttf");
		int actionBarTitle = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
		TextView actionBarTitleView = (TextView) getWindow().findViewById(actionBarTitle);
		actionBarTitleView.setTypeface(TF);
	}

	@Override
	public abstract boolean onCreateOptionsMenu(Menu menu);

}
