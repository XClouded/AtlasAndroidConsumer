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

package atlasapp.section_calendar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import atlasapp.database.AtlasServerConnect;
import atlasapp.database.EventController;
import atlasapp.database.EventControllerCallBackInterface;
import atlasapp.database.EventProperties;
import atlasapp.database.ItemUserProperties;
import atlasapp.database.DatabaseConstants.ACTION;
import atlasapp.database.DatabaseConstants.EVENT_STATUS;
import atlasapp.database.DatabaseConstants.EVENT_TYPE;
import atlasapp.common.ATLConstants.AlertType;
import atlasapp.common.ATLConstants.EventResponseType;
import atlasapp.common.ATLAlertDialogUtils;
import atlasapp.common.ATLAnimationUtils;
import atlasapp.common.ATLUser;
import atlasapp.common.CalendarUtilities;
import atlasapp.model.ATLCalendarModel;
import atlasapp.model.ATLContactModel;
import atlasapp.section_alerts.ATLAlertWebAccess;
import atlasapp.section_alerts.ATLAlertWebAccessCallBackInterface;
import atlasapp.section_appentry.R;

import atlasapp.section_contacts.ATLContactListActivity;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * @author nghia & sharon
 * 
 */
public class CalendarEditView extends Activity implements OnClickListener,
		OnWheelChangedListener, OnWheelScrollListener,OnItemClickListener,
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
	
	
//	private HashMap<String,ATLContactModel> inviteePicked;
	
