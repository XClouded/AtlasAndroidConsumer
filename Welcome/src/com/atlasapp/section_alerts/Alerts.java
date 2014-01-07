
//  ==================================================================================================================
//  Alerts.java
//  ATLAS
//  Copyright (c) 2012 Atlas Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-11-09 NGHIA:  Created
//  ==================================================================================================================

package com.atlasapp.section_alerts;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import com.atlasapp.common.SlideOutHelper;
import com.atlasapp.section_appentry.R;

public class Alerts extends FragmentActivity {

	public static AlertFragment fragment = new AlertFragment();
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
		getSupportFragmentManager().beginTransaction()
				.add(R.id.slideout_placeholder, fragment, "menu")
				.commit();
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

}
