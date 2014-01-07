//  ==================================================================================================================
//  OffHourTimesPickerView.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-12-07 TAN:     Create class
//  ==================================================================================================================

package atlasapp.section_calendar;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import atlasapp.section_appentry.R;

interface OffHourTimesPickerViewDelegate {
	void didChooseStartMinute(int minute);
}

public class OffHourTimesPickerView extends RelativeLayout implements
		View.OnClickListener {
	Activity mActivity;
	ImageView calendarImageView; // Calendar color view
	TextView hourLabel;
	TextView amPmLabel;
	TextView minuteLabel;
	TextView at_00Label;
	TextView at_15Label;
	TextView at_30Label;
	TextView at_45Label;
	ATLCalCellData cellData;
	OffHourTimesPickerViewDelegate delegate;
	public OffHourTimesPickerView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public OffHourTimesPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public OffHourTimesPickerView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context) {
		// Init CallendarCell view
		LayoutInflater.from(context).inflate(
				R.layout.slide_out_off_hour_times_picker, this, true);
		mActivity = (Activity) context;
		calendarImageView = (ImageView) findViewById(R.id.imageCalendarColor);
		hourLabel = (TextView) findViewById(R.id.textEventTime);
		amPmLabel = (TextView) findViewById(R.id.textAMPM);
		minuteLabel = (TextView) findViewById(R.id.textMinute);

		at_00Label = (TextView) findViewById(R.id.off_hour_at_00minute);
		at_15Label = (TextView) findViewById(R.id.off_hour_at_15minute);
		at_30Label = (TextView) findViewById(R.id.off_hour_at_30minute);
		at_45Label = (TextView) findViewById(R.id.off_hour_at_45minute);
		
		at_00Label.setOnClickListener(this);
		at_15Label.setOnClickListener(this);
		at_30Label.setOnClickListener(this);
		at_45Label.setOnClickListener(this);

	}

	public void setCellData(ATLCalCellData cellData) {
		// TODO Auto-generated method stub
		// calendarImageView.setVisibility(View.GONE);
		this.cellData = cellData;

		Calendar calStart = Calendar.getInstance();
		calStart.setTimeInMillis(cellData.getCalCellStartDate().getTime());

		amPmLabel.setText(determineAmPm(calStart.get(Calendar.HOUR_OF_DAY)));
		amPmLabel.setTextColor(Color.BLACK);
		minuteLabel.setText(calStart.get(Calendar.MINUTE) + "");
		minuteLabel.setTextColor(Color.BLACK);

		hourLabel
				.setText(determineClockHour(calStart.get(Calendar.HOUR_OF_DAY))
						+ "");
		if (cellData.getCalCellMinute() == 0) {
			minuteLabel.setText("00");
		}
		calendarImageView
				.setBackgroundColor(cellData.getCalCellCalendarColor());
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
		int minute = 0;
		switch(v.getId()){
		case R.id.off_hour_at_00minute:
			break;
		case R.id.off_hour_at_15minute:
			minute = 15;
			break;
		case R.id.off_hour_at_30minute:
			minute = 30;
			break;
		case R.id.off_hour_at_45minute:
			minute = 45;
			break;
		}
		delegate.didChooseStartMinute(minute);
	}

}