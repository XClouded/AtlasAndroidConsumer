//  ==================================================================================================================
//  ATLSlideMenuFragment.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2013-02-14 TAN:     Create class
//  ==================================================================================================================

package com.atlasapp.section_slidemenu;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atlasapp.common.ATLAnimationUtils;
import com.atlasapp.common.ATLDragAndDropView;
import com.atlasapp.common.AtlasAndroidUser;
import com.atlasapp.section_alerts.ATLAlertFragment_2;
import com.atlasapp.section_appentry.AtlasApplication;
import com.atlasapp.section_appentry.R;
import com.atlasapp.section_calendar.CalendarDayView;
import com.atlasapp.section_contacts.ATLContactListActivity;
import com.atlasapp.section_contacts.ATLContactSettingsActivity;
import com.atlasapp.section_settings.ATLCalendarSettingsSelectCalendarActivity;
import com.atlasapp.section_settings.ATLMyProfile;
import com.atlasapp.section_settings.ATLSettingsActivity;
import com.atlasapp.section_settings.EditProfile;
import com.atlasapp.section_settings.FriendsFinder;
import com.atlasapp.section_settings.Settings;
import com.atlasapp.section_tasks.ATLTaskListActivity;

/**
 * @author Ryan Tan
 * 
 */
public class ATLSlideMenuFragment extends Fragment implements
		View.OnClickListener {

	LayoutInflater mInflater;
	Context mContext;
	View mLayout;
	TextView userNameTextView;
	ImageView userAvatar;

	View todayCell;
	View calendarCell;
	View mapViewCell;
	View taskListCell;
	View contactCell;

	View findMyFriendCell;
	View myProfileCell;
	View settingsCell;

	ImageButton calSettings;
	ImageButton taskSettings;
	ImageButton contactSettings;

	private FragmentActivity calendarActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		calendarActivity = getActivity();
		mContext = (Context) getActivity();
		mInflater = LayoutInflater.from(calendarActivity);
		mLayout = (View) mInflater.inflate(R.layout.slide_menu_3, null);
		initAttributes();
		setListener();
		bindingData();

		return mLayout;
	}

	private void initAttributes() {
		// TODO Auto-generated method stub
		userNameTextView = (TextView) mLayout
				.findViewById(R.id.cal_select_user_name_textview);
		userAvatar = (ImageView) mLayout
				.findViewById(R.id.cal_select_user_avatar);

		todayCell = (View) mLayout
				.findViewById(R.id.slide_menu_table_row_today);
		calendarCell = (View) mLayout
				.findViewById(R.id.slide_menu_table_row_calendars);
		mapViewCell = (View) mLayout
				.findViewById(R.id.slide_menu_table_row_map_view);
		taskListCell = (View) mLayout
				.findViewById(R.id.slide_menu_table_row_tasks);
		contactCell = (View) mLayout
				.findViewById(R.id.slide_menu_table_row_contacts);

		findMyFriendCell = (View) mLayout
				.findViewById(R.id.slide_menu_table_row_find_friends);
		myProfileCell = (View) mLayout
				.findViewById(R.id.slide_menu_table_row_my_profile);
		settingsCell = (View) mLayout
				.findViewById(R.id.slide_menu_table_row_settings);

		calSettings = (ImageButton) mLayout
				.findViewById(R.id.slide_menu_calendars_settings);
		taskSettings = (ImageButton) mLayout
				.findViewById(R.id.slide_menu_tasks_settings);
		contactSettings = (ImageButton) mLayout
				.findViewById(R.id.slide_menu_contacts_settings);

	}

	private void setListener() {
		// TODO Auto-generated method stub
		todayCell.setOnClickListener(this);
		calendarCell.setOnClickListener(this);
		mapViewCell.setOnClickListener(this);
		taskListCell.setOnClickListener(this);
		contactCell.setOnClickListener(this);

		findMyFriendCell.setOnClickListener(this);
		myProfileCell.setOnClickListener(this);
		settingsCell.setOnClickListener(this);

		calSettings.setOnClickListener(this);
		taskSettings.setOnClickListener(this);
		contactSettings.setOnClickListener(this);
	}

	private void bindingData() {
		// TODO Auto-generated method stub
		userNameTextView.setText(AtlasAndroidUser.getUserNameDisplay());

		// 2013-01-09 TAN : add profile picture, temp solution
		Bitmap storedBitmap = null;
		File imgFile = AtlasApplication.PROFILE_PIC_PATH;
		if (imgFile.exists())
			storedBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		userAvatar.setImageBitmap(storedBitmap);
		// 2013-01-09 TAN : add profile picture, temp solution

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (ATLDragAndDropView.isAtRight) {

			if (v == todayCell) {
				Toast.makeText(getActivity(), "Today \nComming soon",
						Toast.LENGTH_SHORT).show();
			} else if (v == calendarCell) {
				if (getActivity() instanceof CalendarDayView) {
					CalendarDayView mActivity = (CalendarDayView) getActivity();
					mActivity.calendarViewCover.setVisibility(View.GONE);
					mActivity.calendarHolderView.setX(0);
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
					mActivity.calendarHolderView
							.startAnimation(ATLAnimationUtils
									.mainFragmentBackFromRight(mActivity.calendarHolderView.LEFTRIGHT_WINDOW_WIDTH));
					mActivity.isFragmentShowing = false;
					mActivity.onResume();
				} else {
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
					
					getActivity().getSupportFragmentManager().beginTransaction()
					.remove(ATLAlertFragment_2.getInstance()).commit();
					
					Intent intentTasks = new Intent(getActivity(),
							CalendarDayView.class);
					intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intentTasks);
					getActivity().overridePendingTransition(
							R.anim.main_fragment_back_from_right,
							R.anim.stand_by);
					getActivity().finish();
					System.gc();
				}

			} else if (v == mapViewCell) {
				Toast.makeText(getActivity(), "Map View \nComming soon",
						Toast.LENGTH_SHORT).show();
			} else if (v == taskListCell) {
				if (getActivity() instanceof ATLTaskListActivity) {
					ATLTaskListActivity mActivity = (ATLTaskListActivity) getActivity();
					mActivity.taskListViewCover.setVisibility(View.GONE);
					mActivity.taskListHolderView.setX(0);
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
					mActivity.taskListHolderView
							.startAnimation(ATLAnimationUtils
									.mainFragmentBackFromRight(mActivity.taskListHolderView.LEFTRIGHT_WINDOW_WIDTH));
					mActivity.isFragmentShowing = false;
					mActivity.onResume();
				} else {
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
					
					getActivity().getSupportFragmentManager().beginTransaction()
					.remove(ATLAlertFragment_2.getInstance()).commit();
					
					Intent intentTasks = new Intent(getActivity(),
							ATLTaskListActivity.class);
					intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intentTasks);
					getActivity().overridePendingTransition(
							R.anim.main_fragment_back_from_right,
							R.anim.stand_by);
					getActivity().finish();
					System.gc();
				}
			} else if (v == contactCell) {
				if (getActivity() instanceof ATLContactListActivity) {
					ATLContactListActivity mActivity = (ATLContactListActivity) getActivity();
					mActivity.contactViewCover.setVisibility(View.GONE);
					mActivity.contactHolderView.setX(0);
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
					mActivity.contactHolderView
							.startAnimation(ATLAnimationUtils
									.mainFragmentBackFromRight(mActivity.contactHolderView.LEFTRIGHT_WINDOW_WIDTH));
					mActivity.isFragmentShowing = false;
					mActivity.onResume();
				} else {
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
					
					getActivity().getSupportFragmentManager().beginTransaction()
					.remove(ATLAlertFragment_2.getInstance()).commit();
					
					Intent intentTasks = new Intent(getActivity(),
							ATLContactListActivity.class);
					intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intentTasks);
					getActivity().overridePendingTransition(
							R.anim.main_fragment_back_from_right,
							R.anim.stand_by);
					getActivity().finish();
					System.gc();
				}
			} else if (v == findMyFriendCell) {
				if (getActivity() instanceof FriendsFinder) {
					FriendsFinder mActivity = (FriendsFinder) getActivity();
					mActivity.findFriendViewCover.setVisibility(View.GONE);
					mActivity.findFriendHolderView.setX(0);
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
					mActivity.findFriendHolderView
							.startAnimation(ATLAnimationUtils
									.mainFragmentBackFromRight(mActivity.findFriendHolderView.LEFTRIGHT_WINDOW_WIDTH));
					mActivity.isFragmentShowing = false;
					mActivity.onResume();
				} else {
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
					
					getActivity().getSupportFragmentManager().beginTransaction()
					.remove(ATLAlertFragment_2.getInstance()).commit();
					
					Intent intentTasks = new Intent(getActivity(),
							FriendsFinder.class);
					intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intentTasks);
					getActivity().overridePendingTransition(
							R.anim.main_fragment_back_from_right,
							R.anim.stand_by);
					getActivity().finish();
					System.gc();
				}

			} else if (v == myProfileCell) {
				if (getActivity() instanceof ATLMyProfile) {
					ATLMyProfile mActivity = (ATLMyProfile) getActivity();
					mActivity.editProfileViewCover.setVisibility(View.GONE);
					mActivity.editProfileHolderView.setX(0);
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
					mActivity.editProfileHolderView
							.startAnimation(ATLAnimationUtils
									.mainFragmentBackFromRight(mActivity.editProfileHolderView.LEFTRIGHT_WINDOW_WIDTH));
					mActivity.isFragmentShowing = false;
					mActivity.onResume();
				} else {
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
					
					getActivity().getSupportFragmentManager().beginTransaction()
					.remove(ATLAlertFragment_2.getInstance()).commit();
					
					Intent intentTasks = new Intent(getActivity(),
							ATLMyProfile.class);
					intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intentTasks);
					getActivity().overridePendingTransition(
							R.anim.main_fragment_back_from_right,
							R.anim.stand_by);
					getActivity().finish();
					System.gc();
				}

			} else if (v == settingsCell) {
//				if (getActivity() instanceof ATLSettingsActivity) {
//					ATLSettingsActivity mActivity = (ATLSettingsActivity) getActivity();
//					mActivity.settingsViewCover.setVisibility(View.GONE);
//					mActivity.settingsHolderView.setX(0);
//					ATLDragAndDropView.isAtRight = false;
//					ATLDragAndDropView.isAtLeft = false;
//					mActivity.settingsHolderView
//							.startAnimation(ATLAnimationUtils
//									.mainFragmentBackFromRight(mActivity.settingsHolderView.LEFTRIGHT_WINDOW_WIDTH));
//					mActivity.isFragmentShowing = false;
//					mActivity.onResume();
//				} else {
//					ATLDragAndDropView.isAtRight = false;
//					ATLDragAndDropView.isAtLeft = false;
//					Intent intentTasks = new Intent(getActivity(),
//							ATLSettingsActivity.class);
//					intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					startActivity(intentTasks);
//					getActivity().overridePendingTransition(
//							R.anim.main_fragment_back_from_right,
//							R.anim.stand_by);
//					getActivity().finish();
//					System.gc();
//				}
				
				Intent intentCalSettings = new Intent(getActivity(),
						Settings.class);
				intentCalSettings.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intentCalSettings);
				getActivity().overridePendingTransition(
						R.anim.main_fragment_back_from_right,
						R.anim.stand_by);
