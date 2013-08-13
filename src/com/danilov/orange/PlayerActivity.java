package com.danilov.orange;

import com.actionbarsherlock.view.Menu;
import com.danilov.orange.util.BasePlayerActivity;

import android.os.Bundle;

public class PlayerActivity extends BasePlayerActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.player, menu);
		return true;
	}

}
