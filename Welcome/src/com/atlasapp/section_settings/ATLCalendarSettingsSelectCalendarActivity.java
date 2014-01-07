//  ==================================================================================================================
//  ATLCalendarSettingsSelectCalendarActivity.java
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


package com.atlasapp.section_settings;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlasapp.common.ATLAnimationUtils;
import com.atlasapp.common.ATLDragAndDropView;
import com.atlasapp.common.ATLTabbarViewDelegete;
import com.atlasapp.model.ATLCalendarModel;
import com.atlasapp.section_alerts.ATLAlertFragment_2;
import com.atlasapp.section_alerts.AlertFragment;
import com.atlasapp.section_appentry.R;
import com.atlasapp.section_calendar.ATLCalendarStore;
import com.atlasapp.section_calendar.CalendarDayView;
import com.atlasapp.section_slidemenu.ATLSlideMenuFragment;
import com.ericharlow.DragNDrop.DragListener;
import com.ericharlow.DragNDrop.DropListener;

public class ATLCalendarSettingsSelectCalendarActivity extends FragmentActivity
 {

	private LayoutInflater mInflater;
	private ViewGroup mLayout;
	ImageButton btnSort;
	public boolean isFragmentShowing;
	public ListView calSelectList;
	public CalendarSelectAdapter adapter;
	public ArrayList<ATLCalendarModel> calendarList;
	public ATLDragAndDropView calendarHolderView;
	public ViewGroup alertHolderView;
	public ViewGroup selectCalendarHolderView;
	public View calendarViewCover;
	public int topMenuBarHeight;
	public ATLSlideMenuFragment slideMenuFragment;
	public ATLAlertFragment_2 alertFragment;
	public ImageButton sortBtn;
	public ImageButton alliesBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = LayoutInflater.from(this);
		mLayout = (ViewGroup) mInflater.inflate(R.layout.calendar_settings_select_calendars, null);
		setContentView(mLayout);
		// calArr = ATLCalendarManager.getCalendars(calendarActivity);
		calendarList = ATLCalendarStore.loadCalendarList(this);
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
		calSelectList = (ListView) mLayout
				.findViewById(R.id.cal_select_list_view);
		sortBtn = (ImageButton) findViewById(R.id.sortBtn);
		alliesBtn = (ImageButton) findViewById(R.id.alliesBtn);
		//==========================================================
		// 2013-02-21 TAN: new slide out # START
		//==========================================================
		calendarHolderView = (ATLDragAndDropView)mLayout.findViewById(R.id.calendar_settings_select_calendar);
		alertHolderView = (ViewGroup)mLayout.findViewById(R.id.calendar_day_view_alert_fragment);
		selectCalendarHolderView = (ViewGroup)mLayout.findViewById(R.id.calendar_day_view_cal_selects_fragment);
		calendarViewCover = (View)mLayout.findViewById(R.id.calendar_day_view_cover_view);
		calendarViewCover.setOnTouchListener(calendarHolderView);
	    // top menu bar height
	    topMenuBarHeight = (int) Math.ceil(50 * this.getResources().getDisplayMetrics().density);
	    
		 if (calendarHolderView instanceof ATLDragAndDropView) {
			 ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
			 calendarHolderView.setDropListener(mDropListener);
			 calendarHolderView.setDragListener(mDragListener);
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

		loadingAllCalendar();
		saveCalendarList();
		
		adapter = new CalendarSelectAdapter(calendarList, this);
		calSelectList.setAdapter(adapter);

	}

	private void saveCalendarList() {
		ATLCalendarStore.save(calendarList, this);
	}

	private void loadingAllCalendar() {
		CalendarDayView.calendarInActiveNameArray.clear();
		for (ATLCalendarModel calendar : calendarList) {
			if (!calendar.isActive) {
				CalendarDayView.calendarInActiveNameArray.add(calendar.name);
			}
		}


	}

	private void setListener() {
		// TODO Auto-generated method stub
		calendarHolderView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//DO nothing 
			}
		});
		
		sortBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(!isFragmentShowing){
					alertHolderView.setVisibility(View.GONE);
					selectCalendarHolderView.setVisibility(View.VISIBLE);
					calendarViewCover.setVisibility(View.VISIBLE);
					calendarHolderView.setX(calendarHolderView.LEFTRIGHT_WINDOW_WIDTH);
					ATLDragAndDropView.topMenuBarHeight = calendarHolderView.getHeight();
					ATLDragAndDropView.isAtRight = true;
					ATLDragAndDropView.isAtLeft = false;
					calendarHolderView.startAnimation(ATLAnimationUtils.mainFragmentMoveRight(calendarHolderView.LEFTRIGHT_WINDOW_WIDTH));
					isFragmentShowing = true;
				}
				else{
					calendarViewCover.setVisibility(View.GONE);
					calendarHolderView.setX(0);
					ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
					calendarHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromRight(calendarHolderView.LEFTRIGHT_WINDOW_WIDTH));
					isFragmentShowing = false;
					onResume();
				}
				// 2013-02-03 TAN: new slide out # END
			}
		});
		
		alliesBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(!isFragmentShowing){
					alertHolderView.setVisibility(View.VISIBLE);
					selectCalendarHolderView.setVisibility(View.GONE);
					calendarViewCover.setVisibility(View.VISIBLE);
					ATLDragAndDropView.topMenuBarHeight = calendarHolderView.getHeight();
					calendarHolderView.setX(-calendarHolderView.LEFTRIGHT_WINDOW_WIDTH);
					calendarHolderView.startAnimation(ATLAnimationUtils.mainFragmentMoveLeft(calendarHolderView.LEFTRIGHT_WINDOW_WIDTH));
					alertFragment.onResume();// Reload view data
					isFragmentShowing = true;
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = true;
				}
				else{
					ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
					calendarViewCover.setVisibility(View.GONE);
					calendarHolderView.setX(0);
					calendarHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromLeft());
					isFragmentShowing = false;
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
				}
				
			}
		});
	}
	// ===============================================================================
	// 2013-02-03 TAN: Implement Swipe left and right # START
	// ===============================================================================
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			calendarViewCover.setVisibility(View.GONE);
			calendarHolderView.setX(0);
			calendarHolderView.setVisibility(View.VISIBLE);
			ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
			
			if(ATLDragAndDropView.isAtRight){
				calendarHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromRight(calendarHolderView.LEFTRIGHT_WINDOW_WIDTH));
				ATLDragAndDropView.isAtRight = false;
				ATLDragAndDropView.isAtLeft = false;
				isFragmentShowing = false;
				onResume();
				return true;
			}
			else if(ATLDragAndDropView.isAtLeft){
				calendarHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromLeft());
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
					selectCalendarHolderView.setVisibility(View.GONE);
				}
				else{
					//isSwipeLeft = false;
					alertHolderView.setVisibility(View.GONE);
					selectCalendarHolderView.setVisibility(View.VISIBLE);
				}
				
			}
	
			public void onStartDrag(View itemView) {
				calendarHolderView.setVisibility(View.GONE);
			}
	
			public void onStopDrag(View itemView) {
				calendarHolderView.setVisibility(View.VISIBLE);
				float x = itemView.getX();
				float y = itemView.getY();
				Log.v("CalendarDayView", "onStopDrag  =====   "+ x +" ===  "+y);
			}
			
	};	

	private DropListener mDropListener = 
		new DropListener() {
        public void onDrop(int from, int to) {
        	if(calendarHolderView.getVisibility() != View.VISIBLE){
        		calendarHolderView.setVisibility(View.VISIBLE);
        	}
        	
        	if(ATLDragAndDropView.isTouched){
        		ATLDragAndDropView.isTouched = false;
        		calendarViewCover.setVisibility(View.GONE);
    			calendarHolderView.setX(0);
    			calendarHolderView.setVisibility(View.VISIBLE);
    			ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
    			
    			if(ATLDragAndDropView.isAtRight){
    				calendarHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromRight(calendarHolderView.LEFTRIGHT_WINDOW_WIDTH));
    				ATLDragAndDropView.isAtRight = false;
    				ATLDragAndDropView.isAtLeft = false;
    				isFragmentShowing = false;
    				onResume();
    			}
    			else if(ATLDragAndDropView.isAtLeft){
    				calendarHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromLeft());
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
					calendarViewCover.setVisibility(View.GONE);
					calendarHolderView.setX(0);
					calendarHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromLeftAt(to, calendarHolderView.LEFTRIGHT_WINDOW_WIDTH));
					isFragmentShowing = false;
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
					onResume();
    	        }
			}else if(ATLDragAndDropView.isAtRight){
				if(from < to){
	        		
        		}
        		else if(from > to && to < 450){
        			calendarViewCover.setVisibility(View.GONE);
					calendarHolderView.setX(0);
					ATLDragAndDropView.topMenuBarHeight = topMenuBarHeight;
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = false;
					calendarHolderView.startAnimation(ATLAnimationUtils.mainFragmentBackFromRightAt(to 
							- (from - calendarHolderView.LEFTRIGHT_WINDOW_WIDTH)));
					isFragmentShowing = false;
					onResume();
    	        }
			}else{
				if(from > to && to < 360){
	        		
	        		alertHolderView.setVisibility(View.VISIBLE);
					selectCalendarHolderView.setVisibility(View.GONE);
					calendarViewCover.setVisibility(View.VISIBLE);
					calendarHolderView.setX(-calendarHolderView.LEFTRIGHT_WINDOW_WIDTH);
					calendarHolderView.startAnimation(ATLAnimationUtils.mainFragmentMoveLeftAt(from - to, calendarHolderView.LEFTRIGHT_WINDOW_WIDTH));
					ATLDragAndDropView.topMenuBarHeight = calendarHolderView.getHeight();
					alertFragment.onResume();// Reload view data
					isFragmentShowing = true;
					ATLDragAndDropView.isAtRight = false;
					ATLDragAndDropView.isAtLeft = true;
					//onResume();
					
	        	}
	        	else if(to > from && to > 360){
	        		
	        		alertHolderView.setVisibility(View.GONE);
					selectCalendarHolderView.setVisibility(View.VISIBLE);
					calendarViewCover.setVisibility(View.VISIBLE);
					calendarHolderView.setX(calendarHolderView.LEFTRIGHT_WINDOW_WIDTH);
					calendarHolderView.startAnimation(ATLAnimationUtils.mainFragmentMoveRightAt(to - from, calendarHolderView.LEFTRIGHT_WINDOW_WIDTH));
					ATLDragAndDropView.topMenuBarHeight = calendarHolderView.getHeight();
					isFragmentShowing = true;
					ATLDragAndDropView.isAtRight = true;
					ATLDragAndDropView.isAtLeft = false;
					//onResume();
	        	}
			}
        	
        }
	};
	// ===============================================================================
	// 2013-02-03 TAN: Implement Swipe left and right # END
	// ===============================================================================	

	// /////////////////////////////////////////////////
	// CalendarSelectAdapter START
	// /////////////////////////////////////////////////
	class CalendarSelectAdapter extends BaseAdapter {
		ArrayList<ATLCalendarModel> calModels;
		int size;
		Context mContext;

		public CalendarSelectAdapter(ArrayList<ATLCalendarModel> calArr,
				Context ctx) {

			// TODO Auto-generated constructor stub
			ATLCalendarModel allCal = new ATLCalendarModel();
			if(calArr.size() >0){
				allCal.name = "All";
				
				boolean showAll = true;
				for(ATLCalendarModel cal : calArr){
					if(!cal.isActive){
						showAll = false;
						break;
					}
				}
				Log.v("CalendarSelectAdapter", "showAll : " +showAll);
				allCal.isActive = showAll;
				calArr.add(0, allCal);
			}
			
			calModels = calArr;
			mContext = ctx;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			size = calModels.size();// Add all options
			// Hard code for basic view phase
			return size;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return calModels.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			CalendarSelectCell cell;
			if (convertView == null) {
				cell = new CalendarSelectCell(mContext);
				convertView = (View) cell;
				convertView.setTag(cell);
			} else {
				cell = (CalendarSelectCell) convertView.getTag();
			}
			ATLCalendarModel cellData = (ATLCalendarModel) calModels
					.get(position);
			cell.setCellData(cellData);
			if(size == 1){
				cell.backgroundImg.setBackgroundResource(R.drawable.one_row_with_edges2x);
			}
			else if(position == 0){
				cell.backgroundImg.setBackgroundResource(R.drawable.one_row_with_bottom_edges2x);
			}else if(position == size-1){
				cell.backgroundImg.setBackgroundResource(R.drawable.one_row_with_top_edges2x);
			}
			return convertView;
		}

	}

	// /////////////////////////////////////////////////
	// CalendarSelectAdapter END
	// /////////////////////////////////////////////////

	// /////////////////////////////////////////////////
	// CalendarSelectCell START
	// /////////////////////////////////////////////////

	class CalendarSelectCell extends RelativeLayout {

		private CheckBox showCalEventCheckBox;
		private TextView calNameTextView;
		private ImageView calColorView;
		ATLCalendarModel mCellData;
		public View backgroundImg;

		public CalendarSelectCell(Context context, AttributeSet attrs,
				int defStyle) {
			super(context, attrs, defStyle);
			// TODO Auto-generated constructor stub
			initView(context);
		}

		public void setCellData(ATLCalendarModel cellData) {
			// TODO Auto-generated method stub
			mCellData = cellData;
			if(mCellData.name.equals("All")){
			   Log.v("CalendarSelectAdapter", "showAll isActive: " +mCellData.isActive);
			}
			
			showCalEventCheckBox.setChecked(mCellData.isActive);
			String calName = cellData.name;
			if (cellData.name.length() > 25) {
				calName = calName.substring(0, 23) + "...";
			}
			calNameTextView.setText(calName);
			calColorView.setBackgroundColor(cellData.color);
		}

		public CalendarSelectCell(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO Auto-generated constructor stub
			initView(context);
		}

		public CalendarSelectCell(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			initView(context);
		}

		private void initView(Context context) {
			// TODO Auto-generated method stub
			LayoutInflater.from(context).inflate(R.layout.calendar_select_cell,
					this, true);
			showCalEventCheckBox = (CheckBox) findViewById(R.id.calendar_select_checkbox);
			calNameTextView = (TextView) findViewById(R.id.calendar_select_calendar_name);
			calColorView = (ImageView) findViewById(R.id.calendar_select_color_img);
			backgroundImg = (View) findViewById(R.id.calendar_select_row_view);
			this.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					handleCheckList();
				}
			});
			showCalEventCheckBox.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					handleCheckList();
				}
			});
		}
		
		private void handleCheckList(){
			mCellData.isActive = !mCellData.isActive;
			if(mCellData.name.equals("All") && mCellData.id == 0){
				   Log.v("CalendarSelectAdapter", "showAll isActive: " +mCellData.isActive);
				   if(mCellData.isActive){
					   for(ATLCalendarModel cal : calendarList){
						   cal.isActive = true;
					   }
				   }
				   else{
					   for(ATLCalendarModel cal : calendarList){
						   cal.isActive = false;
					   }
				   }
				
			}
			else{
				boolean showAll = true;
				saveCalendarList();
				loadingAllCalendar();
				if(mCellData.isActive){
					for(ATLCalendarModel cal : calendarList){
						if(!cal.isActive && !cal.name.equals("All") && cal.id != 0){
							showAll = false;
							break;
						}
					}
				   }
				   else{
					   showAll = false;
				 }
				Log.v("CalendarSelectAdapter", "showAll isActive: " + showAll);
				((ATLCalendarModel)adapter.getItem(0)).isActive = showAll;
			}
			saveCalendarList();
			loadingAllCalendar();
			if(!showCalEventCheckBox.isChecked()){
				showCalEventCheckBox.setChecked(true);
			}
			else{
				showCalEventCheckBox.setChecked(false);
			}
			adapter.notifyDataSetChanged();
			
		}

	}

	// /////////////////////////////////////////////////
	// CalendarSelectCell END
	// /////////////////////////////////////////////////
}
