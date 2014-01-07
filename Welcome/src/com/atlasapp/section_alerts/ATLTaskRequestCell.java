//  ==================================================================================================================
//  ATLTaskRequestCell.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================

package com.atlasapp.section_alerts;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlasapp.common.ATLAnimationUtils;
import com.atlasapp.common.ATLColor;
import com.atlasapp.common.ATLConstants.TaskCellPriority;
import com.atlasapp.section_appentry.R;
import com.atlasapp.section_tasks.ATLTaskPriorityScrollListAdapter;

interface ATLTaskRequestCellDelegete {

	void didChangeIsComplete(AlertCellData cellData);

	void didShowAcceptTaskAtIndex(int index);

	void didAcceptTaskAtIndex(AlertCellData cellData);

}

public class ATLTaskRequestCell extends RelativeLayout implements
		View.OnClickListener {

	Activity mActivity;
	public ImageView calendarImageView;
	public int calendarColor;
	public ImageView taskImageView;
	public ImageView priorityImageView;
	public ImageView flameImageView;
	public ImageView avatarImageView;
	public ImageView statusImageView;
	public ImageView dividerImageView;
	public ImageView checkboxImageView;
	public ImageView checkmarkImageView;
	public CheckBox checkboxButton;
	public TextView primaryLabel;
	public TextView secondaryLabel;
	public float taskPct;
	public TaskCellPriority priority;
	protected ATLTaskRequestCellDelegete delegate;
	View centerLayout;
	View rightLayout;
	public int cellIndex = -1;

	AlertCellData mAlertTaskCellData;
	private View acceptView;
	private View contentView;
	private ImageButton acceptButton;

	public ATLTaskRequestCell(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public ATLTaskRequestCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public ATLTaskRequestCell(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context) {
		// Init CallendarCell view
		LayoutInflater.from(context).inflate(R.layout.alert_task_request_cell,
				this, true);
		mActivity = (Activity) context;
		delegate = (ATLTaskRequestCellDelegete) context;
		calendarImageView = (ImageView) findViewById(R.id.imageCalendarColor);
		priorityImageView = (ImageView) findViewById(R.id.task_quick_add_priority_cell_img);
		secondaryLabel = (TextView) findViewById(R.id.taskDate);
		primaryLabel = (TextView) findViewById(R.id.taskTitle);

		centerLayout = (View) findViewById(R.id.centerLayout);
		rightLayout = (View) findViewById(R.id.rightLayout);
		centerLayout.setOnClickListener(this);
		rightLayout.setOnClickListener(this);

		checkboxButton = (CheckBox) findViewById(R.id.task_cell_is_complete_check_box);
		checkboxButton.setOnClickListener(this);
		acceptView = (View) findViewById(R.id.alert_task_cell_accept_view);
		acceptButton = (ImageButton) findViewById(R.id.alert_task_cell_accept_btn);
		acceptButton.setOnClickListener(this);
		contentView = (View)findViewById(R.id.alert_task_request_content_cell_view);
	}

	public void setCellData(AlertCellData cellData) {
		// TODO Auto-generated method stub
		mAlertTaskCellData = cellData;
		String time1 = (String) DateUtils.getRelativeDateTimeString(mActivity,
				mAlertTaskCellData.alertCellPreferredDatetime.getTime(),
				DateUtils.DAY_IN_MILLIS, DateUtils.WEEK_IN_MILLIS,
				DateUtils.FORMAT_SHOW_WEEKDAY);

		secondaryLabel.setText(time1);
		primaryLabel.setText(cellData.alertCellEventTitle);

		 if (cellData.isTaskCompleted) {
			 checkboxButton.setChecked(true);
		 } else {
			 checkboxButton.setChecked(false);
		 }

		int resourceID = 0;
		switch (cellData.alertCellTaskPriority) {
		case ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_HIGH:
			resourceID = R.drawable.tasks_flame_green;
			break;
		case ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_MEDIUM:
			resourceID = R.drawable.tasks_very_important_green;
			break;
		case ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_LOW:
			resourceID = R.drawable.tasks_important_green;
			break;

		}
		priorityImageView.setImageResource(resourceID);

		if(mAlertTaskCellData.isShowAcceptBtn){
			showAcceptTaskButton();
		}
		else{
			dismissAcceptTaskButton();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == rightLayout || v == centerLayout) {
			delegate.didShowAcceptTaskAtIndex(mAlertTaskCellData.cellIndex);
			showAcceptTaskButton();
		} else if (v == checkboxButton) {
			mAlertTaskCellData.isTaskCompleted = checkboxButton.isChecked();
			delegate.didChangeIsComplete(mAlertTaskCellData);
		} else if (v == acceptButton) {
			delegate.didAcceptTaskAtIndex(mAlertTaskCellData);
		}

	}

	public void dismissAcceptTaskButton() {
		// TODO Auto-generated method stub
		acceptView.setAnimation(ATLAnimationUtils.outAlpha());
		acceptView.setVisibility(View.GONE);
		contentView.setBackgroundColor(Color.WHITE);
		mAlertTaskCellData.isShowAcceptBtn = false;
	}

	public void showAcceptTaskButton() {
		// TODO Auto-generated method stub
		acceptView.setAnimation(ATLAnimationUtils.inAlpha());
		acceptView.setVisibility(View.VISIBLE);
		contentView.setBackgroundColor(ATLColor.orange_cell_bg);
		mAlertTaskCellData.isShowAcceptBtn = true;
	}

}