//				getActivity().finish();
				System.gc();

			} else if (v == calSettings) {
				if (getActivity() instanceof ATLCalendarSettingsSelectCalendarActivity) {
					ATLCalendarSettingsSelectCalendarActivity mActivity = (ATLCalendarSettingsSelectCalendarActivity) getActivity();
					mActivity.calendarViewCover.setVisibility(View.GONE);
					mActivity.calendarHolderView.setX(0);
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
					mActivity.calendarHolderView
							.startAnimation(ATLAnimationUtils
									.mainFragmentBackFromRight(mActivity.calendarHolderView.LEFTRIGHT_WINDOW_WIDTH));
					mActivity.isFragmentShowing = false;
					mActivity.onResume();
				} else {
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
					
					getActivity().getSupportFragmentManager().beginTransaction()
					.remove(ATLAlertFragment_2.getInstance()).commit();
					
					Intent intentCalSettings = new Intent(getActivity(),
							ATLCalendarSettingsSelectCalendarActivity.class);
					intentCalSettings.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intentCalSettings);
					getActivity().overridePendingTransition(
							R.anim.main_fragment_back_from_right,
							R.anim.stand_by);
					getActivity().finish();
					System.gc();
				}

			} else if (v == taskSettings) {
				Toast.makeText(getActivity(), "Tasks Settings \nComming soon",
						Toast.LENGTH_SHORT).show();
			} else if (v == contactSettings) {
				// Toast.makeText(getActivity(),
				// "Contacts Settings \nComming soon",
				// Toast.LENGTH_SHORT).show();
				if (getActivity() instanceof ATLContactSettingsActivity) {
					ATLContactSettingsActivity mActivity = (ATLContactSettingsActivity) getActivity();
					mActivity.contactSettingsViewCover.setVisibility(View.GONE);
					mActivity.contactSettingsHolderView.setX(0);
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
					mActivity.contactSettingsHolderView
							.startAnimation(ATLAnimationUtils
									.mainFragmentBackFromRight(mActivity.contactSettingsHolderView.LEFTRIGHT_WINDOW_WIDTH));
					mActivity.isFragmentShowing = false;
					mActivity.onResume();
				} else {
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
					
					getActivity().getSupportFragmentManager().beginTransaction()
					.remove(ATLAlertFragment_2.getInstance()).commit();
					
					Intent intentTasks = new Intent(getActivity(),
							ATLContactSettingsActivity.class);
					intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intentTasks);
					getActivity().overridePendingTransition(
							R.anim.main_fragment_back_from_right,
							R.anim.stand_by);
					getActivity().finish();
					System.gc();
				}
			}
		}
		else{
//			Toast.makeText(getActivity(), "Notice \nSlideMenu is not visible",
//					Toast.LENGTH_SHORT).show();
		}
	}
}
