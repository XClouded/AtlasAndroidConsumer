//  ==================================================================================================================
//  CalendarEditAltView.java
//  ATLAS
//  Copyright (c) 2012 Atlas Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-11-24 TAN:    Implement data flow
//  2012-11-11 NGHIA:  Created
//  ==================================================================================================================

package atlasapp.section_calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import atlasapp.common.CalendarUtilities;
import atlasapp.model.ATLCalendarModel;
import atlasapp.section_appentry.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarEditAltView extends Activity implements OnClickListener,
		CalendarEditAltCellInterface {

	ATLCalendarListEditAltAdapter adapter;
	ATLCalCellList aCalCellList;
	ListView listEvent;
	final int PRE = 0;
	final int ALT2 = 1;
	final int ALT3 = 2;
	int FLAG = 0;

	TextView preferDay;
	TextView preferHour;
	TextView alt2Hour;
	TextView alt2Day;
	TextView alt3Hour;
	TextView alt3Day;

	ImageButton backArrowBtn;
	ImageButton nextArrowBtn;
	TextView dateLable;

	Date currentDate = new Date();
	private ArrayList<Integer> atlIndexs = new ArrayList<Integer>();
	private ArrayList<Date> atlDateArr = new ArrayList<Date>();
	private ImageView alt2Img;
	private ImageView alt3Img;
	private ImageView preferImg;
	private int altCurrentActive;
	private ATLCalCellData currentCellData = new ATLCalCellData();
	private Date preferDayNew;
	private Date alt2DayNew;
	private Date alt3DayNew;
	private int lastShowOffHourIndex = -1;
	private ArrayList<ATLCalendarModel> calListModel;
	private ArrayList<String> calendarInActiveNameArray;

	static final int CELL_BACKGROUND_ORANGE = Color.rgb(255, 211, 170);
	static final int CELL_BACKGROUND_WHITE = Color.WHITE;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_edit_alt_view);
		currentCellData.setCalCellDate(CalendarEventSingleton.getInstance()
				.getCalCellData().calCellPreferDateTime);
		currentCellData.setCalCellALT2Datetime(CalendarEventSingleton
				.getInstance().getCalCellData().getCalCellALT2Datetime());
		currentCellData.setCalCellALT3Datetime(CalendarEventSingleton
				.getInstance().getCalCellData().getCalCellALT3Datetime());
		currentCellData.setCalCellStartDate(CalendarEventSingleton
				.getInstance().getCalCellData().getCalCellStartDate());
		currentCellData.calCellPreferDateTime = CalendarEventSingleton.getInstance()
				.getCalCellData().calCellPreferDateTime;
		currentDate = currentCellData.calCellPreferDateTime;
		altCurrentActive = getIntent().getExtras().getInt(
				CalendarEditView.ALT_NUMBER);
		
		ImageView monthBtn = (ImageView) findViewById(R.id.monthBtn);
		monthBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(),
						CalendarMonthView.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				intent.putExtra(ATLCalendarIntentKeys.KEY_CURRENT_DAY_VIEW, currentDate.getTime());
				// //intent.putExtra("EXTRA_SESSION_ID", sessionId);
				startActivityForResult(intent,
						ATLCalendarIntentKeys.CALL_FROM_CALENDAR_ALT_VIEW);
				overridePendingTransition(R.anim.in_from_bottom, R.anim.stand_by);
			}
		});

		ImageView cancelBtn = (ImageView) findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.stand_by, R.anim.out_to_bottom);
			}
		});

		ImageView saveBtn = (ImageView) findViewById(R.id.saveBtn);
		saveBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO save data before finish
				Intent i = new Intent();
				int idx = 0;
				ArrayList<Date> atlDays = new ArrayList<Date>();
				if (preferDayNew != null)
					atlDays.add(preferDayNew);
				if (alt2DayNew != null)
					atlDays.add(alt2DayNew);
				if (alt3DayNew != null)
					atlDays.add(alt3DayNew);

				for (Date calDate : atlDays) {
					String time = (String) DateFormat.format(getResources()
							.getString(R.string.calendar_edit_atl_time_format),
							calDate);
					Log.v("CalendarEditAltView", time);
					i.putExtra("alt" + idx, time);
					idx++;
				}

				setResult(ATLCalendarIntentKeys.RESULT_FROM_ALT_VIEW, i);
				finish();
				overridePendingTransition(R.anim.stand_by, R.anim.out_to_bottom);
			}
		});

		preferDay = (TextView) findViewById(R.id.preferDay);
		preferHour = (TextView) findViewById(R.id.preferHour);
		preferImg = (ImageView) findViewById(R.id.preferImg);
		preferImg.setOnClickListener(this);

		alt2Hour = (TextView) findViewById(R.id.alt2Hour);
		alt2Day = (TextView) findViewById(R.id.alt2Day);
		alt2Img = (ImageView) findViewById(R.id.alt2);
		alt2Img.setOnClickListener(this);

		alt3Hour = (TextView) findViewById(R.id.alt3Hour);
		alt3Day = (TextView) findViewById(R.id.alt3Day);
		alt3Img = (ImageView) findViewById(R.id.alt3);
		alt3Img.setOnClickListener(this);

		backArrowBtn = (ImageButton) findViewById(R.id.prevDay);
		backArrowBtn.setOnClickListener(this);
		nextArrowBtn = (ImageButton) findViewById(R.id.nextDay);
		nextArrowBtn.setOnClickListener(this);
		dateLable = (TextView) findViewById(R.id.currentDay);

		listEvent = (ListView) findViewById(R.id.calendarList);
		calListModel = ATLCalendarStore.loadCalendarList(this);
		calendarInActiveNameArray = ATLCalendarStore.loadInActiveCalendarNameList(this);
		
		aCalCellList = new ATLCalCellList(calListModel,calendarInActiveNameArray);
		aCalCellList.calListDate = currentCellData.calCellPreferDateTime;
		adapter = new ATLCalendarListEditAltAdapter(aCalCellList, this,null);
		listEvent.setAdapter(adapter);
		
		initATLTime(); //20113-02-08 TAN: just init only one time when 
		dateDidChanged();// Set data to view
		 initATLTime(); // TAN : To handle when change current date
		listEvent.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> list, View v, int index,
					long arg3) {
				// TODO Auto-generated method stub
				if (atlIndexs.size() < 3) {
					atlIndexs.add(Integer.valueOf(index));
					((CalendarEditAltCell) v).cellData.calCellBackgroundColor = CELL_BACKGROUND_ORANGE;
					atlDateArr
							.add(((CalendarEditAltCell) v).cellData.calCellDate);
				} else {
					int id0 = atlIndexs.get(0);
					ATLCalCellData cellData = (ATLCalCellData) list
							.getAdapter().getItem(id0);
					cellData.calCellBackgroundColor = CELL_BACKGROUND_WHITE;
					atlIndexs.add(Integer.valueOf(index));

					((CalendarEditAltCell) v).cellData.calCellBackgroundColor = CELL_BACKGROUND_ORANGE;

					atlDateArr
							.add(((CalendarEditAltCell) v).cellData.calCellDate);
					atlDateArr.remove(0);
					atlIndexs.remove(0);

				}
				setAltTime();
				adapter.notifyDataSetChanged();
			}
		});
		// 2012-11-24 TAN: comment out
		/*
		 * listEvent.setOnItemSelectedListener(new OnItemSelectedListener() {
		 * 
		 * @Override public void onItemSelected(AdapterView<?> parent, View
		 * view, int position, long id) { // TODO Auto-generated method stub
		 * Toast.makeText(getBaseContext(), "click", Toast.LENGTH_LONG) .show();
		 * ATLCalCellData calCellData = CalendarEventSingleton
		 * .getInstance().getCalCellData(); switch (FLAG) { case PRE:
		 * CharSequence day = DateFormat.format("MMM d",
		 * calCellData.calCellStartDate); CharSequence hour =
		 * DateFormat.format("h", calCellData.calCellStartDate);
		 * preferDay.setText(day); preferHour.setText(hour); break;
		 * 
		 * case ALT2: CharSequence alt2DayText = DateFormat.format("MMM d",
		 * calCellData.calCellALT2Datetime); CharSequence alt2HourText =
		 * DateFormat.format("h", calCellData.calCellALT2Datetime);
		 * alt2Day.setText(alt2DayText); alt2Hour.setText(alt2HourText); break;
		 * 
		 * case ALT3: CharSequence alt3DayText = DateFormat.format("MMM d",
		 * calCellData.calCellALT3Datetime); CharSequence alt3HourText =
		 * DateFormat.format("h", calCellData.calCellALT3Datetime);
		 * alt3Day.setText(alt3DayText); alt3Hour.setText(alt3HourText); break;
		 * 
		 * default: break; } }
		 * 
		 * @Override public void onNothingSelected(AdapterView<?> arg0) { //
		 * TODO Auto-generated method stub
		 * 
		 * } });
		 */
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
					listEvent.smoothScrollToPositionFromTop(currentHourIndex - 1, 0);				}

			} else {
				int currentHourIndex = cal.get(Calendar.HOUR_OF_DAY);// 0 - 23
				if (currentHourIndex > 0) {
					listEvent.smoothScrollToPositionFromTop(currentHourIndex - 1, 0);				}
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
					listEvent.smoothScrollToPositionFromTop(currentHourIndex - 1, 0);				}
			}

		}
	}

	// 2012-12-11 TAN: END - Scroll day view ==========================

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == backArrowBtn) {

			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDate);
			Calendar calTemp = Calendar.getInstance();
			calTemp.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
					cal.get(Calendar.DAY_OF_MONTH) - 1, 0, 0, 0);
			currentDate = calTemp.getTime();
			aCalCellList.currentDateDidChanged(currentDate);
			dateDidChanged();
