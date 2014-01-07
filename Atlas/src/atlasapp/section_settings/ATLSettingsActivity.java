//  ==================================================================================================================
//  ATLSettingsActivity.java
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

package atlasapp.section_settings;

import atlasapp.common.ATLAnimationUtils;
import atlasapp.common.ATLDragAndDropView;
import atlasapp.common.ATLTabbarViewDelegete;
import atlasapp.section_alerts.AlertFragment;
import atlasapp.section_appentry.R;
import atlasapp.section_contacts.ATLContactListActivity;
import atlasapp.slidemenu.ATLSlideMenuFragment;
import com.ericharlow.DragNDrop.DragListener;
import com.ericharlow.DragNDrop.DropListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class ATLSettingsActivity extends FragmentActivity implements
		ATLTabbarViewDelegete, View.OnClickListener {
	private LayoutInflater mInflater;
	private ViewGroup mLayout;

	public Context mContext;
	private Button calendarSettingsSelectCalendarButton;
	private Button calendarSettingsDefaultReminderButton;
	private Button calendarSettingsDefaultDurationButton;
	private Button calendarSettingsDefaultCalendarButton;
//	ImageButton sortBtn;
//	public boolean isFragmentShowing;
//	public ImageButton alertBtn;
//	public ATLDragAndDropView settingsHolderView;
//	public ViewGroup alertHolderView;
//	public ViewGroup slideMenuHolderView;
	public View settingsViewCover;
	public int topMenuBarHeight;
//	public ATLSlideMenuFragment slideMenuFragment;
//	public AlertFragment alertFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = LayoutInflater.from(this);
		mLayout = (ViewGroup) mInflater.inflate(R.layout.setting_list, null);
		setContentView(mLayout);
		mContext = this;
		initAttributes();
		setListener();
		bindingData();
	}
	
	@Override
	public void onResume(){
		super.onResume();
	}
  
	private void bindingData() {
		// TODO Auto-generated method stub
		
	}

	private void setListener() {
		// TODO Auto-generated method stub
		calendarSettingsSelectCalendarButton.setOnClickListener(this);
		calendarSettingsDefaultCalendarButton.setOnClickListener(this);
		calendarSettingsDefaultDurationButton.setOnClickListener(this);
		calendarSettingsDefaultReminderButton.setOnClickListener(this);
		
//		sortBtn.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				
//				if(!isFragmentShowing){
//					alertHolderView.setVisibility(View.GONE);
//					slideMenuHolderView.setVisibility(View.VISIBLE);
//					settingsViewCover.setVisibility(View.VISIBLE);
//					settingsHolderView.setX(settingsHolderView.LEFTRIGHT_WINDOW_WIDTH);
//					ATLDragAndDropView.topMenuBarHeight = settingsHolderView.getHeight();
//					ATLDragAndDropView.isAtRight = true;
//					ATLDragAndDropView.isAtLeft = false;
//					settingsHolderView.startAnimation(ATLAnimationUtils.mainFragmentMoveRight(settingsHolderView.LEFTRIGHT_WINDOW_WIDTH));
//					isFragmentShowing = true;
//				}
//				else{
//					settingsViewCover.setVisibility(View.GONE);
//					settingsHolderView.setX(0);
//					ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
//					ATLDragAndDropView.isAtRight = false;
//					ATLDragAndDropView.isAtLeft = false;
//					settingsHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromRight(settingsHolderView.LEFTRIGHT_WINDOW_WIDTH));
//					isFragmentShowing = false;
//					onResume();
//				}
//				// 2013-02-03 TAN: new slide out # END
//			}
//		});
		
//		alertBtn.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				
//				if(!isFragmentShowing){
//					alertHolderView.setVisibility(View.VISIBLE);
//					slideMenuHolderView.setVisibility(View.GONE);
//					settingsViewCover.setVisibility(View.VISIBLE);
//					ATLDragAndDropView.topMenuBarHeight = settingsHolderView.getHeight();
//					settingsHolderView.setX(-settingsHolderView.LEFTRIGHT_WINDOW_WIDTH);
//					settingsHolderView.startAnimation(ATLAnimationUtils.mainFragmentMoveLeft(settingsHolderView.LEFTRIGHT_WINDOW_WIDTH));
//					alertFragment.onResume();// Reload view data
//					isFragmentShowing = true;
//					ATLDragAndDropView.isAtRight = false;
//					ATLDragAndDropView.isAtLeft = true;
//				}
//				else{
//					ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
//					settingsViewCover.setVisibility(View.GONE);
//					settingsHolderView.setX(0);
//					settingsHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromLeft());
//					isFragmentShowing = false;
//					ATLDragAndDropView.isAtRight = false;
//					ATLDragAndDropView.isAtLeft = false;
//				}
//				
//			}
//		});
		
	}

	private void initAttributes() {
		// TODO Auto-generated method stub
		calendarSettingsSelectCalendarButton = (Button)findViewById(R.id.calendar_settings_select_calendar_button);
		calendarSettingsDefaultCalendarButton = (Button)findViewById(R.id.calendar_settings_default_calendar_button);
		calendarSettingsDefaultDurationButton = (Button)findViewById(R.id.calendar_settings_default_duration_button);
		calendarSettingsDefaultReminderButton = (Button)findViewById(R.id.calendar_settings_default_reminder_button);
//		sortBtn = (ImageButton) findViewById(R.id.sortButton);
//		alertBtn = (ImageButton) findViewById(R.id.alliesBtn);
		//==========================================================
		// 2013-02-21 TAN: new slide out # START
		//==========================================================
//		settingsHolderView = (ATLDragAndDropView)mLayout.findViewById(R.id.settings_list_content);
//		alertHolderView = (ViewGroup)mLayout.findViewById(R.id.calendar_day_view_alert_fragment);
//		slideMenuHolderView = (ViewGroup)mLayout.findViewById(R.id.calendar_day_view_cal_selects_fragment);
//		settingsViewCover = (View)mLayout.findViewById(R.id.calendar_day_view_cover_view);
//		settingsViewCover.setOnTouchListener(settingsHolderView);
//	    // top menu bar height
//	    topMenuBarHeight = (int) Math.ceil(50 * this.getResources().getDisplayMetrics().density);
//	    
//		 if (settingsHolderView instanceof ATLDragAndDropView) {
//			 ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
//			 settingsHolderView.setDropListener(mDropListener);
//			 settingsHolderView.setDragListener(mDragListener);
//	     }
//		 
//		slideMenuFragment = new ATLSlideMenuFragment();
//			getSupportFragmentManager().beginTransaction()
//			.add(R.id.calendar_day_view_cal_selects_fragment, slideMenuFragment).commit();
//		alertFragment = new AlertFragment();
//			getSupportFragmentManager().beginTransaction()
//			.add(R.id.calendar_day_view_alert_fragment, alertFragment).commit();
//		//==========================================================
//		// 2013-02-21 TAN: new slide out # END
//		//==========================================================
	}

	@Override
	public void didTabToTabIndex(int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == calendarSettingsSelectCalendarButton){
			Log.v("calendarSettingsSelectCalendarButton", "calendarSettingsSelectCalendarButton touch");
			Intent intent = new Intent(getBaseContext(), ATLCalendarSettingsSelectCalendarActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
//			getActivity().overridePendingTransition(R.anim.main_fragment_back_from_right, R.anim.stand_by);
		}else if(v == calendarSettingsDefaultCalendarButton){
			Log.v("calendarSettingsDefaultCalendarButton", "calendarSettingsDefaultCalendarButton touch");
			Intent intent = new Intent(getBaseContext(), ATLCalendarSettingsDefaultCalendarActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
//			getActivity().overridePendingTransition(R.anim.main_fragment_back_from_right, R.anim.stand_by);
		}else if(v == calendarSettingsDefaultDurationButton){
			Log.v("calendarSettingsDefaultDurationButton", "calendarSettingsDefaultDurationButton touch");
			Intent intent = new Intent(getBaseContext(), ATLCalendarSettingsDefaultDurationActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
//			getActivity().overridePendingTransition(R.anim.main_fragment_back_from_right, R.anim.stand_by);
		}else if(v == calendarSettingsDefaultReminderButton){
			Log.v("calendarSettingsDefaultReminderButton", "calendarSettingsDefaultReminderButton touch");
			Intent intent = new Intent(getBaseContext(), ATLCalendarSettingsDefaultReminderActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
//			getActivity().overridePendingTransition(R.anim.main_fragment_back_from_right, R.anim.stand_by);
		}
	}
	public boolean onOptionsItemSelected(MenuItem item) 
	{
//		super.onOptionsItemSelected(item);
		switch (item.getItemId()) 
		{
		case R.id.settings:
			Toast.makeText(this, "Settings pressed",
					Toast.LENGTH_SHORT).show();
			Intent intentCalSettings = new Intent(ATLSettingsActivity.this,
					ATLSettingsActivity.class);
			intentCalSettings.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intentCalSettings);
//			CalendarDayView.this.overridePendingTransition(
//					R.anim.main_fragment_back_from_right,
//					R.anim.stand_by);
//			System.gc();
			break;
		case R.id.profile:
			Intent intentTasks = new Intent(ATLSettingsActivity.this,
					ATLMyProfile.class);
			intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intentTasks);
//			System.gc();
			break;
		case R.id.share:
			Toast.makeText(this, "Coming soon",
					Toast.LENGTH_SHORT).show();
//			Intent intentCalSettings = new Intent(CalendarDayView.this,
//					Settings.class);
//			intentCalSettings.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intentCalSettings);
////			CalendarDayView.this.overridePendingTransition(
//					R.anim.main_fragment_back_from_right,
//					R.anim.stand_by);
//			System.gc();
			break;
		case R.id.feedback:
			Toast.makeText(this, "Coming soon",
					Toast.LENGTH_SHORT).show();
//			Intent intentCalSettings = new Intent(CalendarDayView.this,
//					Settings.class);
//			intentCalSettings.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intentCalSettings);
////			CalendarDayView.this.overridePendingTransition(
//					R.anim.main_fragment_back_from_right,
//					R.anim.stand_by);
//			System.gc();
			break;

//		default:
//			return super.onOptionsItemSelected(item);
		}
		return true;
	}
	// ===============================================================================
	// 2013-02-23 TAN: Implement Swipe left and right # START
	// ===============================================================================
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
//			settingsViewCover.setVisibility(View.GONE);
//			settingsHolderView.setX(0);
//			settingsHolderView.setVisibility(View.VISIBLE);
//			ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
//			
//			if(ATLDragAndDropView.isAtRight){
//				settingsHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromRight(settingsHolderView.LEFTRIGHT_WINDOW_WIDTH));
//				ATLDragAndDropView.isAtRight = false;
//				ATLDragAndDropView.isAtLeft = false;
//				isFragmentShowing = false;
//				onResume();
//				return true;
//			}
//			else if(ATLDragAndDropView.isAtLeft){
//				settingsHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromLeft());
//				ATLDragAndDropView.isAtRight = false;
//				ATLDragAndDropView.isAtLeft = false;
//				isFragmentShowing = false;
//				return true;
//			}
		}
		return super.onKeyDown(keyCode, event);
		
	}

