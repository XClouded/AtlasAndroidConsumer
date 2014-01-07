//  ==================================================================================================================
//  CalendarDayView.java
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

package com.atlasapp.section_calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;

import com.atlasapp.common.ATLAddBarView;
import com.atlasapp.common.ATLAnimationUtils;
import com.atlasapp.common.ATLDragAndDropView;
import com.atlasapp.common.ATLTabbarView;
import com.atlasapp.common.ATLTabbarViewDelegete;
import com.atlasapp.common.CalendarUtilities;
import com.atlasapp.model.ATLCalendarModel;
import com.atlasapp.section_actions.Actions;
import com.atlasapp.section_alerts.ATLAlertFragment_2;
import com.atlasapp.section_appentry.R;
import com.atlasapp.section_contacts.ATLContactListActivity;
import com.atlasapp.section_contacts.Contacts;
import com.atlasapp.section_notes.ATLNoteListActivity;
import com.atlasapp.section_settings.Settings;
import com.atlasapp.section_slidemenu.ATLSlideMenuFragment;
import com.atlasapp.section_tasks.ATLTaskListActivity;
import com.ericharlow.DragNDrop.DragListener;
import com.ericharlow.DragNDrop.DropListener;

public class CalendarDayView extends FragmentActivity implements OnClickListener,
		CalendarCellDelegateInterface, CalendarQuickAddEventViewDelegate,
		ATLTabbarViewDelegete {

	// 2012-11-08 RYAN TAN: add code #1 add

	LayoutInflater mInflater;

	ViewGroup mLayout;
	ListView listEvent;
	ImageButton backArrowBtn;
	ImageButton forwardArrowBtn;
	TextView dateTime;
	Date currentDate = new Date();
	ATLCalendarListAdapter adaper;
	ATLCalCellList aCalCellList;

	ImageButton sortBtn;
	ImageButton alliesBtn;

	private RadioButton calBtn;
	private ViewGroup quickAddViewHolder;
	private CalendarQuickAddEventView quickAddView;

	private ATLTabbarView tabbar;

	
	public static ArrayList<String> calendarInActiveNameArray = new ArrayList<String>();
	public static ArrayList<ATLCalendarModel> calListModel = new ArrayList<ATLCalendarModel>();
	

	public boolean isFragmentShowing = false;

	private ATLSlideMenuFragment slideMenuFragment;
//	private CalendarDayViewFragment slideMenuFragment; TAN is going on menu 3.0

	private ATLAlertFragment_2 alertFragment;

	private ViewGroup alertHolderView;

	private ViewGroup selectCalendarHolderView;
	
	public ATLDragAndDropView calendarHolderView;
	
	public View calendarViewCover;
	
	private int topMenuBarHeight = 0;

	private ATLAddBarView addbar;

	// 2012-11-08 RYAN TAN: add code #1 end add

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mInflater = LayoutInflater.from(this);
		mLayout = (ViewGroup) mInflater.inflate(R.layout.calendar_day_view_2,
				null);
		//==========================================================
		// 2013-02-03 TAN: new slide out # START
		//==========================================================
		calendarHolderView = (ATLDragAndDropView)mLayout.findViewById(R.id.calendar_day_view_content);
		alertHolderView = (ViewGroup)mLayout.findViewById(R.id.calendar_day_view_alert_fragment);
//		alertHolderView.setX(100);
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
		//==========================================================
		// 2013-02-03 TAN: new slide out # END
		//==========================================================
		
		setContentView(mLayout);
		calListModel = ATLCalendarStore.loadCalendarList(this);
		calendarInActiveNameArray = ATLCalendarStore.loadInActiveCalendarNameList(this);
		initEventListView();

		backArrowBtn = (ImageButton) findViewById(R.id.prevDay);
		forwardArrowBtn = (ImageButton) findViewById(R.id.nextDay);
		dateTime = (TextView) findViewById(R.id.currentDay);

		tabbar = new ATLTabbarView(this);
		tabbar.delegate = this;
		
		addbar = new ATLAddBarView(this);
		calendarHolderView.addView(addbar);

		setListener();
		bindingData();
//		TAN is going on change menu 3.0
//		slideMenuFragment = new CalendarDayViewFragment();
		slideMenuFragment = new ATLSlideMenuFragment();
		getSupportFragmentManager().beginTransaction()
		.add(R.id.calendar_day_view_cal_selects_fragment, slideMenuFragment).commit();
		alertFragment = ATLAlertFragment_2.getInstance();
		getSupportFragmentManager().beginTransaction()
		.add(R.id.calendar_day_view_alert_fragment, alertFragment).commit();
		
		sortBtn = (ImageButton) findViewById(R.id.sortBtn);
		sortBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*
				int width = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, SlideOutHelper.SLIDE_OUT_X, getResources()
								.getDisplayMetrics());
				SlideOutHelper.prepare(CalendarDayView.this,
						R.id.calendar_day_view_content, width);
				startActivity(new Intent(CalendarDayView.this,
						CalendarDayViewSort.class));
				overridePendingTransition(0, 0);
				//2013-01-20 TAN: avoid tap multi-times to the button
				sortBtn.setClickable(false);
				alliesBtn.setClickable(false);
				*/
				// 2013-02-03 TAN: new slide out # START
				
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

		ImageButton monthBtn = (ImageButton) findViewById(R.id.monthBtn);
		// btnMonth.setVisibility(View.VISIBLE);
		// btnMonth.setBackgroundResource(R.drawable.cal_day__top_bar_btn_month_image);
		monthBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(),
						CalendarMonthView.class);
				intent.putExtra(ATLCalendarIntentKeys.KEY_CURRENT_DAY_VIEW,
						currentDate.getTime());
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
				startActivityForResult(intent,
						ATLCalendarIntentKeys.CALL_FROM_CALENDAR_DAY);
				overridePendingTransition(R.anim.in_from_bottom, R.anim.stand_by);
			}
		});

		ImageButton actionsBtn = (ImageButton) findViewById(R.id.actionsBtn);
		actionsBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(), Actions.class);
				startActivity(i);
			}
		});

		// ImageButton addBtn = (ImageButton) findViewById(R.id.addBtn);
		// // btnAdd.setVisibility(View.VISIBLE);
		// // btnAdd.setBackgroundResource(R.drawable.cal_day__top_bar__addbtn);
		// //
		// addBtn.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// CalendarEventSingleton.getInstance().setCalCellData(
		// new ATLCalCellData()); // TAN : create a new event
		// Intent i = new Intent(getBaseContext(), CalendarEditView.class);
		// startActivity(i);
		// }
		// });
		
		
		alliesBtn = (ImageButton) findViewById(R.id.alliesBtn);
		alliesBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				int width = (int) TypedValue.applyDimension(
//						TypedValue.COMPLEX_UNIT_DIP, SlideOutHelper.SLIDE_OUT_X, getResources()
//								.getDisplayMetrics());
//				SlideOutHelper.prepare(CalendarDayView.this,
//						R.id.calendar_day_view_content, width);
//				Intent intent = new Intent(getBaseContext(), Alerts.class);
//				intent.putExtra("rightSwipe", true);
//				startActivity(intent);
//				overridePendingTransition(0, 0);
//				//2013-01-20 TAN: avoid tap multi-times to the button
//				sortBtn.setClickable(false);
//				alliesBtn.setClickable(false);
				
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

		((RadioButton) findViewById(R.id.btnCal)).setVisibility(View.VISIBLE);
		calBtn = (RadioButton) findViewById(R.id.btnCal);
		calBtn.setChecked(true);
		calBtn.setTextColor(Color.BLACK);

		setMainMenuListener();
		// Handle add new event from settings 
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			int intentCode = extras.getInt(ATLCalendarIntentKeys.CALL_CALLENDARDAYVIEW_MODE);
			if(intentCode == ATLCalendarIntentKeys.CALL_CALLENDARDAYVIEW_MODE_ADD_EVENT){
				mLayout.postDelayed(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						ATLCalCellData cellData = new ATLCalCellData();
						CalendarEventSingleton.getInstance().setCalCellData(cellData);
						Intent i = new Intent(mLayout.getContext(), CalendarEditView.class);
						i.putExtra(CalendarCell.CELL_IS_BLANK, cellData.isBlank);
						startActivityForResult(i, ATLCalendarIntentKeys.CALL_FROM_CALENDAR_CELL);
					}
				}, 100);
			}
		}

	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case ATLCalendarIntentKeys.CALL_FROM_CALENDAR_CELL:
			if (resultCode == ATLCalendarIntentKeys.RESULT_FROM_MOVE_EVENT_VIEW) {

				long returnDateLong = data.getExtras().getLong(
						ATLCalendarIntentKeys.KEY_FROM_MOVE_EVENT_VIEW);
				Date tempDate = new Date(returnDateLong);
				CalendarEventSingleton.getInstance().getCalCellData().setCalCellDate(tempDate);
				CalendarEventSingleton.getInstance().getCalCellData().setCalCellStartDate(tempDate);
				CalendarEventSingleton.getInstance().getCalCellData().calCellPreferDateTime = tempDate;
				CalendarUtilities.updateCellData(CalendarEventSingleton.getInstance().getCalCellData(), this);
				aCalCellList.currentDateDidChanged(currentDate);
				adaper.notifyDataSetChanged();
			} else {
				aCalCellList.currentDateDidChanged(currentDate);
				bindingData();
				adaper.notifyDataSetChanged();
			}
			break;
		case ATLCalendarIntentKeys.CALL_FROM_CALENDAR_DAY:
			// currentStartTime
			if (resultCode == ATLCalendarIntentKeys.MONTH_VIEW_RETURN_KEY) {
				SimpleDateFormat dateFormatter = new SimpleDateFormat(
						"dd-MMM-yyyy");
				String returnDateStr = data.getExtras().getString(
						ATLCalendarIntentKeys.MONTH_VIEW_RETURN_DAY);
				Date tempDate = new Date();
				try {
					tempDate = dateFormatter.parse(returnDateStr);
					currentDate = tempDate;
					aCalCellList.currentDateDidChanged(currentDate);
					bindingData();
					adaper.notifyDataSetChanged();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;

		default:
			break;
		}

	}

	// Init View attributes
	private void initEventListView() {
		// TODO Auto-generated method stub
		// mTopBar = (ATLTopbar) findViewById(R.id.topBarMenu);
		// mTopBar.setType(ATLMainTabbarActivity.TAB_CALENDAR);
		// btnAdd = mTopBar.addButton;
		// =========================================================
		// 2012-12-08 TAN: START - Add quick add event view
		// ========================================================
		bottomMenu = (View) findViewById(R.id.bottomMenu);
		quickAddViewHolder = (ViewGroup) findViewById(R.id.calendar_day_view_content_layout);
		quickAddView = new CalendarQuickAddEventView(this);
		quickAddViewHolder.addView(quickAddView, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		quickAddView.delegate = this;
		quickAddView.setVisibility(View.GONE);
		// =========================================================
		// 2012-12-08 TAN: END - Add quick add event view
		// ========================================================
		listEvent = (ListView) findViewById(R.id.calendarList);
		aCalCellList = new ATLCalCellList();
		aCalCellList.calListDate = currentDate;
		adaper = new ATLCalendarListAdapter(aCalCellList, this);
		listEvent.setAdapter(adaper);
		listEvent.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				handleListViewPosition();
			}
		});
	}

	// 2012-12-11 TAN: START - Scroll day view ========================
	private void handleListViewPosition() {
		// TODO Auto-generated method stub
		boolean isToday = CalendarUtilities.isToday(currentDate);
		Date currentTime = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentTime);
		if (isToday) {
			if (aCalCellList.calEventsListArray.size() > 0) {

				int currentHourIndex = cal.get(Calendar.HOUR_OF_DAY);// 0 - 23
				for (Object temp : aCalCellList.calEventsListArray) {
					ATLCalCellData cellData = (ATLCalCellData) temp;
					if (cellData.getCalCellHour() > 0) {
						if (cellData.getCalCellHour() > currentHourIndex) {
							currentHourIndex = cellData.getCalCellHour();
						}
					}
				}

				if (currentHourIndex > 0) {
					listEvent.smoothScrollToPositionFromTop(currentHourIndex - 1, 0);
				}

			} else {
				int currentHourIndex = cal.get(Calendar.HOUR_OF_DAY);// 0 - 23
				if (currentHourIndex > 0) {
					listEvent.smoothScrollToPositionFromTop(currentHourIndex - 1, 0);
				}
			}
		} else {
			if (aCalCellList.calEventsListArray.size() > 0) {
				int currentHourIndex = cal.get(Calendar.HOUR_OF_DAY);// 0 - 23
				for (Object temp : aCalCellList.calEventsListArray) {
					ATLCalCellData cellData = (ATLCalCellData) temp;
					if (cellData.getCalCellHour() > 0) {
						currentHourIndex = cellData.getCalCellHour();
					}
				}

				if (currentHourIndex > 0) {
					listEvent.smoothScrollToPositionFromTop(currentHourIndex - 1, 0);
				}

			}

		}
	}

	// 2012-12-11 TAN: END - Scroll day view ==========================

	// To set data for UI
	private void bindingData() {
		// TODO Auto-generated method stub
		dateTime.setText(DateFormat
				.format(getResources().getString(
						R.string.calendar_day_view_day_format), currentDate));
	}

	// Register UI touch listener
	private void setListener() {
		// TODO Auto-generated method stub
		backArrowBtn.setOnClickListener(this);
		forwardArrowBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		// 2012-11-08 RYAN TAN: add code #3
		// TODO Auto-generated method stub
		if (v == backArrowBtn) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDate);
			Calendar calTemp = Calendar.getInstance();
			calTemp.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
					cal.get(Calendar.DAY_OF_MONTH) - 1, 0, 0, 0);
			currentDate = calTemp.getTime();
			aCalCellList.currentDateDidChanged(currentDate);
			bindingData();
			adaper.notifyDataSetChanged();
			handleListViewPosition();
		} else if (v == forwardArrowBtn) {

			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDate);
			Calendar calTemp = Calendar.getInstance();
			calTemp.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
					cal.get(Calendar.DAY_OF_MONTH) + 1, 0, 10, 0);
			currentDate = calTemp.getTime();
			aCalCellList.currentDateDidChanged(currentDate);
			bindingData();
			adaper.notifyDataSetChanged();
			handleListViewPosition();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		
		calendarViewCover.setVisibility(View.GONE);
		calendarHolderView.setX(0);
		
		//2013-01-27 Tan fix double tap to show side menu
		sortBtn.setClickable(true);
		alliesBtn.setClickable(true);
		//2013-01-27 End

		//[#107] : 2013-01-19 TAN: Avoid change cell order in OnResume() 
		aCalCellList.currentDateDidChanged(currentDate);
		bindingData();
		adaper.notifyDataSetChanged();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// =============== START Hide quick add view
		ATLAnimationUtils.hideKeyboard(this, quickAddView.eventLocation);
		// bottomMenu.setVisibility(View.VISIBLE);// 2013-01-04 TAN: fix for new
		// tabbar