//			handleListViewPosition();

		} else if (v == nextArrowBtn) {

			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDate);
			Calendar calTemp = Calendar.getInstance();
			calTemp.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
					cal.get(Calendar.DAY_OF_MONTH) + 1, 0, 0, 0);
			currentDate = calTemp.getTime();
			aCalCellList.currentDateDidChanged(currentDate);
			dateDidChanged();
//			handleListViewPosition();
		} else if (v == alt2Img) {
			altCurrentActive = 1;
			setAltCurrentActive();
		} else if (v == alt3Img) {
			altCurrentActive = 2;
			setAltCurrentActive();
		} else if (v == preferImg) {
			altCurrentActive = 0;
			setAltCurrentActive();
		}
	}

	private void dateDidChanged() {
		// TODO Auto-generated method stub
		initATLTime();
		dateLable.setText(DateFormat
				.format(getResources().getString(
						R.string.calendar_day_view_day_format), currentDate));
		adapter.notifyDataSetChanged();
		handleListViewPosition();
	}

	private void initATLTime() {
		// TODO Auto-generated method stub
		int count = 0;
		atlDateArr.clear();
		atlIndexs.clear();

		Calendar currentDate = Calendar.getInstance();
		currentDate.setTimeInMillis(currentCellData.calCellStartDate.getTime());

		String hourFormat = getResources().getString(
				R.string.calendar_edit_event_when_hour_format);
		String dayFormat = getResources().getString(
				R.string.calendar_edit_event_when_day_format);

		preferDay.setText(DateFormat.format(dayFormat, currentCellData
				.getCalCellDate().getTime()));
		preferHour.setText(DateFormat
				.format(hourFormat, currentCellData.getCalCellDate())
				.toString().toUpperCase());
		preferDayNew = currentCellData.getCalCellDate();
		atlDateArr.add(preferDayNew);
		changeBgColorOfCell(preferDayNew);
		count = 1;
		if (currentCellData.getCalCellALT2Datetime() != null) {// For init value
			alt2Day.setText(DateFormat.format(dayFormat, currentCellData
					.getCalCellALT2Datetime().getTime()));
			alt2Hour.setText(DateFormat
					.format(hourFormat,
							currentCellData.getCalCellALT2Datetime())
					.toString().toUpperCase());
			alt2DayNew = currentCellData.getCalCellALT2Datetime();
			atlDateArr.add(alt2DayNew);
			changeBgColorOfCell(alt2DayNew);
			count = 2;
		}

		if (currentCellData.getCalCellALT3Datetime() != null) { // For init
																// value
			alt3Day.setText(DateFormat.format(dayFormat,
					currentCellData.getCalCellALT3Datetime()));
			alt3Hour.setText(DateFormat
					.format(hourFormat,
							currentCellData.getCalCellALT3Datetime())
					.toString().toUpperCase());
			alt3DayNew = currentCellData.getCalCellALT3Datetime();
			atlDateArr.add(alt3DayNew);
			changeBgColorOfCell(alt3DayNew);
			count = 3;
		}
		altCurrentActive = altCurrentActive < count ? altCurrentActive : count;// TAN
																				// :
																				// set
																				// alts
																				// Prefered->alt2->alt3
		setAltCurrentActive();
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
			Log.v("CalendarEditAltView",
					"aDateCal "
							+ DateFormat
									.format(getResources()
											.getString(
													R.string.calendar_edit_atl_time_format),
											aDateCal.getTime()));
			Log.v("CalendarEditAltView",
					"aCellCal "
							+ DateFormat
									.format(getResources()
											.getString(
													R.string.calendar_edit_atl_time_format),
											aCellCal.getTime()));
			String aCellCalString = (String) DateFormat.format(getResources()
					.getString(R.string.calendar_edit_atl_time_format),
					aCellCal.getTime());
			if (aDateCalString.equals(aCellCalString)) {
				cellData.calCellBackgroundColor = CELL_BACKGROUND_ORANGE;
				atlIndexs.add(Integer.valueOf(i));// TAN: To keep index of the
													// row
			}
		}
	}

	private void setAltTime() {

		altCurrentActive = altCurrentActive % 3;
		String dateString = ((String)DateFormat.format(
				getResources().getString(
						R.string.calendar_edit_event_when_day_format),
				atlDateArr.get(atlDateArr.size() - 1)));
		String hourString = ((String)DateFormat.format(
				getResources().getString(
						R.string.calendar_edit_event_when_hour_format),
				atlDateArr.get(atlDateArr.size() - 1))).toUpperCase();
		switch (altCurrentActive) {
		case 0:
			preferDayNew = atlDateArr.get(atlDateArr.size() - 1);
			preferDay.setText(dateString);
			preferHour.setText(hourString);
			currentCellData.setCalCellDate(preferDayNew);

			break;
		case 1:
			alt2DayNew = atlDateArr.get(atlDateArr.size() - 1);
			alt2Day.setText(dateString);
			alt2Hour.setText(hourString);
			currentCellData.setCalCellALT2Datetime((alt2DayNew));
			break;
		case 2:
			alt3DayNew = atlDateArr.get(atlDateArr.size() - 1);
			alt3Day.setText(dateString);
			alt3Hour.setText(hourString);
			currentCellData.setCalCellALT3Datetime((alt3DayNew));
			break;

		}
		altCurrentActive++;
		setAltCurrentActive();

	}

	private void setAltCurrentActive() {
		altCurrentActive = altCurrentActive % 3;
		switch (altCurrentActive) {
		case 0:
			preferImg
					.setImageResource(R.drawable.calendar_editevent_alttimes_select_btn);
			alt2Img.setImageResource(R.drawable.calendar_editevent_alttimes_unselect_btn);
			alt3Img.setImageResource(R.drawable.calendar_editevent_alttimes_unselect_btn);
			break;
		case 1:
			preferImg
					.setImageResource(R.drawable.calendar_editevent_alttimes_unselect_btn);
			alt2Img.setImageResource(R.drawable.calendar_editevent_alttimes_select_btn);
			alt3Img.setImageResource(R.drawable.calendar_editevent_alttimes_unselect_btn);
			break;
		case 2:
			preferImg
					.setImageResource(R.drawable.calendar_editevent_alttimes_unselect_btn);
			alt2Img.setImageResource(R.drawable.calendar_editevent_alttimes_unselect_btn);
			alt3Img.setImageResource(R.drawable.calendar_editevent_alttimes_select_btn);
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {

		case ATLCalendarIntentKeys.CALL_FROM_CALENDAR_ALT_VIEW:
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
					dateDidChanged();
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

		if (lastShowOffHourIndex  != -1) {
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
					CalendarEditAltCell cellOld = (CalendarEditAltCell) listEvent
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
}
