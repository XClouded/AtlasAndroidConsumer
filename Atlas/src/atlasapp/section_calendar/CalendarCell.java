//  ==================================================================================================================
//  CalendarCell.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-10-22 TAN:    Init Class and port all properties and methods from iOS_IDAHO 
//  2012-10-24 TAN:    Handle load(Context ctx) method
//  ==================================================================================================================
package atlasapp.section_calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import atlasapp.common.ATLAnimationUtils;
import atlasapp.common.ATLColor;
import atlasapp.common.UtilitiesProject;
import atlasapp.common.ATLConstants.EventResponseType;
import atlasapp.section_appentry.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

interface CalendarCellDelegateInterface {
	void didTapToShowOffHours(int index);

	void requestAddNewView(int hour, int minute);

	void didTouchToMoveEvent(int index);

	void didDeleteEventAtIndex(int index);

	void didDoubleTapToQuickAddEventAtIndex(int index);

	void conformCellData(ATLCalCellData cellData);
	
	
}

public class CalendarCell extends RelativeLayout implements
		View.OnClickListener, View.OnTouchListener,
		OffHourTimesPickerViewDelegate {
	static public final String CELL_IS_BLANK = "isBlank";
	static public final String MOVE_EVENT_FROM_CELL = "fromCell";
	Activity mActivity;
	ImageView calendarImageView; // Calendar color view
	TextView hourLabel;
	TextView amPmLabel;
	TextView primaryLabel;
	TextView untilLabel;
	TextView locationLabel;
	ViewGroup leftView;
	ViewGroup rightView;
	ATLCalCellData cellData;
	private OffHourTimesPickerView offHours;
	ViewGroup mainView;
	public static CalendarCellDelegateInterface calendarDayView;
	public int index = -1;
	public boolean isShowOffHour = false;

	ImageButton calendarCellConfirmBtn;
//	ImageButton deleteEventBtn;
	private ImageView calendarCellInviterImage;

	public CalendarCell(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public CalendarCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public CalendarCell(Context contex) {
		super(contex);
		calendarDayView = (CalendarCellDelegateInterface) contex;  
		// TODO Auto-generated constructor stub
		initView(contex);
	}

	private void initView(Context context) {
		// Init CallendarCell view
		LayoutInflater.from(context)
				.inflate(R.layout.calendar_cell, this, true);
		mActivity = (Activity) context;

//		calendarDayView = (CalendarDayView) mActivity;

		mainView = (ViewGroup) findViewById(R.id.calendar_cell_layout);
		calendarImageView = (ImageView) findViewById(R.id.imageCalendarColor);
		hourLabel = (TextView) findViewById(R.id.textEventTime);
		amPmLabel = (TextView) findViewById(R.id.textAMPM);
		primaryLabel = (TextView) findViewById(R.id.textEventTitle);

		untilLabel = (TextView) findViewById(R.id.calendar_cell_until_text);
		locationLabel = (TextView) findViewById(R.id.calendar_cell_event_location_text);
		rightView = (ViewGroup) findViewById(R.id.cal_cell_right_view);
		leftView = (ViewGroup) findViewById(R.id.cal_cell_left_view);

		calendarCellConfirmBtn = (ImageButton) findViewById(R.id.calendarCellConfirmBtn);
		calendarCellConfirmBtn.setVisibility(View.GONE);
//		moveEventBtn.setOnClickListener(this);
		calendarCellInviterImage = (ImageView) findViewById(R.id.calendarCellInviterImage);
		calendarCellInviterImage.setVisibility(View.GONE);
//		deleteEventBtn.setOnClickListener(this);

		offHours = new OffHourTimesPickerView(mActivity);
		addView(offHours, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		offHours.delegate = this;
		offHours.setVisibility(View.GONE);
		// rightView.setOnClickListener(this);
		leftView.setOnClickListener(this);
		offHours.setOnClickListener(this);

		rightView.setOnTouchListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == rightView) {
			CalendarEventSingleton.getInstance().setCalCellData(this.cellData);
			Intent i = new Intent(mActivity, CalendarEditView.class);
			i.putExtra(CELL_IS_BLANK, this.cellData.isBlank);
			// i.putExtra("celldata", this.cellData);
			mActivity.startActivityForResult(i,
					ATLCalendarIntentKeys.CALL_FROM_CALENDAR_CELL);
			mActivity.overridePendingTransition(0, R.anim.in_from_bottom);

		} else if (v == leftView) {
			// Handle slide out move events
			showOffHours();
			calendarDayView.didTapToShowOffHours(index);

		} else if (v == offHours) {
			dismissOffHours();
		}
//		} else if (v == moveEventBtn) {
//			// Toast.makeText(mActivity, "moveEventBtn",
//			// Toast.LENGTH_SHORT).show();
//			CalendarEventSingleton.getInstance().setCalCellData(this.cellData);
//			Intent i = new Intent(mActivity, CalendarMoveEvent.class);
//			i.putExtra(MOVE_EVENT_FROM_CELL, true);
//			
//			mActivity.startActivityForResult(i,
//					ATLCalendarIntentKeys.CALL_FROM_CALENDAR_CELL);
////		} else if (v == deleteEventBtn) {
////			// Toast.makeText(mActivity, "deleteEventBtn", Toast.LENGTH_SHORT)
////			// .show();
////			calendarDayView.didDeleteEventAtIndex(index);
////		}
//		}
	}

	public void dismissOffHours() {
		// TODO Auto-generated method stub
		mainView.setAnimation(ATLAnimationUtils.inFromRightAnimation());
		offHours.setAnimation(ATLAnimationUtils.outToLeftAnimation());
		mainView.setVisibility(View.VISIBLE);
		offHours.setVisibility(View.GONE);
		isShowOffHour = false;
		cellData.isShowOffHour = false;
	}

	public void showOffHours() {
		// TODO Auto-generated method stub
		mainView.setAnimation(ATLAnimationUtils.outToRightAnimation());
		offHours.setAnimation(ATLAnimationUtils.inFromLeftAnimation());
		mainView.setVisibility(View.GONE);
		offHours.setVisibility(View.VISIBLE);
		isShowOffHour = true;
		cellData.isShowOffHour = true;

	}

	public void showMoveEvents() {
		// TODO Auto-generated method stub
//		deleteEventBtn
//				.setAnimation(ATLAnimationUtils.inFromRightAnimation(300));
//		moveEventBtn.setAnimation(ATLAnimationUtils.inFromRightAnimation(200));
//		deleteEventBtn.setVisibility(View.VISIBLE);
//		moveEventBtn.setVisibility(View.VISIBLE);
		cellData.isShowMoveEvent = true;
		isAdd = true;

	}

	public void dismissMoveEvents() {
		// TODO Auto-generated method stub
//		deleteEventBtn.setAnimation(ATLAnimationUtils.outToRightAnimation(200));
//		moveEventBtn.setAnimation(ATLAnimationUtils.outToRightAnimation(300));
//		deleteEventBtn.setVisibility(View.GONE);
//		moveEventBtn.setVisibility(View.GONE);
		cellData.isShowMoveEvent = false;
		isAdd = false;

	}

	public void setCellData(final ATLCalCellData cellData, final int position) {
		// TODO Auto-generated method stub
		// calendarImageView.setVisibility(View.GONE);

		index = position;
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
		primaryLabel.setText(cellData.getCalCellTitle());
		calendarImageView
				.setBackgroundColor(cellData.getCalCellCalendarColor());

		Calendar calEndTime = Calendar.getInstance();
		calEndTime.setTimeInMillis(cellData.getCalCellEndDate().getTime());

		SimpleDateFormat sf = new SimpleDateFormat("h:mma");
		int compareDay = calStart.getTime().compareTo(calEndTime.getTime());
		if (compareDay < 0) {
			untilLabel.setText("until " + sf.format(calEndTime.getTime()));
		} else {

			untilLabel.setText("");
		}
		if (cellData.getCalCellLocation() != null
				&& !cellData.getCalCellLocation().equals("")) {
			locationLabel.setText("@ " + cellData.getCalCellLocation());
		} else {
			locationLabel.setText("");
		}
		// Reload view always set offHours GONE
		mainView.setVisibility(View.VISIBLE);
		offHours.setVisibility(View.GONE);
		cellData.isShowOffHour = false;

		offHours.setCellData(cellData);// Set data for offHours view

//		moveEventBtn.setVisibility(View.GONE);
//		deleteEventBtn.setVisibility(View.GONE);
		isAdd = false;
		Bitmap pic = UtilitiesProject.getProfilePic(cellData.inviterAtlasId);
		if (pic!=null)
		{
//			calendarCellConfirmBtn.setVisibility(View.VISIBLE);
//			calendarCellConfirmBtn.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//
//					if (calendarDayView!=null)
//					calendarDayView.conformCellData(cellData);
//				}
//			});
			calendarCellInviterImage.setVisibility(View.VISIBLE);
			calendarCellInviterImage.setImageBitmap(pic);
		}
		else
		{
			calendarCellConfirmBtn.setVisibility(View.GONE);
			calendarCellInviterImage.setVisibility(View.GONE);
		}
		switch (cellData.eventResponseType_CellData) {
		case None:
			mainView.setBackgroundColor(ATLColor.WHITE);
			break;
		case NoAttendees:
			mainView.setBackgroundColor(ATLColor.CELL_BACKGROUND_YELLOW);
			break;
		case Pending:
			mainView.setBackgroundColor(ATLColor.CELL_BACKGROUND_YELLOW);
			break;
		case Decline:
			mainView.setBackgroundColor(ATLColor.CELL_BACKGROUND_RED);
			break;
		case Accepted:
			mainView.setBackgroundColor(ATLColor.CELL_BACKGROUND_GREEN);
			break;
		case Deleted:
			mainView.setBackgroundColor(ATLColor.WHITE);
			break;
		}
	}

	public void setCellData(final ATLCalCellData cellData) {
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
		primaryLabel.setText(cellData.getCalCellTitle());
		calendarImageView
				.setBackgroundColor(cellData.getCalCellCalendarColor());

		Calendar calEndTime = Calendar.getInstance();
		calEndTime.setTimeInMillis(cellData.getCalCellEndDate().getTime());

		SimpleDateFormat sf = new SimpleDateFormat("h:mma");
		int compareDay = calStart.getTime().compareTo(calEndTime.getTime());
		if (compareDay < 0) {
			untilLabel.setText("until " + sf.format(calEndTime.getTime()));
		} else {

			untilLabel.setText("");
		}
		if (cellData.getCalCellLocation() != null
				&& !cellData.getCalCellLocation().equals("")) {
			locationLabel.setText("@ " + cellData.getCalCellLocation());
		} else {
			locationLabel.setText("");
		}

		// Reload view always set offHours GONE
		mainView.setVisibility(View.VISIBLE);
		offHours.setVisibility(View.GONE);
		cellData.isShowOffHour = false;

		offHours.setCellData(cellData);// Set data for offHours view
//		moveEventBtn.setVisibility(View.GONE);
		Bitmap pic = UtilitiesProject.getProfilePic(cellData.inviterAtlasId);
		if (pic!=null)
		{
			calendarCellInviterImage.setVisibility(View.VISIBLE);
			calendarCellInviterImage.setImageBitmap(pic);
//			calendarCellConfirmBtn.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//
//					if (calendarDayView!=null)
//					calendarDayView.conformCellData(cellData);
//				}
//			});
//			calendarCellConfirmBtn.setVisibility(View.VISIBLE);
		}else
		{
			calendarCellConfirmBtn.setVisibility(View.GONE);
			calendarCellInviterImage.setVisibility(View.GONE);
		}
			
//		}deleteEventBtn.setVisibility(View.GONE);
		isAdd = false;

		switch (cellData.eventResponseType_CellData) {
		case None:
			mainView.setBackgroundColor(ATLColor.WHITE);
			break;
		case NoAttendees:
			mainView.setBackgroundColor(ATLColor.CELL_BACKGROUND_YELLOW);
			break;
		case Pending:
			mainView.setBackgroundColor(ATLColor.CELL_BACKGROUND_YELLOW);
			break;
		case Decline:
			mainView.setBackgroundColor(ATLColor.CELL_BACKGROUND_RED);
			break;
		case Accepted:
			mainView.setBackgroundColor(ATLColor.CELL_BACKGROUND_GREEN);
			break;
		case Deleted:
			mainView.setBackgroundColor(ATLColor.WHITE);
			break;
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

	// 2012-12-08 TAN: Handle Touch events in the right view of
	// CalendarCell========
	private float point_x;
	private boolean isAdd = false;
	private final int TOUCH_MOVE_DISTANCE = 60;

	private double delta = 0;
	private int tapCount = 0;
	private Handler myHandler = new Handler();

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			point_x = event.getX();
			delta = 0;
			/*
			 // Do nothing
			// isMove = true;
			Log.v("CalendarCell", "action down");
			point_x = event.getX();
			isStartSingleTap = true;
			startTouchTime = System.currentTimeMillis();
			break;
			 */
			break;
		case MotionEvent.ACTION_MOVE: {
			delta = Math.abs(event.getX() - point_x);
			/*
			  if (!cellData.isBlank) {
				if (Math.abs(event.getX() - point_x) > TOUCH_MOVE_DISTANCE
						&& isStartSingleTap) {
					if (!isAdd) {
						showMoveEvents();
						calendarDayView.didTouchToMoveEvent(index);
						isStartSingleTap = false;
					} else {
						dismissMoveEvents();
						isStartSingleTap = false;
					}
				}
			} else {
				if (Math.abs(event.getX() - point_x) > TOUCH_MOVE_DISTANCE
						&& isStartSingleTap) {
					calendarDayView.didTouchToMoveEvent(index);
					isStartSingleTap = false;
				}
			}
			 
			 */
			break;
		}
		case MotionEvent.ACTION_UP:
			if (isAdd) {
				isAdd = false;
				dismissMoveEvents();
				return true;
			} else {
				if(cellData.isEditable)
				if (delta > TOUCH_MOVE_DISTANCE ) {
					if(!cellData.isBlank ){
						showMoveEvents();
						calendarDayView.didTouchToMoveEvent(index);
					}
				} else {

					tapCount++;
					switch (tapCount) {
					case 1: {
						
						// waiting in 0.15 second
						myHandler.postDelayed(showEditEventActivity, 150);
						break;
					}
					case 2: {
						myHandler.removeCallbacks(showEditEventActivity);
						calendarDayView
								.didDoubleTapToQuickAddEventAtIndex(index);

						break;
					}
					default:
						break;
					}
					if (tapCount >= 2) {
						tapCount = 0;
					}
				}
			}
			/*
			 Log.v("CalendarCell", "action up");
			endTouchTime_2 = endTouchTime_1;
			endTouchTime_1 = System.currentTimeMillis();
			touchDuration1 = endTouchTime_1 - startTouchTime;
			Log.v("CalendarCell", "endTouchTime_2 : " + endTouchTime_2);
			Log.v("CalendarCell", "endTouchTime_1 : " + endTouchTime_1);
			Log.v("CalendarCell", "touchDuration1 : " + touchDuration1);
			if (touchDuration1 <= SINGLE_TOUCH_DURATION) {
				rightView.postDelayed(new Runnable() {
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
								calendarDayView.didDoubleTapToQuickAddEventAtIndex(index);
							} else {
								isStartDoubleTap = false;
							}

						} else {
							// Toast.makeText(mActivity, "isSingleTap",
							// Toast.LENGTH_SHORT).show();
							CalendarEventSingleton.getInstance()
									.setCalCellData(cellData);
							Intent i = new Intent(mActivity,
									CalendarEditView.class);
							i.putExtra(CELL_IS_BLANK, cellData.isBlank);
							// i.putExtra("celldata", this.cellData);
							mActivity.startActivityForResult(i,
									ATLCalendarIntentKeys.CALL_FROM_CALENDAR_CELL);
						}

					}
				}, DOUBLE_TOUCH_DURATION);
			}

			 */
			break;

		}
		return true;
	}

	// END
	// ============================================================================

	@Override
	public void didChooseStartMinute(int minute) {
		// TODO Auto-generated method stub
		// Toast.makeText(mActivity, "minute " + minute, Toast.LENGTH_SHORT)
		// .show();
		Calendar calStart = Calendar.getInstance();
		calStart.setTimeInMillis(cellData.getCalCellStartDate().getTime());
		calendarDayView.requestAddNewView(calStart.get(Calendar.HOUR_OF_DAY),
				minute);
	}

	private Runnable showEditEventActivity = new Runnable() {
		@Override
		public void run() {
			// Change state here
			tapCount = 0;
			CalendarEventSingleton.getInstance().setCalCellData(cellData);
			Intent i = new Intent(mActivity, CalendarEditView.class);
			i.putExtra(CELL_IS_BLANK, cellData.isBlank);
			i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			mActivity.startActivityForResult(i,
					ATLCalendarIntentKeys.CALL_FROM_CALENDAR_CELL);
			
			mActivity.overridePendingTransition(R.anim.in_from_bottom, R.anim.stand_by);
		}
	};

}
