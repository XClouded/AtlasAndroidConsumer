//  ==================================================================================================================
//  CalendarMoveEvent.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-12-08 TAN:     Create class
//  ==================================================================================================================

package com.atlasapp.section_calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.atlasapp.common.CalendarUtilities;
import com.atlasapp.section_appentry.R;

public class CalendarMoveEvent extends Activity implements OnClickListener,
		CalendarMoveEventCellInterface {

	ATLCalendarListMoveEventAdapter adapter;
	ATLCalCellList aCalCellList;
	ListView listEvent;
	final int PRE = 0;
	int FLAG = 0;

	TextView preferDay;
	TextView preferHour;

	ImageButton backArrowBtn;
	ImageButton nextArrowBtn;
	TextView dateLable;

	Date currentPreferTime = new Date();
	private ATLCalCellData currentCellData = new ATLCalCellData();
	private ImageButton monthBtn;
	private ImageButton cancelBtn;
	private ImageButton saveBtn;
	private int lastShowOffHourIndex = -1;
	private int currentIndex = 0;

	static final int CELL_BACKGROUND_ORANGE = Color.rgb(255, 211, 170);
	static final int CELL_BACKGROUND_WHITE = Color.WHITE;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_move_event);

		getIntendData();
		initAttributes();
		setListener();
		bindingData();
		changeBgColorOfCell(currentPreferTime);
		
		listEvent.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				listEvent.smoothScrollToPositionFromTop(currentIndex, 0);
			}
		});
		
	}

	private void getIntendData() {
		// TODO Auto-generated method stub
		currentCellData.setCalCellDate(CalendarEventSingleton.getInstance()
				.getCalCellData().getCalCellDate());
		
		currentCellData.setCalCellStartDate(CalendarEventSingleton
				.getInstance().getCalCellData().getCalCellStartDate());
		if(getIntent().getExtras()!= null && 
				getIntent().getExtras().getBoolean(CalendarCell.MOVE_EVENT_FROM_CELL)){
			currentPreferTime = CalendarEventSingleton.getInstance()
					.getCalCellData().getCalCellStartDate();
		}
		else{
			currentPreferTime = CalendarEventSingleton.getInstance()
				.getCalCellData().calCellPreferDateTime != null? CalendarEventSingleton.getInstance()
						.getCalCellData().calCellPreferDateTime: CalendarEventSingleton.getInstance()
						.getCalCellData().getCalCellStartDate();
		}
	}

	private void initAttributes() {
		// TODO Auto-generated method stub
		// Buttons
		monthBtn = (ImageButton) findViewById(R.id.monthBtn);
		cancelBtn = (ImageButton) findViewById(R.id.cal_move_event_btn_cancel);
		saveBtn = (ImageButton) findViewById(R.id.cal_move_event_btn_save);
		backArrowBtn = (ImageButton) findViewById(R.id.prevDay);
		nextArrowBtn = (ImageButton) findViewById(R.id.nextDay);

		// TextViews
		dateLable = (TextView) findViewById(R.id.currentDay);
		preferDay = (TextView) findViewById(R.id.preferDay);
		preferHour = (TextView) findViewById(R.id.preferHour);

		// ListViews
		listEvent = (ListView) findViewById(R.id.calendarList);

	}

	private void setListener() {
		// TODO Auto-generated method stub
		monthBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		saveBtn.setOnClickListener(this);
		backArrowBtn.setOnClickListener(this);
		nextArrowBtn.setOnClickListener(this);
	}

	private void bindingData() {
		// TODO Auto-generated method stub
		// Set day text view
		dateLable.setText(DateFormat
				.format(getResources().getString(
						R.string.calendar_day_view_day_format),
						currentPreferTime));

		// Set DayList view
		aCalCellList = new ATLCalCellList();
		aCalCellList.setCalListDate(currentPreferTime);
		adapter = new ATLCalendarListMoveEventAdapter(aCalCellList, this);
		listEvent.setAdapter(adapter);

		// Set prefer date time
		setPreferDateTime();

	}

	private void setPreferDateTime() {
		// TODO Auto-generated method stub
		String hourFormat = getResources().getString(
				R.string.calendar_edit_event_when_hour_format);
		String dayFormat = getResources().getString(
				R.string.calendar_edit_event_when_day_format);
		preferDay.setText(DateFormat.format(dayFormat, currentPreferTime));
		preferHour.setText(((String)DateFormat.format(hourFormat, currentPreferTime)).toUpperCase());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == backArrowBtn) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentPreferTime);
			Calendar calTemp = Calendar.getInstance();
			calTemp.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
					cal.get(Calendar.DAY_OF_MONTH) - 1, 0, 0, 0);
			currentPreferTime = calTemp.getTime();
			aCalCellList.currentDateDidChanged(currentPreferTime);
			dateDidChanged();
			handleListViewPosition();

		} else if (v == nextArrowBtn) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentPreferTime);
			Calendar calTemp = Calendar.getInstance();
			calTemp.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
					cal.get(Calendar.DAY_OF_MONTH) + 1, 0, 0, 0);
			currentPreferTime = calTemp.getTime();
			aCalCellList.currentDateDidChanged(currentPreferTime);
			dateDidChanged();
			handleListViewPosition();
		} else if (v == monthBtn) {
			Intent intent = new Intent(getBaseContext(),
					CalendarMonthView.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			intent.putExtra(ATLCalendarIntentKeys.KEY_CURRENT_DAY_VIEW,
					currentPreferTime.getTime());
			// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
			startActivityForResult(intent,
					ATLCalendarIntentKeys.CALL_FROM_CALENDAR_MOVE_EVENT_VIEW);
			overridePendingTransition(R.anim.in_from_bottom, R.anim.stand_by);
		} else if (v == cancelBtn) {
			finish();
			overridePendingTransition(R.anim.stand_by, R.anim.out_to_bottom);
		} else if (v == saveBtn) {

			Intent intent = new Intent();
			intent.putExtra(ATLCalendarIntentKeys.KEY_FROM_MOVE_EVENT_VIEW,
					currentPreferTime.getTime());
			setResult(ATLCalendarIntentKeys.RESULT_FROM_MOVE_EVENT_VIEW, intent);
			finish();
			overridePendingTransition(R.anim.stand_by, R.anim.out_to_bottom);
		}

	}

	// 2012-12-11 TAN: START - Scroll day view ========================
	private void handleListViewPosition() {
		// TODO Auto-generated method stub
		boolean isToday = CalendarUtilities.isToday(currentPreferTime);
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
//					listEvent.setSelection(currentHourIndex -1);
				}

			}

		}
	}

	// 2012-12-11 TAN: END - Scroll day view ==========================

	private void dateDidChanged() {
		// TODO Auto-generated method stub
		initStartTime();
		dateLable.setText(DateFormat
				.format(getResources().getString(
						R.string.calendar_day_view_day_format),
						currentPreferTime));
		adapter.notifyDataSetChanged();
	}

	private void initStartTime() {
		// TODO Auto-generated method stub
		String aCellCalString = (String) DateFormat.format(
				getResources().getString(
						R.string.calendar_move_event_time_format_compare_day),
				CalendarEventSingleton.getInstance().getCalCellData()
						.getCalCellStartDate());
		// Log.v("CalendarMoveEvent", "initStart0: " + aCellCalString);
		String aCurrentTimeString = (String) DateFormat.format(
				getResources().getString(
						R.string.calendar_move_event_time_format_compare_day),
				currentPreferTime);
		// Log.v("CalendarMoveEvent", "initStart1: " + aCurrentTimeString);
		if (aCurrentTimeString.equals(aCellCalString)) {
			changeBgColorOfCell(CalendarEventSingleton.getInstance()
					.getCalCellData().getCalCellStartDate());
		}
	}

	private void changeBgColorOfCell(Date aDate) {
		// TODO Auto-generated method stub
		Calendar aDateCal = Calendar.getInstance();
		aDateCal.setTime(aDate);
		aDateCal.set(aDateCal.get(Calendar.YEAR), aDateCal.get(Calendar.MONTH),
				aDateCal.get(Calendar.DAY_OF_MONTH),
				aDateCal.get(Calendar.HOUR_OF_DAY),
				aDateCal.get(Calendar.MINUTE), 0);

		String aDateCalString = (String) DateFormat.format(getResources()
				.getString(R.string.calendar_edit_atl_time_format), aDateCal
				.getTime());

		Calendar aCellCal = Calendar.getInstance();

		int listSize = adapter.getCount();
		for (int i = 0; i < listSize; i++) {
			ATLCalCellData cellData = (ATLCalCellData) adapter.getItem(i);
			aCellCal.setTime(cellData.getCalCellDate());
			aCellCal.set(aCellCal.get(Calendar.YEAR),
					aCellCal.get(Calendar.MONTH),
					aCellCal.get(Calendar.DAY_OF_MONTH),
					aCellCal.get(Calendar.HOUR_OF_DAY),
					aCellCal.get(Calendar.MINUTE), 0);
			String aCellCalString = (String) DateFormat.format(getResources()
					.getString(R.string.calendar_edit_atl_time_format),
					aCellCal.getTime());
			if (aDateCalString.equals(aCellCalString)) {
				cellData.calCellBackgroundColor = CELL_BACKGROUND_ORANGE;
				currentIndex = i;
			} else {
				// RESET ALL OTHER CELL BACKGROUND
				cellData.calCellBackgroundColor = CELL_BACKGROUND_WHITE;
			}

		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {

		case ATLCalendarIntentKeys.CALL_FROM_CALENDAR_MOVE_EVENT_VIEW:
			// currentPreferTime
			if (resultCode == ATLCalendarIntentKeys.MONTH_VIEW_RETURN_KEY) {
				SimpleDateFormat dateFormatter = new SimpleDateFormat(
						"dd-MMM-yyyy");
				String returnDateStr = data.getExtras().getString(
						ATLCalendarIntentKeys.MONTH_VIEW_RETURN_DAY);
				Date tempDate = new Date();
				try {
					tempDate = dateFormatter.parse(returnDateStr);
					currentPreferTime = tempDate;
					aCalCellList.currentDateDidChanged(currentPreferTime);
					// dateDidChanged();
					dateLable.setText(DateFormat.format(getResources()
							.getString(R.string.calendar_day_view_day_format),
							currentPreferTime));
					adapter.notifyDataSetChanged();
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
					CalendarMoveEventCell cellOld = (CalendarMoveEventCell) listEvent
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
		adapter.notifyDataSetChanged();
	}

	@Override
	public void didTapToRightViewIndex(int index) {
		// TODO Auto-generated method stub
		aCalCellList.load(getBaseContext());
//      2013-01-24 TAN: fix when tap to cell have events and tap other cell
//		ATLCalCellData cellCurrentData = (ATLCalCellData) listEvent
//				.getAdapter().getItem(currentIndex);
//		cellCurrentData.cleanToBlank();
		ATLCalCellData cellNewData = (ATLCalCellData) listEvent.getAdapter()
				.getItem(index);
		cellNewData.copyEventOff(CalendarEventSingleton.getInstance()
				.getCalCellData());


		changeBgColorOfCell(cellNewData.getCalCellStartDate());
		currentPreferTime = cellNewData.calCellPreferDateTime;

		setPreferDateTime();

		adapter.notifyDataSetChanged();
	}
}