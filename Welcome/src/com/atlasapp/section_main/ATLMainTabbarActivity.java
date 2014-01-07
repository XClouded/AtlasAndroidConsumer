//  ==================================================================================================================
//  ATLMainTabbarActivity.java
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

package com.atlasapp.section_main;




import com.atlasapp.section_appentry.R;
import com.atlasapp.section_appentry.Welcome;
import com.atlasapp.section_calendar.ATLCalendarActivity;
import com.atlasapp.section_notes.ATLNoteListActivity;
import com.atlasapp.section_tasks.ATLTasksActivity;
import com.atlasapp.section_today.ATLTodayActivity;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.ViewFlipper;
import android.widget.TabHost.OnTabChangeListener;

@SuppressWarnings("deprecation")
public class ATLMainTabbarActivity extends TabActivity{

	public static final int TAB_CONTACTS = 0;
	public static final int TAB_CALENDAR = 1;
	public static final int TAB_TASKS = 2;
	public static final int TAB_NOTES = 3;
	public static final int TAB_SETTINGS = 4;

	/** Called when the activity is first created. */
	public static TabHost tabHost;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_tabbar_01);

		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();

		tabHost.addTab(tabHost.newTabSpec("Contacts").setIndicator("Contacts")
				.setContent(new Intent(this, ATLTodayActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("Cal").setIndicator("Cal")
				.setContent(new Intent(this, ATLCalendarActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("Todo").setIndicator("Todo")
				.setContent(new Intent(this, ATLTasksActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("Notes").setIndicator("Notes")
				.setContent(new Intent(this, ATLNoteListActivity.class)));
//		tabHost.addTab(tabHost.newTabSpec("Settings").setIndicator("Settings")
//				.setContent(new Intent(this, ATLSettingsActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("Settings").setIndicator("Settings")
				.setContent(new Intent(this, Welcome.class)));

		tabHost.setCurrentTab(TAB_CONTACTS);

		setMainMenuListener();
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				switch (tabHost.getCurrentTab()) {

				case TAB_CONTACTS:

					break;
				case TAB_CALENDAR:

					break;
				case TAB_TASKS:

					break;
				case TAB_NOTES:

					break;
				case TAB_SETTINGS:

					break;
				default:
					break;
				}
			}
		});
	}

	private void setMainMenuListener() {

		RadioGroup mainBtns = (RadioGroup) findViewById(R.id.tabBarIncludeLayout);
		RadioGroup.OnCheckedChangeListener rdGrpCheckedListener = new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				// ViewFlipper vf = (ViewFlipper) findViewById(R.id.content_vf);
				// ImageView mainTitle = (ImageView)
				// findViewById(R.id.mainTitleLabel);
				// mainTitle.setVisibility(0);

				// TODO Auto-generated method stub
				// setCategoryinList(position, checkedId);
				switch (checkedId) {
				case R.id.btnCal:
					// mainTitle
					// .setBackgroundResource(R.drawable.settings_title_bar_title);
					// vf.setDisplayedChild(1);
					tabHost.setCurrentTab(TAB_CALENDAR);

					break;
				case R.id.btnNotes:
					// mainTitle
					// .setBackgroundResource(R.drawable.settings_title_bar_title);
					// vf.setDisplayedChild(3);
					tabHost.setCurrentTab(TAB_NOTES);
					break;
				case R.id.btnSettings:
					// mainTitle
					// .setBackgroundResource(R.drawable.settings_title_bar_title);
					// vf.setDisplayedChild(4);
					tabHost.setCurrentTab(TAB_SETTINGS);

					break;
				case R.id.btnToday:
					// mainTitle
					// .setBackgroundResource(R.drawable.settings_title_bar_title);
					// vf.setDisplayedChild(0);
					tabHost.setCurrentTab(TAB_CONTACTS);
					break;
				case R.id.btnToDo:
					// mainTitle
					// .setBackgroundResource(R.drawable.settings_title_bar_title);
					// vf.setDisplayedChild(2);
					tabHost.setCurrentTab(TAB_TASKS);
					break;
				default:
					// mainTitle
					// .setBackgroundResource(R.drawable.settings_title_bar_title);
					// vf.setDisplayedChild(0);
					break;
				}
			}
		};
		
		mainBtns.setOnCheckedChangeListener(rdGrpCheckedListener);
		mainBtns.check(R.id.btnToday);

	}

}
