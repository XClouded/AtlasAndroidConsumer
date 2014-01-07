//  ==================================================================================================================
//  CalendarEditView.java
//  ATLAS
//  Copyright (c) 2012 Atlas Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-11-25 TAN:    Handle passing data between CalendarEditView & CalendarEditAltView
//  2012-11-24 TAN:    Add WheelView and assigned data 
//  2012-11-10 NGHIA:  Created
//  ==================================================================================================================

package com.atlasapp.section_calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.atlasapp.atlas_database.AtlasServerConnect;
import com.atlasapp.atlas_database.EventController;
import com.atlasapp.atlas_database.EventControllerCallBackInterface;
import com.atlasapp.atlas_database.EventProperties;
import com.atlasapp.atlas_database.ItemUserProperties;
import com.atlasapp.atlas_database.DatabaseConstants.ACTION;
import com.atlasapp.atlas_database.DatabaseConstants.EVENT_STATUS;
import com.atlasapp.atlas_database.DatabaseConstants.EVENT_TYPE;
import com.atlasapp.common.ATLConstants.AlertType;
import com.atlasapp.common.ATLConstants.EventResponseType;
import com.atlasapp.common.ATLAlertDialogUtils;
import com.atlasapp.common.ATLAnimationUtils;
import com.atlasapp.common.AtlasAndroidUser;
import com.atlasapp.common.CalendarUtilities;
import com.atlasapp.model.ATLCalendarModel;
import com.atlasapp.model.ATLContactModel;
import com.atlasapp.section_alerts.ATLAlertWebAccess;
import com.atlasapp.section_alerts.ATLAlertWebAccessCallBackInterface;
import com.atlasapp.section_appentry.R;
import com.atlasapp.section_contacts.ATLContactListActivity;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * @author nghia & sharon
 * 
 */