//	private static ATLContactModel invitee;
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
	private ArrayList<ATLContactModel> inviteeContactModelArray;

	// ///
	private static final String LOG_TAG = "ExampleApp";
    
	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/queryautocomplete";
	private static final String OUT_JSON = "/json";

	private static final String API_KEY = "AIzaSyCvDY7GLIk8qXzsbLSWibIIhim2rmPVewI";
	
	
	
	///////
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
				Intent intent = new Intent(getBaseContext(), CalendarDayView.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//               		//intent.putExtra("EXTRA_SESSION_ID", sessionId);
                startActivity(intent);
                overridePendingTransition(R.anim.enter,R.anim.leave);
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
					if (isBlankCell) { // 2012-12-03 TAN: handle new and edit
	
//						CalendarUtilities.addCellData(currentCellData, mContext);mh
					} else {
						// 2012-12-04 TAN: Handle update
						// event==================================================
	
//						CalendarUtilities.updateCellData(currentCellData, mContext);
	
						// end
						// ================================================================================
					}
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
//	
//		Bundle extras = getIntent().getExtras();
//		if (extras != null) {
//			// message = extras.getString("message");
//			//
////			invitee = extras
////					.getParcelable("com.atlasapp.model.ATLContactModel");
////			
//			
//			inviteeIds = (ArrayList<String>) extras.getStringArrayList("arrayList");
////			 inviteeIds = extras
////					.getParcelable("arrayList");
//			
//			if (inviteeIds!=null)
//			{
//				Toast.makeText(getBaseContext(),inviteeIds.size() +" Contacts picked...", 
//		                Toast.LENGTH_SHORT).show();
//			}
//
//			// calList.setViewAdapter(new ArrayWheelAdapter<String>(this,
//			// calNames));
//			// calList.addChangingListener(this);
//			if (invitee != null)
//				assigneeName.setText(invitee.displayName());
//			// // message = "Hello "+invitee.getFirstname()+" ! ";
//			// // if (!message.equals(""))
//			// // alertUser("", message);
//			//
//			updateCellData();
//		}
		
		
		 ATLContactListActivity.setNewInviteeList();
		 
		 
		 
		 ///////////////
		 /// LOCATION SENSOR
//		 AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
//		 autoCompView.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));
//		 
//		 autoCompView.setOnItemClickListener(this);
		 ////////////////

	}
	
	private ArrayList<String> autocomplete(String input) {
	    ArrayList<String> resultList = null;
	    
	    HttpURLConnection conn = null;
	    StringBuilder jsonResults = new StringBuilder();
	    try {
	        StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
	        sb.append("?sensor=true&key=" + API_KEY);
//	        sb.append("&components=country:uk");
	        sb.append("&input=" + URLEncoder.encode(input, "utf8"));
	        
	        URL url = new URL(sb.toString());
	        conn = (HttpURLConnection) url.openConnection();
	        InputStreamReader in = new InputStreamReader(conn.getInputStream());
	        
	        // Load the results into a StringBuilder
	        int read;
	        char[] buff = new char[1024];
	        while ((read = in.read(buff)) != -1) {
	            jsonResults.append(buff, 0, read);
	        }
	    } catch (MalformedURLException e) {
	        Log.e(LOG_TAG, "Error processing Places API URL", e);
	        return resultList;
	    } catch (IOException e) {
	        Log.e(LOG_TAG, "Error connecting to Places API", e);
	        return resultList;
	    } finally {
	        if (conn != null) {
	            conn.disconnect();
	        }
	    }

	    try {
	        // Create a JSON object hierarchy from the results
	        JSONObject jsonObj = new JSONObject(jsonResults.toString());
	        JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
	        
	        // Extract the Place descriptions from the results
	        resultList = new ArrayList<String>(predsJsonArray.length());
	        for (int i = 0; i < predsJsonArray.length(); i++) {
	            resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
	        }
	    } catch (JSONException e) {
	        Log.e(LOG_TAG, "Cannot process JSON results", e);
	    }
	    
	    return resultList;
	}
	
	
	private static ArrayList<String> inviteeIds;
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
			String inviterId = ATLUser.getParseUserID();
			
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
			        , true,EVENT_TYPE.LUNCH,"","");
			
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
				        , false,EVENT_TYPE.LUNCH,"","");
				
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
					        , false,EVENT_TYPE.LUNCH,"","");
					
					event.add(eventAlt);
				}
				
				
			}
		}
		
		
		return event;
	}
	private void createSelfCalendarEvent() 
	{
		ArrayList<EventProperties> eventProperties = setEvent();
		if (eventProperties!=null && eventProperties.size()>0 
				&& mContext!=null)
		{	
//			Toast.makeText(this,"self calendar ", 
//					Toast.LENGTH_SHORT).show();
			eventController.eventControllerCallBackListener = this;
			/// CALL BACK FROM createCalendarEventCallBack method
			
			for (EventProperties eventAlt:eventProperties)
			{
				eventAlt.status = EVENT_STATUS.THE_ONE;
			}
			eventController.createSelfCalendarEvent(eventProperties,mContext);
//			ATLCalendarCellController.saveCalendarCellWithEvent(eventProperties, mContext);
		}
		
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
		
		
		if (eventProperties!=null && eventProperties.size()>0 && inviteeContactModelArray!=null && inviteeContactModelArray.size()>0
				&& mContext!=null)
		{	
				
			eventController.eventControllerCallBackListener = this;
			/// CALL BACK FROM createCalendarEventCallBack method
			eventController.createCalendarEvent(eventProperties, inviteeContactModelArray,mContext);
//			ATLCalendarCellController.saveCalendarCellWithEvent(eventProperties, mContext);
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
			
	    	ATLContactListActivity.contactIdPicked = new ArrayList<String>();

//			String inviteesNames = "";
//			int count = inviteePicked.size();
//			if (inviteePicked!=null && inviteePicked.size()>0 && inviteePicked.size()<4)
//				for (String friendId:inviteePicked.keySet())
			if (inviteeContactModelArray != null && inviteeContactModelArray.size()>0) 

			alertUser("Calendar invite",
					"Invitation sent " );
			else
				alertUser("Calendar event saved",
						" " );
		//	Toast.makeText(mContext.getApplicationContext(), "Event successfully updated", Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(mContext.getApplicationContext(), "Event failed to update", Toast.LENGTH_LONG).show();

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

		// Date taskDate =
		// EditTaskModel.getInstance().getTaskCellData().taskCellDate;

		datePref = currentCellData.getCalCellDate();
		dateAlt1 = currentCellData.getCalCellALT2Datetime();
		dateAlt2 = currentCellData.getCalCellALT3Datetime();
		Log.v("Date", "pref"+lv_formatter.format(datePref));
		// Date anotherDate = new Date(taskDate.getTime() + (3600 * 1000));
		String prefdateFormateInUTC = lv_formatter.format(datePref);
		String altOnefdateFormateInUTC = "";
		String altTwofdateFormateInUTC = "";
		if (dateAlt1 != null) {
			altOnefdateFormateInUTC = lv_formatter.format(dateAlt1);
		}
		if (dateAlt2 != null) {
			altTwofdateFormateInUTC = lv_formatter.format(dateAlt2);
		}
		
		//if(currentCellData.calCellTitle.trim().equals("")){
		//	Toast.makeText(mContext, "Please fill in event title", Toast.LENGTH_SHORT).show();
		//}

		/*
		 * Nghia comment out and add my code to testing. We need a call back method to handle to save to AlertView about the event sent or could not be send
		 */
//		parseConnect.calendarInvite(invitee, eventTitle, eventLocation,
//				datePref, dateAlt1, dateAlt2, eventDuration,
//				prefdateFormateInUTC, altOnefdateFormateInUTC,
//				altTwofdateFormateInUTC);

		/*
		 * Nghia End comment out and add my code to testing
		 */

		/*
		 * NGHIA : Parse Event Invite
		 */
		// TODO Auto-generated method stub

		// do stuff with the user
//		if(invitee != null){
//		ATLAlertWebAccess eventInviteWebAccess = new ATLAlertWebAccess();
//		eventInviteWebAccess.userId = ATLUser.getParseUserID();
//		eventInviteWebAccess.userDisplayname = ATLUser.getUserNameDisplay();
//		
//		eventInviteWebAccess.inviteId = "";
//		
//		eventInviteWebAccess.invitee = invitee.getAtlasId();
//		eventInviteWebAccess.inviteeEmail = invitee.getEmailAddress();
//		eventInviteWebAccess.inviteeName = invitee.displayName();
//		
//		eventInviteWebAccess.inviter = ATLUser.getParseUserID();
//		eventInviteWebAccess.inviterEmail = ATLUser.getEmail();
//		eventInviteWebAccess.inviterName = ATLUser
//				.getUserNameDisplay();
//		
//		eventInviteWebAccess.eventDuration = currentCellData.calCellDurationMinutes;
//		eventInviteWebAccess.eventLocation = currentCellData.calCellLocation;
//		eventInviteWebAccess.eventTitle = currentCellData.calCellTitle;
//		;
//
//		eventInviteWebAccess.delegate = this;
//		eventInviteWebAccess.getPage_AsyncWithType(
//				AlertType.eventInvited_Sent, currentCellData);
//		}
//		
		currentCellData.eventResponseType_CellData = EventResponseType.Pending;
		
		/*
		 * NGHIA : End Parse Event Invite
		 */

		if (inviteeContactModelArray != null && inviteeContactModelArray.size()>0) {
			createEventCalendar();
//			alertUser("Calendar invite",
//					"Your event invite to " + invitee.getFirstname()
//							+ " was sent");
		} else {// 2013-01-08 TAN: fix crash when null pointer
//				 alertUser("Calendar invite",
//				 "Your event has no invitation " );
			
			createSelfCalendarEvent();
			
			
//			finish();
//			overridePendingTransition(R.anim.stand_by, R.anim.out_to_bottom);
		}

	}
	

	@Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) 
   {
       if(keyCode == KeyEvent.KEYCODE_BACK)
       {
    	   ATLContactListActivity.contactIdPicked = new ArrayList<String>();
//          
//      	 if (rightActioBarBtn.getVisibility()==View.VISIBLE)
//      	 {
      		  Intent intent = new Intent(getBaseContext(), CalendarDayView.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//               		//intent.putExtra("EXTRA_SESSION_ID", sessionId);
                startActivity(intent);
                this.overridePendingTransition(R.anim.enter,R.anim.leave);
                return true;
//       }
       
       }
       return false;
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
				 Intent intent = new Intent(getBaseContext(), CalendarDayView.class);
                 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                		//intent.putExtra("EXTRA_SESSION_ID", sessionId);
                 startActivity(intent);
                 overridePendingTransition(R.anim.enter,R.anim.leave);
//				finish();
//				((Activity)mContext).overridePendingTransition(R.anim.stand_by, R.anim.out_to_bottom);
//				// Write your code here to execute after dialog closed
				// Toast.makeText(getApplicationContext(), "You clicked on OK",
				// Toast.LENGTH_SHORT).show();

			}
		});
		alertDialog.setCanceledOnTouchOutside(false); 

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

