//  ==================================================================================================================
//  TaskCell.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-10-28 TAN:    Init class, add all atribute as iPhone
//					   implement constructor, intiView(Context context), setCellData(ATLTaskCellData cellData) method
//  ==================================================================================================================

package com.atlasapp.section_tasks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import com.atlasapp.section_appentry.R;
import com.atlasapp.common.ATLConstants.EventResponseType;
import com.atlasapp.common.ATLConstants.TaskCellPriority;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atlasapp.common.ATLAnimationUtils;
import com.atlasapp.common.ATLColor;
import com.atlasapp.common.ATLConstants.TaskResponseType;
import com.atlasapp.common.CalendarUtilities;
import com.atlasapp.common.ATLConstants.TaskCellPriority;
import com.atlasapp.model.ATLCalendarModel;
import com.atlasapp.section_appentry.R;
import com.atlasapp.section_calendar.ATLCalendarManager;

interface ATLTaskCellDelegete {

	void didChangeIsComplete(ATLTaskCellData cellData);

	void didShowDeleteTaskAtIndex(int index);
	
	void didDeleteTaskAtIndex(int index);
	
	void didDoubleTapToQuickAddEventAtIndex(ATLTaskCellData mTaskCellData, int index);

}

public class TaskCell extends RelativeLayout implements View.OnClickListener,
		View.OnTouchListener {

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
	protected ATLTaskCellDelegete delegate;
	View centerLayout;
	View rightLayout;
	View mainView;
	public int cellIndex = -1;

	ATLTaskCellData mTaskCellData;
	private View deleteView;
	private ImageButton deleteButton;
	private ArrayList<ATLCalendarModel> calListModel;

	public TaskCell(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public TaskCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public TaskCell(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context) {
		// Init CallendarCell view
		LayoutInflater.from(context).inflate(R.layout.task_cell, this, true);
		mActivity = (Activity) context;
		delegate = (ATLTaskCellDelegete) context;
		calendarImageView = (ImageView) findViewById(R.id.imageCalendarColor);
		priorityImageView = (ImageView) findViewById(R.id.task_quick_add_priority_cell_img);
		secondaryLabel = (TextView) findViewById(R.id.taskDate);
		primaryLabel = (TextView) findViewById(R.id.taskTitle);

		centerLayout = (View) findViewById(R.id.centerLayout);
		rightLayout = (View) findViewById(R.id.rightLayout);
		centerLayout.setOnTouchListener(this);
		rightLayout.setOnTouchListener(this);
		
		mainView = (View) findViewById(R.id.task_cell_content_view);

		checkboxButton = (CheckBox) findViewById(R.id.task_cell_is_complete_check_box);
		checkboxButton.setOnClickListener(this);
		deleteView = (View) findViewById(R.id.task_cell_delete_view);
		deleteButton = (ImageButton) findViewById(R.id.task_cell_delete_btn);
		deleteButton.setOnClickListener(this);
	}

	public void setCellData(ATLTaskCellData cellData) {
		// TODO Auto-generated method stub
		mTaskCellData = cellData;
		String timeString = (String)DateFormat.format(getResources().
				getString(R.string.calendar_day_view_day_format), mTaskCellData.taskCellDueDate);
//		String timeString = (String) DateUtils.getRelativeDateTimeString(mActivity,
//				mTaskCellData.taskCellDueDate.getTime(), DateUtils.WEEK_IN_MILLIS,
//				DateUtils.WEEK_IN_MILLIS, DateUtils.LENGTH_LONG);
		if (CalendarUtilities.isToday(mTaskCellData.taskCellDueDate)){
			timeString = "Today";
		}
		else if(CalendarUtilities.isTomorrow(mTaskCellData.taskCellDueDate)){
			timeString = "Tomorrow";
		}

		secondaryLabel.setText(timeString);
		primaryLabel.setText(cellData.taskCellTitle);
		
		if (cellData.taskCellIsCompleted) {
			checkboxButton.setChecked(true);
		} else {
			checkboxButton.setChecked(false);
		}
		
		calListModel = ATLCalendarManager.getCalendars(mActivity);
		for (ATLCalendarModel calModel : calListModel) {
			if (mTaskCellData.taskCellCalendarName == null || mTaskCellData.taskCellCalendarName.equals("")) {// Default
									
				break;
			} else if (mTaskCellData.taskCellCalendarName.equals(calModel.name)) {
				mTaskCellData.calendarId = calModel.id;
				mTaskCellData.taskCellCalendarColor = calModel.color;
				calendarImageView.setBackgroundColor(calModel.color);
				break;
			}
		}
		
		int resourceID = 0;
		switch (mTaskCellData.taskCellPriorityInt) {
		case ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_HIGH:
			 resourceID = R.drawable.tasks_flame_blue;
			break;
		case ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_MEDIUM:
			 resourceID = R.drawable.tasks_very_important_blue;
			break;
		case ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_LOW:
			 resourceID = R.drawable.tasks_important_blue;
			break;

		}
		priorityImageView.setImageResource(resourceID);
		
		// Reload view always set Delete View GONE
		deleteView.setVisibility(View.GONE);
		mTaskCellData.isShowDelete = false;
		isAdd = false;
		
		switch(mTaskCellData.taskResponseStatus){
		case TaskResponseType.taskResponseType_None:
			mainView.setBackgroundColor(ATLColor.WHITE);
			 break;
		case TaskResponseType.taskResponseType_NoAssignees:
			mainView.setBackgroundColor(ATLColor.CELL_BACKGROUND_YELLOW);
			break;
		case TaskResponseType.taskResponseType_AssignmentsPending:
			mainView.setBackgroundColor(ATLColor.CELL_BACKGROUND_YELLOW);
			break;
		case TaskResponseType.taskResponseType_AssigneeDeclined:
			mainView.setBackgroundColor(ATLColor.CELL_BACKGROUND_RED);
			break;
		case TaskResponseType.taskResponseType_AssigneeAccepted:
			mainView.setBackgroundColor(ATLColor.CELL_BACKGROUND_GREEN);
			break;
		case TaskResponseType.taskResponseType_AssigneeCompleted:
			mainView.setBackgroundColor(ATLColor.CELL_BACKGROUND_GRAY);
			break;	
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == rightLayout || v == centerLayout) {
			EditTaskModel.getInstance().setData(mTaskCellData);
			Intent i = new Intent(mActivity, EditTask.class);
			mActivity.startActivityForResult(i,
					ATLTaskIntentKeys.CALL_FROM_TASK_CELL);
		} else if (v == checkboxButton) {
			mTaskCellData.taskCellIsCompleted = checkboxButton.isChecked();
			delegate.didChangeIsComplete(mTaskCellData);
		} else if (v== deleteButton){
			dismissDeleteTaskButton();
			delegate.didDeleteTaskAtIndex(cellIndex);
		}

	}

	// ===============================================================================
	// 2012-12-19 TAN: Touch events START Implement
	// =================================
	// ===============================================================================
	private float point_x;
	private boolean isAdd = false;
	private boolean isStartSingleTap = false;
	private boolean isStartDoubleTap = false;
	private final int TOUCH_MOVE_DISTANCE = 60;
	private final int SINGLE_TOUCH_DURATION = 120;
	private final int DOUBLE_TOUCH_DURATION = 300;
	private long startTouchTime = 0;
	private long endTouchTime_1 = 0;
	private long endTouchTime_2 = 0;
	private long touchDuration1 = 0;
	private long touchDuration2 = 0;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// Do nothing
			// isMove = true;
			Log.v("CalendarCell", "action down");
			point_x = event.getX();
			isStartSingleTap = true;
			startTouchTime = System.currentTimeMillis();
			break;
		case MotionEvent.ACTION_MOVE:
			Log.v("CalendarCell", "action move");

			if (Math.abs(event.getX() - point_x) > TOUCH_MOVE_DISTANCE
					&& isStartSingleTap) {
				if (!isAdd) {
					delegate.didShowDeleteTaskAtIndex(cellIndex);
					showDeleteTaskButton();
					isStartSingleTap = false;
				} else {
					dismissDeleteTaskButton();
					isStartSingleTap = false;
				}
			}

			break;
		case MotionEvent.ACTION_UP:
			Log.v("CalendarCell", "action up");
			endTouchTime_2 = endTouchTime_1;
			endTouchTime_1 = System.currentTimeMillis();
			touchDuration1 = endTouchTime_1 - startTouchTime;
			Log.v("CalendarCell", "endTouchTime_2 : " + endTouchTime_2);
			Log.v("CalendarCell", "endTouchTime_1 : " + endTouchTime_1);
			Log.v("CalendarCell", "touchDuration1 : " + touchDuration1);
			if (touchDuration1 <= SINGLE_TOUCH_DURATION) {
				rightLayout.postDelayed(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						touchDuration2 = endTouchTime_1 - endTouchTime_2;
						Log.v("CalendarCell", "touchDuration2 : "
								+ touchDuration2);
						if (touchDuration2 <= DOUBLE_TOUCH_DURATION) {
							// isStartSingleTap = false;
							if (!isStartDoubleTap) {
								isStartDoubleTap = true;
								delegate.didDoubleTapToQuickAddEventAtIndex(mTaskCellData.copy(), cellIndex);
							} else {
								isStartDoubleTap = false;
							}

						} else {
							EditTaskModel.getInstance().setData(mTaskCellData.copy());
							Intent i = new Intent(mActivity, EditTask.class);
							i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
							mActivity.startActivityForResult(i,
									ATLTaskIntentKeys.CALL_FROM_TASK_CELL);
							mActivity.overridePendingTransition(R.anim.in_from_bottom, R.anim.stand_by);
						}

					}
				}, DOUBLE_TOUCH_DURATION);
			}

			break;

		}
		return true;
	}

	// ===============================================================================
	// 2012-12-19 TAN: Touch events END Implement
	// ===================================
	// ===============================================================================

	public void dismissDeleteTaskButton() {
		// TODO Auto-generated method stub
		deleteView.setAnimation(ATLAnimationUtils.outToRightAnimation(200));
		deleteView.setVisibility(View.GONE);
		isAdd = false;
		mTaskCellData.isShowDelete = false;
	}

	public void showDeleteTaskButton() {
		// TODO Auto-generated method stub
		deleteView.setAnimation(ATLAnimationUtils.inFromRightAnimation(300));
		deleteView.setVisibility(View.VISIBLE);
		isAdd = true;
		mTaskCellData.isShowDelete = true;
	}
}