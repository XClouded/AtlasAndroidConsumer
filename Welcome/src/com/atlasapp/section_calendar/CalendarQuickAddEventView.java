//  ==================================================================================================================
//  CalendarQuickAddEventView.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================

package com.atlasapp.section_calendar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.test.MoreAsserts;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atlasapp.common.ATLActionEditText;
import com.atlasapp.common.CalendarUtilities;
import com.atlasapp.model.ATLCalendarModel;
import com.atlasapp.section_appentry.R;

interface CalendarQuickAddEventViewDelegate {
	void didTapDoneButton();

	void didTapMoreButton(ATLCalCellData cellData);
}

public class CalendarQuickAddEventView extends RelativeLayout implements
		View.OnClickListener, OnWheelChangedListener {
	Activity mActivity;
	ImageView calendarImageView; // Calendar color view
	TextView hourLabel;
	TextView amPmLabel;
	ATLCalCellData cellData;
	ImageButton moreBtn;
	ImageButton alisaBtn;
	CalendarQuickAddEventViewDelegate delegate;
	public ATLActionEditText eventLocation;
	public View contentHolderView;
	private WheelView calList;
	private int calColor;
	private int calId = 0;
	private ArrayList<ATLCalendarModel> calListModel;

	public CalendarQuickAddEventView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public CalendarQuickAddEventView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public CalendarQuickAddEventView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context) {
		// Init CallendarCell view
		LayoutInflater.from(context).inflate(R.layout.calendar_quick_add_event,
				this, true);
		mActivity = (Activity) context;
		contentHolderView = (View) findViewById(R.id.cal_quick_add_event_content_view);
		calendarImageView = (ImageView) findViewById(R.id.imageCalendarColor);
		eventLocation = (ATLActionEditText) findViewById(R.id.calendar_quick_add_edit_text);
		eventLocation
				.setOnEditorActionListener(new EditText.OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						// TODO Auto-generated method stub
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							save();
							updateDatabase();
							delegate.didTapDoneButton();
							return true;
						}
						return false;
					}

				});
		amPmLabel = (TextView) findViewById(R.id.textAMPM);
		hourLabel = (TextView) findViewById(R.id.textEventTime);
		moreBtn = (ImageButton) findViewById(R.id.cal_quick_add_bottom_bar_btn_more2x);
		moreBtn.setOnClickListener(this);
		alisaBtn = (ImageButton) findViewById(R.id.cal_quick_add_bottom_bar_btn_allies);
		alisaBtn.setOnClickListener(this);

		String[] calNames = ATLCalendarManager.getCalendarName(mActivity);
		calList = (WheelView) findViewById(R.id.cal_quick_add_bottom_bar_calendars);
		// calList.setViewAdapter(new ArrayWheelAdapter<String>(mActivity,
		// calNames));
		calListModel = ATLCalendarManager.getCalendars(mActivity);
		calList.setViewAdapter(new ATLCalendarScrollListAdapter(calListModel, mActivity));
		calList.addChangingListener(this);
	}

	private void save() {
		// TODO Auto-generated method stub
		this.cellData.setCalCellCalendarColor(calColor);
		this.cellData.setCalCellTitle(eventLocation.getText().toString());
		this.cellData.calendarId = calId;
		this.cellData.setCalCellCalendarColor(calColor);
	}

	private void updateDatabase() {
		if (this.cellData.isBlank) {
			CalendarUtilities.addCellData(this.cellData, mActivity);
		} else {
			CalendarUtilities.updateCellData(cellData, mActivity);
		}
	}

	public void setCellData(ATLCalCellData cellData) {
		// TODO Auto-generated method stub
		// calendarImageView.setVisibility(View.GONE);
		this.cellData = cellData;

		Calendar calStart = Calendar.getInstance();
		calStart.setTimeInMillis(cellData.getCalCellStartDate().getTime());

		amPmLabel.setText(determineAmPm(calStart.get(Calendar.HOUR_OF_DAY)));
		amPmLabel.setTextColor(Color.GRAY);
		if (cellData.getCalCellMinute() > 0) {
			hourLabel.setText(determineClockHour(calStart
					.get(Calendar.HOUR_OF_DAY)) + "");
			amPmLabel.setText(calStart.get(Calendar.MINUTE) + "");
			amPmLabel.setTextColor(Color.BLACK);
		} else {
			hourLabel.setText(determineClockHour(calStart
					.get(Calendar.HOUR_OF_DAY)) + "");
		}

		calendarImageView
				.setBackgroundColor(cellData.getCalCellCalendarColor());
		eventLocation.setText(cellData.getCalCellTitle());
		int index = 0;
		for (ATLCalendarModel calModel : calListModel) {
			if (cellData.calendarId == -1) {// Default value
				calList.setCurrentItem(0,true);
				calendarImageView.setBackgroundColor(calModel.color);
				break;
			} else if (cellData.calendarId == calModel.id) {
				calList.setCurrentItem(index, true);
				break;
			}
			index++;
		}

	}

	private int determineClockHour(int hour) {
		if (hour == 0) {
			return 12;
		}
		if (hour > 12) {
			return hour - 12;
		}
		return hour;
	}

	private String determineAmPm(int hour) {
		return hour < 12 ? "AM" : "PM";
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == moreBtn) {
			save();
			delegate.didTapMoreButton(this.cellData);
		} else if (v == alisaBtn) {
			Toast.makeText(mActivity, "Comming soon...", Toast.LENGTH_SHORT)
					.show();
		}
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		calColor = calListModel.get(newValue).color;
		calId = calListModel.get(newValue).id;
		calendarImageView.setBackgroundColor(calColor);
	}

}