//			ATLContactModel contact = (ATLContactModel) data.getExtras()
//					.getParcelable("com.atlasapp.model.ATLContactModel");
//			invitee = contact;
//			if (invitee!=null){
//			currentCellData.attendee = contact; // NGHIA add to currentCellData
//			//Log.v("Contact",
////					"name " + contact.displayName() + "Email "
////							+ contact.getEmailAddress() + "Phone"
////							+ contact.getPhoneNumber());
//			assigneeName.setText(contact.displayName());
//			ImageView imageContact = (ImageView) findViewById(R.id.imageContact);
//			imageContact.setImageBitmap(contact.getImage());
//			}
			
			ArrayList<String> inviteeIds = (ArrayList<String>) data.getStringArrayListExtra("arrayList");
			if (inviteeIds!=null)
			{
				this.inviteeIds = inviteeIds;
//				Toast.makeText(getBaseContext(),inviteeIds.size() +" Contacts picked...", 
//		                Toast.LENGTH_SHORT).show();
//				
//				
				
				if (inviteeIds!=null && inviteeIds.size()>0)
				{
					 inviteeContactModelArray = new ArrayList<ATLContactModel>();
					HashMap<String,ATLContactModel> invitees = ATLContactModel.getContactByFriendId(inviteeIds);
					String inviteeNames = "";
//					this.inviteePicked = (invitees!=null && invitees.size()>0)? invitees: new HashMap<String,ATLContactModel>();
					if (invitees!=null && invitees.size()>0)
					{
						for (String friendId:invitees.keySet())
						{
							inviteeContactModelArray.add(invitees.get(friendId));
							inviteeNames += invitees.get(friendId).getFirstname()+" ,";   
							
						}
//						Toast.makeText(getBaseContext(),inviteeIds.size() +" Contacts friendId picked..."+friendId+" Name "+
//						inviteePicked.get(friendId).getFirstname()
//								, 
//				                Toast.LENGTH_SHORT).show();
						
						assigneeName.setText(inviteeNames);
					}
				
				}
//				
			}

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

	@Override
	public void getUserMoveFromParseCallBack(
			boolean success,
			HashMap<String, ArrayList<ItemUserProperties>> userMoveRecordsByPrimaryEventId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refreshAlertsInBackgroundCallBack(boolean success) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getNewEventReceived(
			HashMap<String, ArrayList<EventProperties>> newEventsByPrimary,
			ArrayList<String> webItemIds,
			HashMap<String, String> webEventIdByPrimry) {
		// TODO Auto-generated method stub
		
	}
	private class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
	    private ArrayList<String> resultList;
	    
	    public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
	        super(context, textViewResourceId);
	    }
	    
	    @Override
	    public int getCount() {
	        return resultList.size();
	    }

	    @Override
	    public String getItem(int index) {
	        return resultList.get(index);
	    }

	    @Override
	    public Filter getFilter() {
	        Filter filter = new Filter() {
	            @Override
	            protected FilterResults performFiltering(CharSequence constraint) {
	                FilterResults filterResults = new FilterResults();
	                if (constraint != null) {
	                    // Retrieve the autocomplete results.
	                    resultList = autocomplete(constraint.toString());
	                    
	                    // Assign the data to the FilterResults
	                    filterResults.values = resultList;
	                    filterResults.count = resultList.size();
	                }
	                return filterResults;
	            }

	            @Override
	            protected void publishResults(CharSequence constraint, FilterResults results) {
	                if (results != null && results.count > 0) {
	                    notifyDataSetChanged();
	                }
	                else {
	                    notifyDataSetInvalidated();
	                }
	            }};
	        return filter;
	    }
	}
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		 String str = (String) adapterView.getItemAtPosition(position);
	        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
		
	}
	
	
}