//		tabbar.setVisibility(View.VISIBLE);
		addbar.setVisibility(View.VISIBLE);
		quickAddView.setVisibility(View.GONE);
		// ================ END
		
		isFragmentShowing = false;
		ATLDragAndDropView.isAtRight = false;
		ATLDragAndDropView.isAtLeft = false;
		
		
	}
	

	private void setMainMenuListener() {

		((RadioGroup) findViewById(R.id.main_btns_group))
				.setVisibility(View.VISIBLE);

		RadioGroup mainBtns = (RadioGroup) findViewById(R.id.main_btns_group);

		// holder.rdgCategory = (RadioGroup)row.findViewById(R.id.radiogroup);

		RadioGroup.OnCheckedChangeListener rdGrpCheckedListener = new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				// ViewFlipper vf = (ViewFlipper)findViewById(R.id.content_vf);
				// ImageView mainTitle =
				// (ImageView)findViewById(R.id.mainTitleLabel);
				// mainTitle.setVisibility(0);

				// TODO Auto-generated method stub
				// setCategoryinList(position, checkedId);
				switch (checkedId) {
				case R.id.btnCal:
					// Intent intent = new Intent(getBaseContext(),
					// Calendar.class);
					// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
					// startActivity(intent);

					break;
				case R.id.btnNotes:
					Intent intentNotes = new Intent(getBaseContext(),
							ATLNoteListActivity.class);
					intentNotes.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

					intentNotes.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
					startActivity(intentNotes);

					break;
				case R.id.btnSettings:
					Intent intent = new Intent(getBaseContext(), Settings.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

					intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
					startActivity(intent);

					break;
				case R.id.btnToday:
					Intent intentToday = new Intent(getBaseContext(),

					Contacts.class);
					intentToday.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					intentToday.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
					startActivity(intentToday);

					break;
				case R.id.btnToDo:
					Intent intentTasks = new Intent(getBaseContext(),
							ATLTaskListActivity.class);
					intentTasks.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

					intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
					// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
					startActivity(intentTasks);

					break;
				default:

					break;
				}
			}
		};

		mainBtns.setOnCheckedChangeListener(rdGrpCheckedListener);

		// 2012-11-08 RYAN TAN: set selected state
		((RadioButton) findViewById(R.id.btnCal)).setVisibility(View.VISIBLE);
		RadioButton calBtn = (RadioButton) findViewById(R.id.btnCal);
		calBtn.setChecked(true);
		calBtn.setTextColor(Color.BLACK);

	}

	// =======================================================================
	// CalendarCellDelegateInterface - START implementation
	// =======================================================================
	int lastShowOffHourIndex = -1;

	@Override
	public void didTapToShowOffHours(int index) {
		// TODO Auto-generated method stub
		// Toast.makeText(this, "_" + index, Toast.LENGTH_SHORT).show();
		Log.v("CalendarDayView", "index: " + index);

		if (lastShowOffHourIndex != -1) {
			// Edit old data
			ATLCalCellData cellOldData = (ATLCalCellData) listEvent
					.getAdapter().getItem(lastShowOffHourIndex);
			// cellOldData.isShowOffHour = false;
			// Edit old view
			int firstVisible = listEvent.getFirstVisiblePosition();
			int lastVisible = listEvent.getLastVisiblePosition();
			Log.v("CalendarDayView", "lastShowOffHourIndex: "
					+ lastShowOffHourIndex);
			Log.v("CalendarDayView", "firstVisible: " + firstVisible);
			if (firstVisible > lastShowOffHourIndex
					|| lastShowOffHourIndex > lastVisible) {
				// Do nothing
			} else if (firstVisible <= lastShowOffHourIndex
					&& lastShowOffHourIndex <= lastVisible) {
				if (lastShowOffHourIndex - firstVisible != index
						&& lastShowOffHourIndex != index
						&& cellOldData.isShowOffHour) {
					CalendarCell cellOld = (CalendarCell) listEvent
							.getChildAt(lastShowOffHourIndex - firstVisible);
					cellOld.dismissOffHours();
				}
			}
			lastShowOffHourIndex = index;
		} else {
			// Do nothing
			lastShowOffHourIndex = index;
		}
	}

	@Override
	public void requestAddNewView(int hour, int minute) {
		// TODO Auto-generated method stub
		aCalCellList.createBlankCellWithHour(hour, minute);
		adaper.notifyDataSetChanged();
	}

	private int lastShowMoveEvent = -1;
	private View bottomMenu;

	@Override
	public void didTouchToMoveEvent(int index) {
		// TODO Auto-generated method stub
		if (lastShowMoveEvent != -1) {
			// Edit old data
			ATLCalCellData cellOldData = (ATLCalCellData) listEvent
					.getAdapter().getItem(lastShowMoveEvent);
			// cellOldData.isShowOffHour = false;
			// Edit old view
			int firstVisible = listEvent.getFirstVisiblePosition();
			int lastVisible = listEvent.getLastVisiblePosition();
			Log.v("CalendarDayView", "lastShowMoveEvent: " + lastShowMoveEvent);
			Log.v("CalendarDayView", "firstVisible: " + firstVisible);
			if (firstVisible > lastShowMoveEvent
					|| lastShowMoveEvent > lastVisible) {
				// Do nothing
			} else if (firstVisible <= lastShowMoveEvent
					&& lastShowMoveEvent <= lastVisible) {
				if (lastShowOffHourIndex - firstVisible != index
						&& lastShowMoveEvent != index
						&& cellOldData.isShowMoveEvent) {
					CalendarCell cellOld = (CalendarCell) listEvent
							.getChildAt(lastShowMoveEvent - firstVisible);
					cellOld.dismissMoveEvents();
				}
			}
			lastShowMoveEvent = index;
		} else {
			// Do nothing
			lastShowMoveEvent = index;
		}
	}

	@Override
	public void didDeleteEventAtIndex(int index) {
		// TODO Auto-generated method stub
		ATLCalCellData cellData = (ATLCalCellData) listEvent.getAdapter()
				.getItem(index);
		CalendarUtilities.deleteCellData(cellData, this);
		// Reload data for list
		//2013-01-20 TAN Fixed [#133] : Avoid scroll to top
		aCalCellList.currentDateDidChanged(currentDate);
		adaper.notifyDataSetChanged();
	}

	private View getCurrentEventView(int index) {

		// Edit old view
		int firstVisible = listEvent.getFirstVisiblePosition();
		CalendarCell cellOld = (CalendarCell) listEvent.getChildAt(index
				- firstVisible);
		return cellOld;
	}

	@Override
	public void didDoubleTapToQuickAddEventAtIndex(int index) {
		// TODO Auto-generated method stub
		// Toast.makeText(this, "isDoubleTap at index: " + index,
		// Toast.LENGTH_SHORT).show();
		//bottomMenu.setVisibility(View.GONE);// Because it's always above
											// keyboard
//		tabbar.setVisibility(View.GONE);
		addbar.setVisibility(View.GONE);
		ATLAnimationUtils.showKeyboard(this);
		View eventView = getCurrentEventView(index);

		quickAddView.contentHolderView.setAnimation(ATLAnimationUtils
				.quickAddEventShow(eventView.getY()));
		quickAddView.setVisibility(View.VISIBLE);
		ATLCalCellData cellData = (ATLCalCellData) listEvent.getAdapter()
				.getItem(index);
		quickAddView.setCellData(cellData);
		quickAddView.eventLocation.setImeOptions(EditorInfo.IME_ACTION_DONE);
		quickAddView.eventLocation.requestFocus();

	}

	// =======================================================================
	// CalendarCellDelegateInterface - END
	// =======================================================================

	// =======================================================================
	// CalendarQuickAddEventViewDelegate - START implementation
	// =======================================================================
	@Override
	public void didTapDoneButton() {
		// TODO Auto-generated method stub
		// Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
		ATLAnimationUtils.hideKeyboard(this, quickAddView.eventLocation);
		//		bottomMenu.setVisibility(View.VISIBLE);
//		tabbar.setVisibility(View.VISIBLE);
		addbar.setVisibility(View.VISIBLE);
		quickAddView.setVisibility(View.GONE);

		// Reload data for list
		//2013-01-20 TAN Fixed [#133] : Avoid scroll to top
		aCalCellList.currentDateDidChanged(currentDate);
		adaper.notifyDataSetChanged();
	}

	@Override
	public void didTapMoreButton(ATLCalCellData cellData) {
		CalendarEventSingleton.getInstance().setCalCellData(cellData);
		Intent i = new Intent(this, CalendarEditView.class);
		i.putExtra(CalendarCell.CELL_IS_BLANK, cellData.isBlank);
		// i.putExtra("celldata", this.cellData);
		startActivityForResult(i, ATLCalendarIntentKeys.CALL_FROM_CALENDAR_CELL);
	}

	// =======================================================================
	// CalendarQuickAddEventViewDelegate - END
	// =======================================================================

	// ===============================================================================
	// 2013-01-04 TAN: ATLTabBarViewDelegete Start Implement
	// ===============================================================================

	@Override
	public void didTabToTabIndex(int index) {
		// TODO Auto-generated method stub
		switch (index) {
		case ATLTabbarViewDelegete.TabbarIndex.tabbar_icon_contacts:
			Intent intentContact = new Intent(this,ATLContactListActivity.class);
			intentContact.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			intentContact.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
			startActivity(intentContact);
			break;
		case ATLTabbarViewDelegete.TabbarIndex.tabbar_icon_calendar:
			break;
		case ATLTabbarViewDelegete.TabbarIndex.tabbar_icon_home:
//			2013-01-09 TAN : Remove Notes
//			Intent intentNotes = new Intent(getBaseContext(), ATLNoteListActivity.class);
//			intentNotes.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//
//			intentNotes.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
//			startActivity(intentNotes);
			ATLCalCellData cellData = new ATLCalCellData();
			CalendarEventSingleton.getInstance().setCalCellData(cellData);
			Intent i = new Intent(this, CalendarEditView.class);
			i.putExtra(CalendarCell.CELL_IS_BLANK, cellData.isBlank);
			// i.putExtra("celldata", this.cellData);
			startActivityForResult(i, ATLCalendarIntentKeys.CALL_FROM_CALENDAR_CELL);
			overridePendingTransition(R.anim.in_from_bottom, R.anim.stand_by);
			
			break;
		case ATLTabbarViewDelegete.TabbarIndex.tabbar_icon_tasks:
			Intent intentTasks = new Intent(getBaseContext(), ATLTaskListActivity.class);
			intentTasks.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

			intentTasks.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
			startActivity(intentTasks);
			break;
		case ATLTabbarViewDelegete.TabbarIndex.tabbar_icon_settings:
			Intent intentSettings = new Intent(getBaseContext(), Settings.class);
			intentSettings.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

			intentSettings.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
			startActivity(intentSettings);
			break;
		}
	}

	// ===============================================================================
	// 2013-01-04 TAN: ATLTabBarViewDelegete End Implement
	// ===============================================================================
	
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
}