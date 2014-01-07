//  ==================================================================================================================
//  ATLContactSettingsActivity.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2013-02-23 TAN:    Create class
//  ==================================================================================================================

package com.atlasapp.section_contacts;

import com.atlasapp.common.ATLAnimationUtils;
import com.atlasapp.common.ATLDragAndDropView;
import com.atlasapp.section_alerts.ATLAlertFragment_2;
import com.atlasapp.section_alerts.AlertFragment;
import com.atlasapp.section_appentry.R;
import com.atlasapp.section_slidemenu.ATLSlideMenuFragment;
import com.ericharlow.DragNDrop.DragListener;
import com.ericharlow.DragNDrop.DropListener;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

/**
 * @author Ryan Tan
 *
 */
public class ATLContactSettingsActivity extends FragmentActivity implements  View.OnClickListener
{
	private LayoutInflater mInflater;
	private ViewGroup mLayout;

	public Context mContext;
	
	ImageButton sortBtn;
	public boolean isFragmentShowing;
	public ImageButton alertBtn;
	public ATLDragAndDropView contactSettingsHolderView;
	public ViewGroup alertHolderView;
	public ViewGroup slideMenuHolderView;
	public View contactSettingsViewCover;
	public int topMenuBarHeight;
	public ATLSlideMenuFragment slideMenuFragment;
	public ATLAlertFragment_2 alertFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = LayoutInflater.from(this);
		mLayout = (ViewGroup) mInflater.inflate(R.layout.contact_settings, null);
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
  
	private void initAttributes() {
		// TODO Auto-generated method stub
		sortBtn = (ImageButton) findViewById(R.id.sortButton);
		alertBtn = (ImageButton) findViewById(R.id.alliesBtn);
		//==========================================================
		// 2013-02-21 TAN: new slide out # START
		//==========================================================
		contactSettingsHolderView = (ATLDragAndDropView)mLayout.findViewById(R.id.settings_list_content);
		alertHolderView = (ViewGroup)mLayout.findViewById(R.id.calendar_day_view_alert_fragment);
		slideMenuHolderView = (ViewGroup)mLayout.findViewById(R.id.calendar_day_view_cal_selects_fragment);
		contactSettingsViewCover = (View)mLayout.findViewById(R.id.calendar_day_view_cover_view);
		contactSettingsViewCover.setOnTouchListener(contactSettingsHolderView);
	    // top menu bar height
	    topMenuBarHeight = (int) Math.ceil(50 * this.getResources().getDisplayMetrics().density);
	    
		 if (contactSettingsHolderView instanceof ATLDragAndDropView) {
			 ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
			 contactSettingsHolderView.setDropListener(mDropListener);
			 contactSettingsHolderView.setDragListener(mDragListener);
	     }
		 
		slideMenuFragment = new ATLSlideMenuFragment();
			getSupportFragmentManager().beginTransaction()
			.add(R.id.calendar_day_view_cal_selects_fragment, slideMenuFragment).commit();
		alertFragment = ATLAlertFragment_2.getInstance();
			getSupportFragmentManager().beginTransaction()
			.add(R.id.calendar_day_view_alert_fragment, alertFragment).commit();
		//==========================================================
		// 2013-02-21 TAN: new slide out # END
		//==========================================================
	}

	private void bindingData() {
		// TODO Auto-generated method stub
		
	}

