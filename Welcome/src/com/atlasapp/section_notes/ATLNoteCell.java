//  ==================================================================================================================
//  NotesCell.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-10-28 TAN:     init class to implement Notes View in simulate mode
//  ==================================================================================================================

package com.atlasapp.section_notes;

import com.atlasapp.common.ATLAnimationUtils;
import com.atlasapp.section_appentry.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

interface ATLNoteCellDelegete {

	void didShowDeleteNoteAtIndex(int index);

	void didDeleteNoteAtIndex(int index);

}

public class ATLNoteCell extends RelativeLayout implements
		View.OnClickListener, View.OnTouchListener {

	Activity mActivity;

	public ImageView backgroundImageView;
	public ImageView calendarImageView;
	public int calendarColor;
	public ImageView noteImageView;
	public TextView primaryLabel;
	public TextView secondaryLabel;
	View centerLayout;
	View rightLayout;
	ATLNoteCellData mNoteCellData;
	public ImageView dividerImageView;
	private View deleteView;
	private ImageButton deleteButton;
	protected ATLNoteCellDelegete delegate;
	int cellIndex = -1;

	public ATLNoteCell(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public ATLNoteCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public ATLNoteCell(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	private void initView(Context context) {
		// Init CallendarCell view
		LayoutInflater.from(context).inflate(R.layout.note_cell, this, true);
		mActivity = (Activity) context;
		delegate = (ATLNoteCellDelegete) context;
		calendarImageView = (ImageView) findViewById(R.id.imageCalendarColor);
		secondaryLabel = (TextView) findViewById(R.id.noteDescription);
		primaryLabel = (TextView) findViewById(R.id.noteTitle);
		noteImageView = (ImageView) findViewById(R.id.imageNote);

		centerLayout = (View) findViewById(R.id.centerLayout);
		rightLayout = (View) findViewById(R.id.rightLayout);
		// centerLayout.setOnTouchListener(this);
		// rightLayout.setOnTouchListener(this);
		centerLayout.setOnClickListener(this);
		rightLayout.setOnClickListener(this);

		deleteView = (View) findViewById(R.id.note_cell_delete_view);
		deleteButton = (ImageButton) findViewById(R.id.note_cell_delete_btn);
		deleteButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == centerLayout || v == rightLayout) {
			ATLNoteSingleton.getInstance().setData(mNoteCellData);
			Intent i = new Intent(mActivity, ATLNoteEditActivity.class);
			mActivity.startActivityForResult(i,
					ATLNoteIntentKeyList.CALL_FROM_NOTE_CELL);

		} else if (v == deleteButton) {
			Log.v("Delete Button", "Delete Button");
			delegate.didDeleteNoteAtIndex(cellIndex);
		}
	}

	public void setCellData(ATLNoteCellData cellData) {
		// TODO Auto-generated method stub
		mNoteCellData = cellData.copy();
		secondaryLabel.setText(mNoteCellData.noteCellBody);
		primaryLabel.setText(setTitle(mNoteCellData.noteCellTitle));
		calendarImageView
				.setBackgroundColor(mNoteCellData.noteCellCalendarColor);
		noteImageView.setBackgroundColor(Color.TRANSPARENT);
//		switch (mNoteCellData.noteCellCalendarColor) {
//		case Color.RED:
//			noteImageView.setBackgroundResource(R.drawable.notes_notered_star);
//			break;
//		case Color.GREEN:
//			noteImageView
//					.setBackgroundResource(R.drawable.notes_body_notegreen);
//			break;
//		case Color.BLUE:
//			noteImageView.setBackgroundResource(R.drawable.note_blue);
//			break;
//		case Color.CYAN:
//		case Color.WHITE:
//			noteImageView.setBackgroundResource(R.drawable.note_red);
//			break;
//		case Color.YELLOW:
//			noteImageView
//					.setBackgroundResource(R.drawable.notes_body_notegreen_star);
//		}
		if (mNoteCellData.isSelectedStar) {
			noteImageView
					.setBackgroundResource(R.drawable.notes_body_notegreen_star);
		} else {
			noteImageView
					.setBackgroundResource(R.drawable.notes_body_notegreen);
		}

	}

	private String setTitle(String text) {
		if (text.length() > 20)
			text = text.substring(0, 20) + "..";

		return text;
	}

	private float point_x;
	private boolean isDeleting = false;
	private boolean isDeleted = false;
	// private boolean isStartSingleTap = false;
	// private boolean isStartDoubleTap = false;
	private final int TOUCH_MOVE_DISTANCE = 60;

	// private final int SINGLE_TOUCH_DURATION = 120;
	// private final int DOUBLE_TOUCH_DURATION = 300;
	// private long startTouchTime = 0;
	// private long endTouchTime_1 = 0;
	// private long endTouchTime_2 = 0;
	// private long touchDuration1 = 0;
	// private long touchDuration2 = 0;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// Do nothing
			// isMove = true;
			Log.v("NoteCell", "action down");
			point_x = event.getX();
			// isStartSingleTap = true;
			// startTouchTime = System.currentTimeMillis();
			break;
		case MotionEvent.ACTION_MOVE:
			Log.v("NoteCell", "action move");

			if (Math.abs(event.getX() - point_x) > TOUCH_MOVE_DISTANCE) {
				isDeleted = true;

			}

			break;
		case MotionEvent.ACTION_UP:
			Log.v("NoteCell", "action up");
			if (isDeleted) {
				if (!isDeleting) {
					showDeleteTaskButton();
					// delegate.didShowDeleteTaskAtIndex(cellIndex);
					// isStartSingleTap = false;
				} else {
					dismissDeleteTaskButton();
					// isStartSingleTap = false;
				}
			}
			// endTouchTime_2 = endTouchTime_1;
			// endTouchTime_1 = System.currentTimeMillis();
			// touchDuration1 = endTouchTime_1 - startTouchTime;
			// Log.v("CalendarCell", "endTouchTime_2 : " + endTouchTime_2);
			// Log.v("CalendarCell", "endTouchTime_1 : " + endTouchTime_1);
			// Log.v("CalendarCell", "touchDuration1 : " + touchDuration1);
			// if (touchDuration1 <= SINGLE_TOUCH_DURATION) {
			// rightLayout.postDelayed(new Runnable() {
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			// touchDuration2 = endTouchTime_1 - endTouchTime_2;
			// Log.v("CalendarCell", "touchDuration2 : "
			// + touchDuration2);
			// if (touchDuration2 <= DOUBLE_TOUCH_DURATION) {
			// // isStartSingleTap = false;
			// if (!isStartDoubleTap) {
			// isStartDoubleTap = true;
			// delegate.didDoubleTapToQuickAddEventAtIndex(mAlertTaskCellData,
			// cellIndex);
			// } else {
			// isStartDoubleTap = false;
			// }
			//
			// } else {
			// EditTaskModel.getInstance().setData(mAlertTaskCellData);
			// Intent i = new Intent(mActivity, EditTask.class);
			// mActivity.startActivityForResult(i,
			// ATLTaskIntentKeys.CALL_FROM_TASK_CELL);
			// }
			//
			// }
			// }, DOUBLE_TOUCH_DURATION);
			// }

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
		Log.v("dismissDeleteTaskButton", "dismissDeleteTaskButton");
		deleteView.setAnimation(ATLAnimationUtils.outToRightAnimation(200));
		deleteView.setVisibility(View.GONE);
		isDeleting = false;
		// mAlertTaskCellData.isShowDelete = false;
	}

	public void showDeleteTaskButton() {
		// TODO Auto-generated method stub
		Log.v("showDeleteTaskButton", "showDeleteTaskButton");
		isDeleting = true;
		deleteView.setAnimation(ATLAnimationUtils.inFromRightAnimation(300));
		deleteView.setVisibility(View.VISIBLE);
		// mAlertTaskCellData.isShowDelete = true;
	}

}
