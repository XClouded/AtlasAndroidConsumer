//  ==================================================================================================================
//  ATLAlertEventRequestCell.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================

package com.atlasapp.section_alerts;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atlasapp.common.ATLAnimationUtils;
import com.atlasapp.common.ATLColor;
import com.atlasapp.common.ATLConstants.EventResponseType;
import com.atlasapp.section_appentry.R;
import com.atlasapp.section_calendar.ATLCalCellData;

interface ATLAlertEventRequestCellDelegate {

	void didTapToShowAcceptViewAtIndex(int index);

	void didAcceptAtIndex(int index);

}

public class ATLAlertEventRequestCell extends RelativeLayout implements
		View.OnClickListener {

	Activity mActivity;
	public ImageView calendarImageView; // Calendar color view
	public ImageView avatarImageView;
	public TextView hourLabel;
	public TextView amPmLabel;
	public TextView primaryLabel;
	
	public ATLCalCellData cellData;
	private View cellView;
	public View confirmationView;
	public ATLAlertEventRequestCellDelegate delegate = null;
	public int index = 0;
	private boolean isShowConfirm = false;
	private ImageButton confirmBtn;

	public ATLAlertEventRequestCell(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public ATLAlertEventRequestCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public ATLAlertEventRequestCell(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context) {
		// Init CallendarCell view

		LayoutInflater.from(context).inflate(R.layout.alert_event_request_cell,
				this, true);
		mActivity = (Activity) context;
		calendarImageView = (ImageView) findViewById(R.id.imageCalendarColor);
		avatarImageView = (ImageView) findViewById(R.id.imageUser);
		hourLabel = (TextView) findViewById(R.id.textEventTime);
		amPmLabel = (TextView) findViewById(R.id.textAMPM);
		primaryLabel = (TextView) findViewById(R.id.textEventTitle);
		cellView = (View) findViewById(R.id.calCell);
		cellView.setBackgroundColor(Color.WHITE);
		cellView.setVisibility(View.VISIBLE);
		delegate = (ATLAlertEventRequestCellDelegate) context;
		confirmationView = (View) findViewById(R.id.alert_event_cell_confirm_view);
		confirmBtn = (ImageButton) findViewById(R.id.alert_event_cell_confirm_btn);
		confirmBtn.setOnClickListener(this);
		cellView.setOnClickListener(this);

		// leftView = (ViewGroup) findViewById(R.id.cal_cell_left_view);

		// rightView = (ViewGroup) findViewById(R.id.cal_cell_right_view);
		// rightView.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v == cellView) {
			// Handle slide out move events
			delegate.didTapToShowAcceptViewAtIndex(index);
			
		}
		else if(v == confirmBtn){
//			Toast.makeText(mActivity, "Confirmed Index " + index, Toast.LENGTH_SHORT).show();
			confirmBtn.setClickable(false);
			delegate.didAcceptAtIndex(index);
			
		}
		

	}

	int currentBKColor = 0;
	public void dismissConfirmView() {
		// TODO Auto-generated method stub
		confirmationView.setVisibility(View.GONE);
		confirmBtn.setVisibility(View.GONE);
		cellView.setBackgroundColor(currentBKColor);
		isShowConfirm = false;
		cellData.isShowOffHour = false;
	}


	public void showConfirmView() {
		// TODO Auto-generated method stub
		confirmationView.setVisibility(View.VISIBLE);
		confirmBtn.setVisibility(View.VISIBLE);
		cellView.setBackgroundColor(ATLColor.CELL_BACKGROUND_GREEN);
		isShowConfirm = true;
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
		
		// Dismiss OffHour
		cellView.setVisibility(View.VISIBLE);

		switch(cellData.eventResponseType_CellData){
		case EventResponseType.eventResponseType_None:
			currentBKColor = ATLColor.WHITE;
			 break;
		case EventResponseType.eventResponseType_NoAttendees:
			currentBKColor = ATLColor.CELL_BACKGROUND_YELLOW;
			break;
		case EventResponseType.eventResponseType_InvitesPending:
			currentBKColor = ATLColor.CELL_BACKGROUND_YELLOW;
			break;
		case EventResponseType.eventResponseType_AttendeeDecline:
			currentBKColor = ATLColor.CELL_BACKGROUND_RED;
			break;
		case EventResponseType.eventResponseType_AttendeeAccepted:
			currentBKColor = ATLColor.CELL_BACKGROUND_GREEN;
			break;
		}
		cellView.setBackgroundColor(currentBKColor);
		
		if(cellData.isBlank){
			//TODO: set no avatar
			avatarImageView.setVisibility(View.GONE);
		}
		else{
			//TODO: default avatar
			avatarImageView.setVisibility(View.VISIBLE);
			avatarImageView.setImageResource(R.drawable.avatar2x);
		}
		
		if(cellData.isShowOffHour)
		{
			showConfirmView();
		}
		else{
			dismissConfirmView();
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

}