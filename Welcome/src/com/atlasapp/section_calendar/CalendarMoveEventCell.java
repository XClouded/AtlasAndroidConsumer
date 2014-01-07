//  ==================================================================================================================
//  CalendarMoveEventCell.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-12-14 TAN:     Create class
//  ==================================================================================================================

package com.atlasapp.section_calendar;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atlasapp.common.ATLAnimationUtils;
import com.atlasapp.section_appentry.R;

interface CalendarMoveEventCellInterface {

	void didTapToShowOffHours(int index);
	void didTapToRightViewIndex(int index);
	void requestAddNewView(int hour, int minute);
}

public class CalendarMoveEventCell extends RelativeLayout implements
		View.OnClickListener, OffHourTimesPickerViewDelegate {

	Activity mActivity;
	public ImageView calendarImageView; // Calendar color view
	public TextView hourLabel;
	public TextView amPmLabel;
	public TextView primaryLabel;

	public ATLCalCellData cellData;
	private View cellView;

	protected CalendarMoveEventCellInterface delegate;
	private View leftView;
	public int index = 0;
	private OffHourTimesPickerView offHours;
	private boolean isShowOffHour = false;
	private ViewGroup rightView;

	public CalendarMoveEventCell(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public CalendarMoveEventCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public CalendarMoveEventCell(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context) {
		// Init CallendarCell view
		LayoutInflater.from(context).inflate(R.layout.calendar_alts_time_cell,
				this, true);
		mActivity = (Activity) context;
		calendarImageView = (ImageView) findViewById(R.id.imageCalendarColor);
		hourLabel = (TextView) findViewById(R.id.textEventTime);
		amPmLabel = (TextView) findViewById(R.id.textAMPM);
		primaryLabel = (TextView) findViewById(R.id.textEventTitle);
		cellView = (View) findViewById(R.id.calCell);
		cellView.setBackgroundColor(Color.WHITE);
		cellView.setVisibility(View.VISIBLE);

		leftView = (ViewGroup) findViewById(R.id.cal_cell_left_view);
		leftView.setOnClickListener(this);

		rightView = (ViewGroup) findViewById(R.id.cal_cell_right_view);
		rightView.setOnClickListener(this);

		offHours = new OffHourTimesPickerView(mActivity);
		addView(offHours, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		offHours.delegate = this;
		offHours.setVisibility(View.GONE);
		offHours.setOnClickListener(this);

		delegate = (CalendarMoveEventCellInterface) mActivity;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v == leftView) {
			// Handle slide out move events
//			Toast.makeText(mActivity, "leftView", Toast.LENGTH_SHORT).show();
			showOffHours();
			delegate.didTapToShowOffHours(index);
		} else if (v == offHours) {
//			Toast.makeText(mActivity, "offHours", Toast.LENGTH_SHORT).show();
			dismissOffHours();
		} else if (v == rightView) {
			delegate.didTapToRightViewIndex(index);
		}
	}

	public void dismissOffHours() {
		// TODO Auto-generated method stub
		cellView.setAnimation(ATLAnimationUtils.inFromRightAnimation());
		offHours.setAnimation(ATLAnimationUtils.outToLeftAnimation());
		cellView.setVisibility(View.VISIBLE);
		offHours.setVisibility(View.GONE);
		isShowOffHour = false;
		cellData.isShowOffHour = false;
	}

	public void showOffHours() {
		// TODO Auto-generated method stub
		cellView.setAnimation(ATLAnimationUtils.outToRightAnimation());
		offHours.setAnimation(ATLAnimationUtils.inFromLeftAnimation());
		cellView.setVisibility(View.GONE);
		offHours.setVisibility(View.VISIBLE);
		isShowOffHour = true;
		cellData.isShowOffHour = true;

	}

	public void setCellData(ATLCalCellData cellData) {
		// TODO Auto-generated method stub
		// calendarImageView.setVisibility(View.GONE);
		this.cellData = cellData;

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(cellData.getCalCellStartDate().getTime());

		amPmLabel.setText(determineAmPm(calendar.get(Calendar.HOUR_OF_DAY)));
		if (cellData.getCalCellMinute() > 0) {
			hourLabel.setText(determineClockHour(calendar
					.get(Calendar.HOUR_OF_DAY))
					+ ":"
					+ calendar.get(Calendar.MINUTE));
		} else {
			hourLabel.setText(determineClockHour(calendar
					.get(Calendar.HOUR_OF_DAY)) + ":" + "00");
		}
		primaryLabel.setText(cellData.getCalCellTitle());
		calendarImageView
				.setBackgroundColor(cellData.getCalCellCalendarColor());
		cellView.setBackgroundColor(cellData.calCellBackgroundColor);
		// Dismiss OffHour
		cellView.setVisibility(View.VISIBLE);

		offHours.setVisibility(View.GONE);
		cellData.isShowOffHour = false;
		isShowOffHour = false;
		offHours.setCellData(cellData);// Set data for offHours view
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
	public void didChooseStartMinute(int minute) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		// Toast.makeText(mActivity, "minute " + minute, Toast.LENGTH_SHORT)
		// .show();
		Calendar calStart = Calendar.getInstance();
		calStart.setTimeInMillis(cellData.getCalCellStartDate().getTime());
		delegate.requestAddNewView(calStart.get(Calendar.HOUR_OF_DAY), minute);
	}

}