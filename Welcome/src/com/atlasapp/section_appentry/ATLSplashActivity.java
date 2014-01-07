//  ==================================================================================================================
//  ATLSplashScreenActivity.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-10-21 TAN:    Init class
//  ==================================================================================================================

package com.atlasapp.section_appentry;



import com.atlasapp.section_main.ATLMainTabbarActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ATLSplashActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		// Hide the Status Bar
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);

		final int welcomeScreenDisplay = 1500;
		Thread welcomeThread = new Thread() {

			int wait = 0;

			@Override
			public void run() {
				try {
					super.run();
					/**
					 * use while to get the splash time. Use sleep() to increase
					 * the wait variable for every 100L.
					 */
					while (wait < welcomeScreenDisplay) {
						sleep(100);
						wait += 100;
					}
				} catch (Exception e) {
					System.out.println("EXc=" + e);
				} finally {

					Intent intent = new Intent(ATLSplashActivity.this, ATLMainTabbarActivity.class);
					startActivity(intent);
					finish();
				}
			}
		};
		welcomeThread.start();
	}

}