	private void setListener() {
		// TODO Auto-generated method stub
		
		
		sortBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(!isFragmentShowing){
					alertHolderView.setVisibility(View.GONE);
					slideMenuHolderView.setVisibility(View.VISIBLE);
					contactSettingsViewCover.setVisibility(View.VISIBLE);
					contactSettingsHolderView.setX(contactSettingsHolderView.LEFTRIGHT_WINDOW_WIDTH);
					ATLDragAndDropView.topMenuBarHeight = contactSettingsHolderView.getHeight();
					ATLDragAndDropView.isAtRight = true;
					ATLDragAndDropView.isAtLeft = false;
					contactSettingsHolderView.startAnimation(ATLAnimationUtils.mainFragmentMoveRight(contactSettingsHolderView.LEFTRIGHT_WINDOW_WIDTH));
					isFragmentShowing = true;
				}
				else{
					contactSettingsViewCover.setVisibility(View.GONE);
					contactSettingsHolderView.setX(0);
					ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
					contactSettingsHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromRight(contactSettingsHolderView.LEFTRIGHT_WINDOW_WIDTH));
					isFragmentShowing = false;
					onResume();
				}
				// 2013-02-03 TAN: new slide out # END
			}
		});
		
		alertBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(!isFragmentShowing){
					alertHolderView.setVisibility(View.VISIBLE);
					slideMenuHolderView.setVisibility(View.GONE);
					contactSettingsViewCover.setVisibility(View.VISIBLE);
					ATLDragAndDropView.topMenuBarHeight = contactSettingsHolderView.getHeight();
					contactSettingsHolderView.setX(-contactSettingsHolderView.LEFTRIGHT_WINDOW_WIDTH);
					contactSettingsHolderView.startAnimation(ATLAnimationUtils.mainFragmentMoveLeft(contactSettingsHolderView.LEFTRIGHT_WINDOW_WIDTH));
					alertFragment.onResume();// Reload view data
					isFragmentShowing = true;
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = true;
				}
				else{
					ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
					contactSettingsViewCover.setVisibility(View.GONE);
					contactSettingsHolderView.setX(0);
					contactSettingsHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromLeft());
					isFragmentShowing = false;
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
				}
				
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	// ===============================================================================
	// 2013-02-23 TAN: Implement Swipe left and right # START
	// ===============================================================================
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			contactSettingsViewCover.setVisibility(View.GONE);
			contactSettingsHolderView.setX(0);
			contactSettingsHolderView.setVisibility(View.VISIBLE);
			ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
			
			if(ATLDragAndDropView.isAtRight){
				contactSettingsHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromRight(contactSettingsHolderView.LEFTRIGHT_WINDOW_WIDTH));
				ATLDragAndDropView.isAtRight = false;
				ATLDragAndDropView.isAtLeft = false;
				isFragmentShowing = false;
				onResume();
				return true;
			}
			else if(ATLDragAndDropView.isAtLeft){
				contactSettingsHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromLeft());
				ATLDragAndDropView.isAtRight = false;
				ATLDragAndDropView.isAtLeft = false;
				isFragmentShowing = false;
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
		
	}

	private DragListener mDragListener =
		new DragListener() {
			public void onDrag(int x, int y, ListView listView) {
				// TODO Auto-generated method stub
				
				if(x <= 0){
					//isSwipeLeft = true;
					alertHolderView.setVisibility(View.VISIBLE);
					slideMenuHolderView.setVisibility(View.GONE);
				}
				else{
					//isSwipeLeft = false;
					alertHolderView.setVisibility(View.GONE);
					slideMenuHolderView.setVisibility(View.VISIBLE);
				}
				
			}
	
			public void onStartDrag(View itemView) {
				contactSettingsHolderView.setVisibility(View.GONE);
			}
	
			public void onStopDrag(View itemView) {
				contactSettingsHolderView.setVisibility(View.VISIBLE);
				float x = itemView.getX();
				float y = itemView.getY();
				Log.v("CalendarDayView", "onStopDrag  =====   "+ x +" ===  "+y);
			}
			
	};	

	private DropListener mDropListener = 
		new DropListener() {
        public void onDrop(int from, int to) {
        	if(contactSettingsHolderView.getVisibility() != View.VISIBLE){
        		contactSettingsHolderView.setVisibility(View.VISIBLE);
        	}
        	
        	if(ATLDragAndDropView.isTouched){
        		ATLDragAndDropView.isTouched = false;
        		contactSettingsViewCover.setVisibility(View.GONE);
    			contactSettingsHolderView.setX(0);
    			contactSettingsHolderView.setVisibility(View.VISIBLE);
    			ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
    			
    			if(ATLDragAndDropView.isAtRight){
    				contactSettingsHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromRight(contactSettingsHolderView.LEFTRIGHT_WINDOW_WIDTH));
    				ATLDragAndDropView.isAtRight = false;
    				ATLDragAndDropView.isAtLeft = false;
    				isFragmentShowing = false;
    				onResume();
    			}
    			else if(ATLDragAndDropView.isAtLeft){
    				contactSettingsHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromLeft());
    				ATLDragAndDropView.isAtRight = false;
    				ATLDragAndDropView.isAtLeft = false;
    				isFragmentShowing = false;
					onResume();
    			}
        	}
        	else if(ATLDragAndDropView.isAtLeft){
        		if(from > to){
        		
        		}
        		else if(to > from && to > 300){
        			ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
					contactSettingsViewCover.setVisibility(View.GONE);
					contactSettingsHolderView.setX(0);
					contactSettingsHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromLeftAt(to, contactSettingsHolderView.LEFTRIGHT_WINDOW_WIDTH));
					isFragmentShowing = false;
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
					onResume();
    	        }
			}else if(ATLDragAndDropView.isAtRight){
				if(from < to){
	        		
        		}
        		else if(from > to && to < 450){
        			contactSettingsViewCover.setVisibility(View.GONE);
					contactSettingsHolderView.setX(0);
					ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
					contactSettingsHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromRightAt(to 
							- (from - contactSettingsHolderView.LEFTRIGHT_WINDOW_WIDTH)));
					isFragmentShowing = false;
					onResume();
    	        }
			}else{
				if(from > to && to < 360){
	        		
	        		alertHolderView.setVisibility(View.VISIBLE);
					slideMenuHolderView.setVisibility(View.GONE);
					contactSettingsViewCover.setVisibility(View.VISIBLE);
					contactSettingsHolderView.setX(-contactSettingsHolderView.LEFTRIGHT_WINDOW_WIDTH);
					contactSettingsHolderView.startAnimation(ATLAnimationUtils.mainFragmentMoveLeftAt(from - to, contactSettingsHolderView.LEFTRIGHT_WINDOW_WIDTH));
					ATLDragAndDropView.topMenuBarHeight = contactSettingsHolderView.getHeight();
					alertFragment.onResume();// Reload view data
					isFragmentShowing = true;
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = true;
					//onResume();
					
	        	}
	        	else if(to > from && to > 360){
	        		
	        		alertHolderView.setVisibility(View.GONE);
					slideMenuHolderView.setVisibility(View.VISIBLE);
					contactSettingsViewCover.setVisibility(View.VISIBLE);
					contactSettingsHolderView.setX(contactSettingsHolderView.LEFTRIGHT_WINDOW_WIDTH);
					contactSettingsHolderView.startAnimation(ATLAnimationUtils.mainFragmentMoveRightAt(to - from, contactSettingsHolderView.LEFTRIGHT_WINDOW_WIDTH));
					ATLDragAndDropView.topMenuBarHeight = contactSettingsHolderView.getHeight();
					isFragmentShowing = true;
					ATLDragAndDropView.isAtRight = true;
					ATLDragAndDropView.isAtLeft = false;
					//onResume();
	        	}
			}
        	
        }
	};
	// ===============================================================================
	// 2013-02-23 TAN: Implement Swipe left and right # END
	// ===============================================================================	
	

}
