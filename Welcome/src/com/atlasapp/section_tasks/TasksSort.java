package com.atlasapp.section_tasks;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.atlasapp.common.SlideOutHelper;
import com.atlasapp.section_appentry.R;

public class TasksSort extends FragmentActivity implements
		TasksFragmentDelegate {
	public static TasksFragment fragment = new TasksFragment();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boolean rightSwipe = false;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			rightSwipe = extras.getBoolean("rightSwipe");
		}
		mSlideoutHelper = new SlideOutHelper(this, rightSwipe);
		mSlideoutHelper.activate();
		
		fragment.delegate = this;
		getSupportFragmentManager().beginTransaction()
				.add(R.id.slideout_placeholder, fragment, "menu").commit();
		mSlideoutHelper.open();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mSlideoutHelper.close();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public SlideOutHelper getSlideoutHelper() {
		return mSlideoutHelper;
	}

	private SlideOutHelper mSlideoutHelper;

	@Override
	public void didChangeSortOrder(int[] p) {
		// TODO Auto-generated method stub

	}

}