public class CalendarEditView extends Activity implements OnClickListener,
		OnWheelChangedListener, OnWheelScrollListener,
		ATLAlertWebAccessCallBackInterface, EventControllerCallBackInterface {

	private EventController eventController;
	/**
	 * 
	 */
	ATLCalCellData currentCellData = new ATLCalCellData();
	Calendar dateTime = Calendar.getInstance();
	Context mContext;
	EditText whatText;
	EditText whereText;
	EditText noteEditText;
	ScrollView contentScrollView;
	// 2012-12-12 TAN: Comment out to change view as iPhone
	// TextView whenDay;
	// TextView whenHour;
	TextView preferDay;
	TextView preferHour;
	TextView alt1Day;
	TextView alt1Hour;
	TextView alt2Day;
	TextView alt2Hour;

	private WheelView duration_Hours;
	private WheelView duration_Mins;
	private WheelView reminder_1;
	private WheelView reminder_2;
	private WheelView calList;

	String durationMinsData[] = new String[] { "0", "5", "10", "15", "20",
			"25", "30", "35", "40", "45", "50", "55" };

	String reminderData[] = new String[] { "Off", "0", "5", "15", "30", "1",
			"2", "1", "2", "1", "1" };
	private ImageView preferImg;
	private ImageView alt2Img;
	private ImageView alt3Img;

	public static final String ALT_NUMBER = "altNum";
	protected static final int ALT_NUMBER_0 = 0;
	protected static final int ALT_NUMBER_1 = 1;
	protected static final int ALT_NUMBER_2 = 2;

	boolean isBlankCell = false;
	private ArrayList<ATLCalendarModel> calListModel;
	private TextView remider_1_unit_text;
	private TextView remider_2_unit_text;

	// //Sharon addings
	private static final int CONTACT_PICKER_RESULT = 0;
	private static ATLContactModel invitee;
	private TextView assigneeName;
	private static String eventTitle = "";
	private static String eventLocation = "";
	private int eventDuration = 0;
	private AtlasServerConnect parseConnect;
	private Date datePref, dateAlt1, dateAlt2;
	private ImageButton saveBtn;
	private ImageButton cancelBtn;
	private View navBarView;
	private LinearLayout whoTableRow;
	private ProgressDialog progressBar;

	// ///

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_edit_2);

		mContext = this;
		/// SHARON ADDINGS
		eventController = new EventController();
		/// initialize CalendarEditView to listen to call back methods
		/// from event controller....
		eventController.eventControllerCallBackListener = this;
		///////
		
		progressBar = new ProgressDialog(this);// 2013-02-27: TAN add
		
		currentCellData = CalendarEventSingleton.getInstance().getCalCellData();
		dateTime.setTimeInMillis(currentCellData.calCellStartDate.getTime());

		whatText = (EditText) findViewById(R.id.calendar_edit_event_what);
		whatText.setText(currentCellData.calCellTitle);

		whereText = (EditText) findViewById(R.id.calendar_edit_event_where);
		whereText.setText(currentCellData.calCellLocation);
		// 2012-12-12 TAN: Comment out to change view as iPhone
		// whenDay = (TextView) findViewById(R.id.whenDay);
		// whenHour = (TextView) findViewById(R.id.whenHour);
		preferDay = (TextView) findViewById(R.id.preferDay);
		preferHour = (TextView) findViewById(R.id.preferHour);
		alt1Day = (TextView) findViewById(R.id.alt1Day);
		alt1Hour = (TextView) findViewById(R.id.alt1Hour);
		alt2Day = (TextView) findViewById(R.id.alt2Day);
		alt2Hour = (TextView) findViewById(R.id.alt2Hour);
		assigneeName = (TextView) findViewById(R.id.calendar_invite_who);
		
		contentScrollView = (ScrollView) findViewById(R.id.cal_edit_content_scroll_view);
		noteEditText = (EditText) findViewById(R.id.calendar_edit_event_notes_edit);
		
		
		// 2012-12-03 TAN: Handle edit or new
		// event=======================================================================

		Intent i = getIntent();
		isBlankCell = i.getExtras().getBoolean(CalendarCell.CELL_IS_BLANK);
		TextView titleText = (TextView) findViewById(R.id.titleTextView);
		navBarView = (View) findViewById(R.id.topMenuBar);
		cancelBtn = (ImageButton) findViewById(R.id.cancelBtn);
		saveBtn = (ImageButton) findViewById(R.id.saveBtn);
		if (isBlankCell) {
			// New an event
			titleText.setText(getResources().getString(
					R.string.calendar_edit_title_add_event));
			whatText.requestFocus();
			whatText.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					ATLAnimationUtils.showKeyboard(mContext);
				}
			}, 300);
			
		} else {
			// Edit event
			titleText.setText(getResources().getString(
					R.string.calendar_edit_title));
			navBarView.setBackgroundResource(R.drawable.navbar);
			saveBtn.setImageResource(R.drawable.btn_done);
			cancelBtn.setImageResource(R.drawable.btn_cancel);
		}
		// END
		// ==========================================================================================================
		
		noteEditText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				contentScrollView.postDelayed(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						contentScrollView.fullScroll(ScrollView.FOCUS_DOWN);
					}
				}, 100);
			}
		});
		noteEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus){
					contentScrollView.postDelayed(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							contentScrollView.fullScroll(ScrollView.FOCUS_DOWN);
						}
					}, 300);
				}
			}
		});
		
		// 2013-01-06 NGHIA: comment when merge with SHARON, please help me
		// check xml file and comment out this line
		updateAltTime();

		setUpWheelView();

		
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				disbleAllButton();
				finish();
				overridePendingTransition(R.anim.stand_by, R.anim.out_to_bottom);
			}
		});
		
		saveBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO save data before finish
				disbleAllButton();
				String title = whatText.getText().toString().trim();
				if(title != null && title.length()>0){
					
					updateCellData();
					updateAltTime();
					
					saveOnDB() ;
//					if (isBlankCell) { // 2012-12-03 TAN: handle new and edit
//	
//						CalendarUtilities.addCellData(currentCellData, mContext);
//					} else {
//						// 2012-12-04 TAN: Handle update
//						// event==================================================
//	
//						CalendarUtilities.updateCellData(currentCellData, mContext);
//	
//						// end
//						// ================================================================================
//					}
				}
				else{
					String msg = "Please fill in event title";
					String alertTitle = "Alert";
					ATLAlertDialogUtils.showAlert(mContext, alertTitle, msg);
//						Toast.makeText(mContext, "Please fill in task title", Toast.LENGTH_SHORT).show();
					enableAllButton();
					return;
				}