//	private DragListener mDragListener =
//		new DragListener() {
//			public void onDrag(int x, int y, ListView listView) {
//				// TODO Auto-generated method stub
//				
//				if(x <= 0){
//					//isSwipeLeft = true;
//					alertHolderView.setVisibility(View.VISIBLE);
//					slideMenuHolderView.setVisibility(View.GONE);
//				}
//				else{
//					//isSwipeLeft = false;
//					alertHolderView.setVisibility(View.GONE);
//					slideMenuHolderView.setVisibility(View.VISIBLE);
//				}
//				
//			}
//	
//			public void onStartDrag(View itemView) {
//				settingsHolderView.setVisibility(View.GONE);
//			}
//	
//			public void onStopDrag(View itemView) {
//				settingsHolderView.setVisibility(View.VISIBLE);
//				float x = itemView.getX();
//				float y = itemView.getY();
//				Log.v("CalendarDayView", "onStopDrag  =====   "+ x +" ===  "+y);
//			}
//			
//	};	

//	private DropListener mDropListener = 
//		new DropListener() {
//        public void onDrop(int from, int to) {
//        	if(settingsHolderView.getVisibility() != View.VISIBLE){
//        		settingsHolderView.setVisibility(View.VISIBLE);
//        	}
//        	
//        	if(ATLDragAndDropView.isTouched){
//        		ATLDragAndDropView.isTouched = false;
//        		settingsViewCover.setVisibility(View.GONE);
//    			settingsHolderView.setX(0);
//    			settingsHolderView.setVisibility(View.VISIBLE);
//    			ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
//    			
//    			if(ATLDragAndDropView.isAtRight){
//    				settingsHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromRight(settingsHolderView.LEFTRIGHT_WINDOW_WIDTH));
//    				ATLDragAndDropView.isAtRight = false;
//    				ATLDragAndDropView.isAtLeft = false;
//    				isFragmentShowing = false;
//    				onResume();
//    			}
//    			else if(ATLDragAndDropView.isAtLeft){
//    				settingsHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromLeft());
//    				ATLDragAndDropView.isAtRight = false;
//    				ATLDragAndDropView.isAtLeft = false;
//    				isFragmentShowing = false;
//					onResume();
//    			}
//        	}
//        	else if(ATLDragAndDropView.isAtLeft){
//        		if(from > to){
//        		
//        		}
//        		else if(to > from && to > 300){
//        			ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
//					settingsViewCover.setVisibility(View.GONE);
//					settingsHolderView.setX(0);
//					settingsHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromLeftAt(to, settingsHolderView.LEFTRIGHT_WINDOW_WIDTH));
//					isFragmentShowing = false;
//					ATLDragAndDropView.isAtRight = false;
//					ATLDragAndDropView.isAtLeft = false;
//					onResume();
//    	        }
//			}else if(ATLDragAndDropView.isAtRight){
//				if(from < to){
//	        		
//        		}
//        		else if(from > to && to < 450){
//        			settingsViewCover.setVisibility(View.GONE);
//					settingsHolderView.setX(0);
//					ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
//					ATLDragAndDropView.isAtRight = false;
//					ATLDragAndDropView.isAtLeft = false;
//					settingsHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromRightAt(to 
//							- (from - settingsHolderView.LEFTRIGHT_WINDOW_WIDTH)));
//					isFragmentShowing = false;
//					onResume();
//    	        }
//			}else{
//				if(from > to && to < 360){
//	        		
//	        		alertHolderView.setVisibility(View.VISIBLE);
//					slideMenuHolderView.setVisibility(View.GONE);
//					settingsViewCover.setVisibility(View.VISIBLE);
//					settingsHolderView.setX(-settingsHolderView.LEFTRIGHT_WINDOW_WIDTH);
//					settingsHolderView.startAnimation(ATLAnimationUtils.mainFragmentMoveLeftAt(from - to, settingsHolderView.LEFTRIGHT_WINDOW_WIDTH));
//					ATLDragAndDropView.topMenuBarHeight = settingsHolderView.getHeight();
//					alertFragment.onResume();// Reload view data
//					isFragmentShowing = true;
//					ATLDragAndDropView.isAtRight = false;
//					ATLDragAndDropView.isAtLeft = true;
//					//onResume();
//					
//	        	}
//	        	else if(to > from && to > 360){
//	        		
//	        		alertHolderView.setVisibility(View.GONE);
//					slideMenuHolderView.setVisibility(View.VISIBLE);
//					settingsViewCover.setVisibility(View.VISIBLE);
//					settingsHolderView.setX(settingsHolderView.LEFTRIGHT_WINDOW_WIDTH);
//					settingsHolderView.startAnimation(ATLAnimationUtils.mainFragmentMoveRightAt(to - from, settingsHolderView.LEFTRIGHT_WINDOW_WIDTH));
//					ATLDragAndDropView.topMenuBarHeight = settingsHolderView.getHeight();
//					isFragmentShowing = true;
//					ATLDragAndDropView.isAtRight = true;
//					ATLDragAndDropView.isAtLeft = false;
//					//onResume();
//	        	}
//			}
//        	
//        }
//	};
	// ===============================================================================
	// 2013-02-23 TAN: Implement Swipe left and right # END
	// ===============================================================================	

}