//				saveOnDB() ;
				//finish();
			}
		});

		// Who TableRow
		whoTableRow = (LinearLayout) findViewById(R.id.whoTableRow);
		whoTableRow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				 disbleAllButton();
				 Intent intent = new Intent(CalendarEditView.this, ATLContactListActivity.class);
				 intent.putExtra("mode", ATLContactListActivity.MODE_PICKER);
				 intent.putExtra("from", "event");

				 startActivityForResult(intent, CONTACT_PICKER_RESULT);

			}

		}); 
		// 2012-12-12 TAN: Comment out to change view as iPhone
		// RelativeLayout whenLayout = (RelativeLayout)
		// findViewById(R.id.whenLayout);
		// whenLayout.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// chooseTime();
		// chooseDate();
		// }
		//
		// });

		// ALT Time TableRow
		// TableRow altTableRow = (TableRow) findViewById(R.id.altTableRow);
		// altTableRow.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Intent i = new Intent(getBaseContext(),
		// CalendarEditAltView.class);
		// startActivityForResult(i, GET_ATLS_TIME);
		// }
		// });

		preferImg = (ImageView) findViewById(R.id.preferImg);
		preferImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 2013-01-11 TAN: Comment out for hanle the new layout # START
				// Intent i = new Intent(getBaseContext(),
				// CalendarEditAltView.class);
				// i.putExtra(ALT_NUMBER, ALT_NUMBER_0);
				// startActivityForResult(i,
				// ATLCalendarIntentKeys.GET_ATLS_TIME);
				// 2013-01-11 TAN: Comment out for hanle the new layout # END
				disbleAllButton();
				updateCellData();
				CalendarEventSingleton.getInstance().setCalCellData(
						currentCellData);
				Intent iMove = new Intent(mContext, CalendarMoveEvent.class);
				iMove.putExtra(CalendarCell.CELL_IS_BLANK,
						currentCellData.isBlank);
				((Activity) mContext).startActivityForResult(iMove,
						ATLCalendarIntentKeys.CALL_FROM_CALENDAR_EDIT);
				overridePendingTransition(R.anim.in_from_bottom, R.anim.stand_by);
			}
		});

		alt2Img = (ImageView) findViewById(R.id.alt1);
		alt2Img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				disbleAllButton();
				Intent i = new Intent(getBaseContext(),
						CalendarEditAltView.class);
				i.putExtra(ALT_NUMBER, ALT_NUMBER_1);

				startActivityForResult(i, ATLCalendarIntentKeys.GET_ATLS_TIME);
				overridePendingTransition(R.anim.in_from_bottom, R.anim.stand_by);
			}
		});

		alt3Img = (ImageView) findViewById(R.id.alt2);
		alt3Img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				disbleAllButton();
				Intent i = new Intent(getBaseContext(),
						CalendarEditAltView.class);
				i.putExtra(ALT_NUMBER, ALT_NUMBER_2);

				startActivityForResult(i, ATLCalendarIntentKeys.GET_ATLS_TIME);
				overridePendingTransition(R.anim.in_from_bottom, R.anim.stand_by);
			}
		});
		// hours = (WheelView) findViewById(R.id.calendar_edit_event_hours);
		// hours.setViewAdapter(new NumericWheelAdapter(this, 1, 12));
		//
		// mins = (WheelView) findViewById(R.id.calendar_edit_event_minutes);
		// mins.setViewAdapter(new NumericWheelAdapter(this, 0, 59, "%02d"));
		// mins.setCyclic(true);

		// String reminderNames[] = new String[] { "Off", "0min", "5min",
		// "15min",
		// "30min", "1hour", "2hours" };

		// reminders = (WheelView)
		// findViewById(R.id.calendar_edit_event_reminder);
		// reminders.setViewAdapter(new ArrayWheelAdapter<String>(this,
		// reminderNames));

		String[] calNames = ATLCalendarManager.getCalendarName(this);
		calList = (WheelView) findViewById(R.id.calendar_edit_event_names);
		calList.setViewAdapter(new ArrayWheelAdapter<String>(this, calNames));
		calListModel = ATLCalendarStore.loadActiveCalendarList(this);
		calList.setViewAdapter(new ATLCalendarScrollListAdapter(calListModel,
				this));
		calList.addChangingListener(this);

		int index = 0;
		for (ATLCalendarModel calModel : calListModel) {
			if (currentCellData.calendarId == -1) {// Default value
				calList.setCurrentItem(0);
				currentCellData.calendarId = calModel.id;
				currentCellData.calCellCalendarColor = calModel.color;
				break;  
			} else if (currentCellData.calendarId == calModel.id) {
				calList.setCurrentItem(index);
				currentCellData.calendarId = calModel.id;
				currentCellData.calCellCalendarColor = calModel.color;
				break;
			}
			index++;
		}
		// ///SHARON EDIT
	
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			// message = extras.getString("message");
			//
			invitee = extras
					.getParcelable("com.atlasapp.model.ATLContactModel");

			// calList.setViewAdapter(new ArrayWheelAdapter<String>(this,
			// calNames));
			// calList.addChangingListener(this);
			if (invitee != null)
				assigneeName.setText(invitee.displayName());
			// // message = "Hello "+invitee.getFirstname()+" ! ";
			// // if (!message.equals(""))
			// // alertUser("", message);
			//
			updateCellData();
		}

	}
	
	private void disbleAllButton(){
		saveBtn.setClickable(false);
		cancelBtn.setClickable(false);
		preferImg.setClickable(false);
		alt2Img.setClickable(false);
		alt3Img.setClickable(false);
		whoTableRow.setClickable(false);
	}
	
	private void enableAllButton(){
		saveBtn.setClickable(true);
		cancelBtn.setClickable(true);
		preferImg.setClickable(true);
		alt2Img.setClickable(true);
		alt3Img.setClickable(true);
		whoTableRow.setClickable(true);
	}
	
	
	@Override
	protected void onResume(){
		super.onResume();
		updateAltTime();
		enableAllButton();
	}

	@Override
	protected void onPause(){
		super.onPause();
		ATLAnimationUtils.hideKeyboard(mContext, whatText);
		ATLAnimationUtils.hideKeyboard(mContext, whereText);
		ATLAnimationUtils.hideKeyboard(mContext, noteEditText);
	}
	private void setUpWheelView() {
		// TODO Auto-generated method stub
		duration_Hours = (WheelView) findViewById(R.id.calendar_edit_event_hours);
		duration_Mins = (WheelView) findViewById(R.id.calendar_edit_event_minutes);
		reminder_1 = (WheelView) findViewById(R.id.calendar_edit_event_reminder_hour);
		reminder_2 = (WheelView) findViewById(R.id.calendar_edit_event_reminder_minutes);
		remider_1_unit_text = (TextView) findViewById(R.id.calendar_edit_remider_alarm1_text);
		remider_2_unit_text = (TextView) findViewById(R.id.calendar_edit_remider_alarm2_text);
		duration_Hours.setViewAdapter(new NumericWheelAdapter(this, 0, 23,
				"%d hr"));
		// duration_Hours.setCyclic(true);
		duration_Mins.setViewAdapter(new ArrayWheelAdapter<String>(this,
				durationMinsData));
		// duration_Mins.setCyclic(true);

		reminder_1.setViewAdapter(new ArrayWheelAdapter<String>(this,
				reminderData));
		reminder_2.setViewAdapter(new ArrayWheelAdapter<String>(this,
				reminderData));
		reminder_1.addScrollingListener(this);
		reminder_2.addScrollingListener(this);

//		int durationMinutes = currentCellData.getCalCellDurationMinutes();
		duration_Hours.setCurrentItem(currentCellData
				.getCalCellDurationMinutes() / 60);
		duration_Mins.setCurrentItem((currentCellData
				.getCalCellDurationMinutes() % 60) / 5);
		if(isBlankCell){// 2013-01-21: TAN: default data
			duration_Hours.setCurrentItem(0);
			duration_Mins.setCurrentItem(3);
		}
		reminder_1.setCurrentItem(3);
		remider_1_unit_text.setText("min"); // 2013-01-21: TAN: James request fake data
		
		reminder_2.setCurrentItem(0);
	}
	
	/**
	 * Scan the currentCellData  for creating the current Event properties 
	 * AFTER all required fields were filled by the user (inviter)
	 * @return Array List of EventProperties for each of the Event alternative times
	 */
	private ArrayList<EventProperties> setEvent()
	{
		ArrayList<EventProperties> event = null;
		
		if (currentCellData!=null && currentCellData.calCellDate!=null)
		{
			EventProperties eventAlt ;
			Date endDateTime,startDateTime;
			event = new ArrayList<EventProperties>();
			//// common events properties
			String title = currentCellData.calCellTitle;
			String location = currentCellData.calCellLocation;
			String notes = currentCellData.calCellNotes;
			int duration  = currentCellData.calCellDurationMinutes;
			String inviterId = AtlasAndroidUser.getParseUserID();
			
			title = (title!=null)? title:"";
			location = (location!=null)? location:"";
			notes = (notes!=null)? notes:"";
			
			////////
			startDateTime = currentCellData.calCellDate;
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentCellData.calCellDate);
			cal.add(Calendar.MINUTE, duration);
			endDateTime = cal.getTime();
			/////
			eventAlt = new EventProperties("","",title, location, notes,
					startDateTime, endDateTime, duration,0,inviterId ,
			        "",EVENT_STATUS.PENDING , ACTION.SAVE 
			        , true,EVENT_TYPE.LUNCH);
			
			event.add(eventAlt);
			////////
			if (currentCellData.calCellALT2Datetime!=null)
			{
				startDateTime = currentCellData.calCellALT2Datetime;
				cal = Calendar.getInstance();
				cal.setTime(currentCellData.calCellALT2Datetime);
				cal.add(Calendar.MINUTE, duration);
				endDateTime = cal.getTime();
				/////
				eventAlt = new EventProperties("","",title, location, notes,
						startDateTime, endDateTime, duration,1,inviterId ,
				        "",EVENT_STATUS.PENDING , ACTION.SAVE 
				        , false,EVENT_TYPE.LUNCH);
				
				event.add(eventAlt);
				////////
				if (currentCellData.calCellALT3Datetime!=null)
				{
					startDateTime = currentCellData.calCellALT3Datetime;
					cal = Calendar.getInstance();
					cal.setTime(currentCellData.calCellALT3Datetime);
					cal.add(Calendar.MINUTE, duration);
					endDateTime = cal.getTime();
					/////
					eventAlt = new EventProperties("","",title, location, notes,
							startDateTime, endDateTime, duration,2,inviterId ,
					        "",EVENT_STATUS.PENDING , ACTION.SAVE 
					        , false,EVENT_TYPE.LUNCH);
					
					event.add(eventAlt);
				}
				
				
			}
		}
		
		
		return event;
	}
	/**
	 * Creates a calendar event invite 
	 * gets respond on createCalendarEventCallBack method
	 * author: sharon 
	 */
	private void createEventCalendar()
	{
		ArrayList<EventProperties> eventProperties = setEvent();
		ArrayList<ATLContactModel> invitees = new ArrayList<ATLContactModel>();
		if (invitee!=null)
		{
			invitees.add(invitee);
		}
		if (eventProperties!=null && eventProperties.size()>0 && invitees.size()>0){
			/// CALL BACK FROM createCalendarEventCallBack method
			ATLAnimationUtils.showProgressBar(progressBar, "Sending...");
			eventController.createCalendarEvent(eventProperties, invitees);
		}
		else
		{
			////// CANT CREATE A VALID EVENT.....
		}
	}
	@Override
	public void createCalendarEventCallBack(boolean success) {
		if (success)
		{
			ATLAnimationUtils.dismissProgressBar(progressBar);
//			
//			alertUser("Calendar invite",
//					"Your event invite to " + invitee.getFirstname()
//							+ " was sent");
			Toast.makeText(mContext.getApplicationContext(), "You have been invited " + invitee.getFirstname() 
					+" to \"" + currentCellData.calCellTitle +"\"", Toast.LENGTH_LONG).show();
			//================================================================
			// 2013-02-27 TAN: handle call back, save event to calendar #START
			//================================================================
			if (isBlankCell) { 
				
				CalendarUtilities.addCellData(currentCellData, mContext);
			} else {

				CalendarUtilities.updateCellData(currentCellData, mContext);
			}
			//================================================================
			// 2013-02-27 TAN: handle call back, save event to calendar #END
			//================================================================
			
			
			finish();
			((Activity)mContext).overridePendingTransition(R.anim.stand_by, R.anim.out_to_bottom);
			
		}
		else
		{
			Toast.makeText(mContext.getApplicationContext(), "Your invitation could not be send!", Toast.LENGTH_LONG).show();

		}
		
	}
	/**
	 * 
	 * @author sharon nachum
	 */
	private void saveOnDB() {
		parseConnect = AtlasServerConnect.getSingletonObject(this);
		SimpleDateFormat lv_formatter;
		lv_formatter = new SimpleDateFormat("yyyy:DD:MM HH:mm:ss");
		lv_formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

		datePref = currentCellData.getCalCellDate();
		dateAlt1 = currentCellData.getCalCellALT2Datetime();
		dateAlt2 = currentCellData.getCalCellALT3Datetime();
		Log.v("Date", "pref"+lv_formatter.format(datePref));
		// Date anotherDate = new Date(taskDate.getTime() + (3600 * 1000));
		String prefdateFormateInUTC = lv_formatter.format(datePref);
		String altOnefdateFormateInUTC = "";
		String altTwofdateFormateInUTC = "";
		if (dateAlt1 != null) {// 2013-01-08 TAN: fix crash when null pointer
			altOnefdateFormateInUTC = lv_formatter.format(dateAlt1);
		}
		if (dateAlt2 != null) {// 2013-01-08 TAN: fix crash when null pointer
			altTwofdateFormateInUTC = lv_formatter.format(dateAlt2);
		}
		currentCellData.eventResponseType_CellData = EventResponseType.eventResponseType_InvitesPending;

		if (invitee != null) {// 2013-01-08 TAN: fix crash when null pointer
			createEventCalendar();
		} else {// 2013-01-08 TAN: fix crash when null pointer
			if (isBlankCell) { // 2013-02-27 TAN: handle new and edit
				
				CalendarUtilities.addCellData(currentCellData, mContext);
			} else {
				//  2013-02-27 TAN: handle update
				// event ==================================================

				CalendarUtilities.updateCellData(currentCellData, mContext);

				// end  ===================================================
			}

			finish();
			overridePendingTransition(R.anim.stand_by, R.anim.out_to_bottom);
		}

	}

	@SuppressWarnings("deprecation")
	private void alertUser(String messageTitle, String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(CalendarEditView.this)
				.create();

		// Setting Dialog Title
		alertDialog.setTitle(messageTitle);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		// Setting Icon to Dialog
		// alertDialog.setIcon(R.drawable.tick);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				finish();
				((Activity)mContext).overridePendingTransition(R.anim.stand_by, R.anim.out_to_bottom);
				// Write your code here to execute after dialog closed
				// Toast.makeText(getApplicationContext(), "You clicked on OK",
				// Toast.LENGTH_SHORT).show();

			}
		});
		alertDialog.setCanceledOnTouchOutside(false);  //2013-02-01 TAN: avoid tap outsite to dismiss

		// Showing Alert Message
		alertDialog.show();
	}
	
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		// /SHARON ADDING (retrieving the contact picked from the user picker
		// friends list)
		case ATLContactListActivity.RESULT_FROM_CONTACTLIST: {

			ATLContactModel contact = (ATLContactModel) data.getExtras()
					.getParcelable("com.atlasapp.model.ATLContactModel");
			invitee = contact;
			currentCellData.attendee = contact; // NGHIA add to currentCellData
			//Log.v("Contact",
//					"name " + contact.displayName() + "Email "
//							+ contact.getEmailAddress() + "Phone"
//							+ contact.getPhoneNumber());
			assigneeName.setText(contact.displayName());
			ImageView imageContact = (ImageView) findViewById(R.id.imageContact);
			imageContact.setImageBitmap(contact.getImage());

		}
			break;

		// //END SHARON ADDINGS
		case ATLCalendarIntentKeys.RESULT_FROM_ALT_VIEW: {
			String time0 = data.getStringExtra("alt0");
			String time1 = data.getStringExtra("alt1");
			String time2 = data.getStringExtra("alt2");

			// Calendar cal = Calendar.getInstance();
			SimpleDateFormat sd = new SimpleDateFormat(getResources()
					.getString(R.string.calendar_edit_atl_time_format));
			// sd.setTimeZone(TimeZone.getDefault());
			Date date0 = new Date();
			Date date1 = new Date();
			Date date2 = new Date();
			try {
				if (time0 != null) {
					date0 = sd.parse(time0);
					currentCellData.setCalCellDate(date0);
					currentCellData.setCalCellStartDate(date0);
					currentCellData.calCellPreferDateTime = date0;
				}
				if (time1 != null) {
					date1 = sd.parse(time1);
					currentCellData.setCalCellALT2Datetime(date1);
				}
				if (time2 != null) {
					date2 = sd.parse(time2);
					currentCellData.setCalCellALT3Datetime(date2);
				}

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			updateAltTime();
			Log.v("CalendarEditView", time0 + " === " + time1 + " === " + time2);
		}
			break;
		case ATLCalendarIntentKeys.RESULT_FROM_MOVE_EVENT_VIEW: {
			long returnDateLong = data.getExtras().getLong(
					ATLCalendarIntentKeys.KEY_FROM_MOVE_EVENT_VIEW);
			Date tempDate = new Date(returnDateLong);
			if (tempDate != null) {
				currentCellData.setCalCellDate(tempDate);
				currentCellData.setCalCellStartDate(tempDate);
				currentCellData.calCellPreferDateTime = tempDate;
			}
			updateAltTime();
		}
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	private void chooseDate() {
		// TODO Auto-generated method stub
		new DatePickerDialog(this, dateListener, dateTime.get(Calendar.YEAR),
				dateTime.get(Calendar.MONTH),
				dateTime.get(Calendar.DAY_OF_MONTH)).show();

	}

	public void chooseTime() {
		new TimePickerDialog(this, timeListener,
				dateTime.get(Calendar.HOUR_OF_DAY),
				dateTime.get(Calendar.MINUTE), true).show();
	}

	private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			dateTime.set(Calendar.YEAR, year);
			dateTime.set(Calendar.MONTH, monthOfYear);
			dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			currentCellData.calCellStartDate
					.setTime(dateTime.getTimeInMillis());
			updateAltTime();
		}
	};

	private TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
			dateTime.set(Calendar.MINUTE, minute);
			currentCellData.calCellStartDate
					.setTime(dateTime.getTimeInMillis());
			updateAltTime();
		}
	};

	private void updateAltTime() {

		Calendar currentDate = Calendar.getInstance();
		currentDate.setTimeInMillis(currentCellData.calCellStartDate.getTime());

		String hourFormat = getResources().getString(
				R.string.calendar_edit_event_when_hour_format);
		String dayFormat = getResources().getString(
				R.string.calendar_edit_event_when_day_format);
		if (currentCellData.calCellPreferDateTime == null) {
			currentCellData.calCellPreferDateTime = currentCellData.calCellDate;
		}
		preferDay.setText(DateFormat.format(dayFormat,
				currentCellData.calCellPreferDateTime));
		preferHour.setText(DateFormat
				.format(hourFormat, currentCellData.calCellPreferDateTime)
				.toString().toUpperCase());
		if (currentCellData.getCalCellALT2Datetime() != null) {// For init value
			alt1Day.setText(DateFormat.format(dayFormat, currentCellData
					.getCalCellALT2Datetime().getTime()));
			alt1Hour.setText(DateFormat
					.format(hourFormat,
							currentCellData.getCalCellALT2Datetime().getTime())
					.toString().toUpperCase());
		}

		if (currentCellData.getCalCellALT3Datetime() != null) { // For init
																// value
			alt2Day.setText(DateFormat.format(dayFormat, currentCellData
					.getCalCellALT3Datetime().getTime()));
			alt2Hour.setText(DateFormat
					.format(hourFormat,
							currentCellData.getCalCellALT3Datetime().getTime())
					.toString().toUpperCase());
		}

	}

	private void updateCellData() {
		// TODO Auto-generated method stub

		// currentCellData.setCalCellTitle(whatText.getText().toString());
		// currentCellData.setCalCellLocation(whereText.getText().toString());
		// 2012-12-05 TAN: Handle update
		// event==================================================
		String title = whatText.getText().toString();
		String where = whereText.getText().toString();

		int duration = duration_Hours.getCurrentItem() * 60
				+ duration_Mins.getCurrentItem() * 5;

		eventTitle = whatText.getText().toString();
		eventLocation = whereText.getText().toString();

		eventDuration = duration_Hours.getCurrentItem() * 60
				+ duration_Mins.getCurrentItem() * 5;

		// long reminder1 = 0;
		// long reminder2 = 0;
		int calendarId = ATLCalendarManager.getCalendarIds(mContext)[calList
				.getCurrentItem()];
		currentCellData.setCalCellDurationMinutes(eventDuration);
		currentCellData.setCalCellStartDate(currentCellData.getCalCellDate());
		currentCellData.setCalCellEndDate(new Date(currentCellData
				.getCalCellStartDate().getTime() + eventDuration * 60 * 1000));
		currentCellData.setCalCellTitle(eventTitle);
		currentCellData.setCalCellLocation(eventLocation);
		currentCellData.calendarId = calendarId;

		// end
		// ================================================================================
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == calList) {
			currentCellData.calendarId = calListModel.get(newValue).id;
			currentCellData.calCellCalendarColor = calListModel.get(newValue).color;
		} else if (wheel == duration_Hours) {

		} else if (wheel == duration_Mins) {

		}
	}

	@Override
	public void onScrollingStarted(WheelView wheel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollingFinished(WheelView wheel) {
		// TODO Auto-generated method stub
		int newValue = wheel.getCurrentItem();
		if (wheel == reminder_1) {
			switch (newValue) {
			case 0:
				remider_1_unit_text.setText("");
				break;
			case 1:
			case 2:
			case 3:
			case 4:
				remider_1_unit_text.setText("min");
				break;
			case 5:
				remider_1_unit_text.setText("hr");
				break;
			case 6:
				remider_1_unit_text.setText("hrs");
				break;
			case 7:
				remider_1_unit_text.setText("day");
				break;
			case 8:
				remider_1_unit_text.setText("days");
				break;
			case 9:
				remider_1_unit_text.setText("wk");
				break;
			case 10:
				remider_1_unit_text.setText("mo");
				break;
			}
		} else if (wheel == reminder_2) {
			switch (newValue) {
			case 0:
				remider_2_unit_text.setText("");
				break;
			case 1:
			case 2:
			case 3:
			case 4:
				remider_2_unit_text.setText("min");
				break;
			case 5:
				remider_2_unit_text.setText("hr");
				break;
			case 6:
				remider_2_unit_text.setText("hrs");
				break;
			case 7:
				remider_2_unit_text.setText("day");
				break;
			case 8:
				remider_2_unit_text.setText("days");
				break;
			case 9:
				remider_2_unit_text.setText("wk");
				break;
			case 10:
				remider_2_unit_text.setText("mo");
				break;
			}
		}

	}

	@Override
	public void didGetDataFinish(Object data, int alertType, String result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void didPostDataFinish(Object data, int alertType, String result) {
		// TODO Auto-generated method stub
		Log.v("didPostDataFinish", "didPostDataFinish");
	}

	@Override
	public void didGetEventList(List<ParseObject> events, int alertType,
			String result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void didGetSenderName() {
		// TODO Auto-generated method stub

	}

	

	@Override
	public void getAllUserAnEventCallBack(
			HashMap<String, ArrayList<EventProperties>> userEvents,
			HashMap<String, ArrayList<String>> eventsMembers, boolean success) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void getAllUserEventsCallBack(
//			HashMap<String, ArrayList<EventProperties>> allEventsRetrievedFromItemUser,
//			boolean b) {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public void getEventByPrimaryWebEventIdCallBack(boolean success,
			ArrayList<EventProperties> event,
			ArrayList<ItemUserProperties> invitees) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void respondToEventInviteCallBack(boolean success) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void bookEventCallBack(boolean success) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAllUserEventsCallBack(
			HashMap<String, ArrayList<EventProperties>> allEventsRetrievedFromItemUser,
			ArrayList<ItemUserProperties> allUserItemUsersPropertiesRefreshedList,
			boolean success) {
		// TODO Auto-generated method stub
		
	}
}
