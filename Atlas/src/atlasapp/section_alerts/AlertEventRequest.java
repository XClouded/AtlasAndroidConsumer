/**
 * 
 */
package atlasapp.section_alerts;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import atlasapp.common.ATLColor;
import atlasapp.common.ATLConstants.AlertType;
import atlasapp.common.ATLConstants.EventResponseType;
import atlasapp.common.CalendarUtilities;
import atlasapp.model.ATLEventCalendarModel;
import atlasapp.section_appentry.R;
import atlasapp.section_calendar.ATLCalCellData;
import atlasapp.section_calendar.ATLCalCellList;
import atlasapp.section_calendar.ATLCalendarManager;
import atlasapp.section_calendar.CalendarDayView;


import com.parse.ParseObject;
import com.parse.ParseUser;

public class AlertEventRequest extends Activity implements OnClickListener,
		ATLAlertWebAccessCallBackInterface, ATLAlertEventRequestCellDelegate {
	static public final int EVENT_REQUEST_DECIDELATER = 0;
	static public final int EVENT_REQUEST_DECLINE = 1;
	static public final int EVENT_REQUEST_PREF = 2;
	static public final int EVENT_REQUEST_ALT1 = 3;
	static public final int EVENT_REQUEST_ALT2 = 4;

	public static ATLAlertWebAccessCallBackInterface delegate;
	ATLAlertEventRequestAdapter adapter;
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
	TextView eventTitle;

	Date currentDate = new Date();
	private ArrayList<Integer> atlIndexs = new ArrayList<Integer>();
	private ArrayList<Date> atlDateArr = new ArrayList<Date>();
	private ImageView alt2Img;
	private ImageView alt3Img;
	private ImageView preferImg;
	private int altCurrentActive;
	private ATLEventCalendarModel currentCellData = new ATLEventCalendarModel();
	private Date preferDayNew;
	private Date alt2DayNew;
	private Date alt3DayNew;

	static final int CELL_BACKGROUND_ORANGE = Color.rgb(255, 211, 170);
	static final int CELL_BACKGROUND_WHITE = Color.WHITE;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.alert_event_request);
		currentCellData = AlertRequestEventSingleton.getInstance()
				.getEventCellData();
		currentDate = currentCellData.startDateTime;
		
		initAttributes();
		setListeners();
		bindingData();
	}


	private void initAttributes() {
		eventTitle = (TextView) findViewById(R.id.alerts_events_title);
	
		preferDay = (TextView) findViewById(R.id.preferDay);
		preferHour = (TextView) findViewById(R.id.preferHour);
		preferImg = (ImageView) findViewById(R.id.preferImg);
		preferImg.setOnClickListener(this);
	
		alt2Hour = (TextView) findViewById(R.id.alt2Hour);
		alt2Day = (TextView) findViewById(R.id.alt2Day);
		alt2Img = (ImageView) findViewById(R.id.alt2);
	
		alt3Hour = (TextView) findViewById(R.id.alt3Hour);
		alt3Day = (TextView) findViewById(R.id.alt3Day);
		alt3Img = (ImageView) findViewById(R.id.alt3);
	
		backArrowBtn = (ImageButton) findViewById(R.id.prevDay);
		backArrowBtn.setOnClickListener(this);
		nextArrowBtn = (ImageButton) findViewById(R.id.nextDay);
		nextArrowBtn.setOnClickListener(this);
		dateLable = (TextView) findViewById(R.id.currentDay);
	
		listEvent = (ListView) findViewById(R.id.calendarList);
		
		CalendarDayView.calListModel = ATLCalendarManager.getCalendarList(this);
//		aCalCellList = new ATLCalCellList();
//		aCalCellList.setCalListDate(currentDate);
//		adapter = new ATLAlertEventRequestAdapter(aCalCellList, this);
//		listEvent.setAdapter(adapter);
	}



	private void setListeners() {
		// TODO Auto-generated method stub
		ImageView decideLater = (ImageView) findViewById(R.id.cancelBtn);
		decideLater.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				actionRequestWithType(EVENT_REQUEST_DECIDELATER);
			}
		});
		ImageView decline = (ImageView) findViewById(R.id.saveBtn);
		decline.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				actionRequestWithType(EVENT_REQUEST_DECLINE);
			}
		});
	}



	private void bindingData(){
		// this is the text we'll be operating on  
	    String key = "You've been invited by ";
	    String key1 = " to \"";
	    String key2 = "\".";
//	    SpannableString title = new SpannableString(key + currentCellData.alertCellSenderName 
//	    		+ key1 +  currentCellData.alertCellEventTitle + key2);
	    SpannableString title = new SpannableString(key + "sender"
	    		+ key1 +  currentCellData.title + key2);
	    // make "key" (characters 0 to key.length()) White  
	    title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, key.length(), 0);
	    
	    // make "alertCellSenderName" (characters 0 to key.length()) White Bold
	    int length1 = key.length() + 5;
	    title.setSpan(new ForegroundColorSpan(Color.WHITE), key.length(), length1 , 0);
	    title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), key.length(), length1 , 0);
	    // make key1 (characters 0 to key.length()) White Bold
	    
	    int length2 = length1 + key1.length();
	    title.setSpan(new ForegroundColorSpan(Color.WHITE), length1, length2 , 0);
	    
	    int length3 = length2 + currentCellData.title.length();
	    title.setSpan(new ForegroundColorSpan(ATLColor.blue_event_title), length2, length3, 0);
	    title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),length2, length3, 0);  
	    eventTitle.setText(title, BufferType.SPANNABLE);
		
	    int length4 = length3 + key2.length();
	    title.setSpan(new ForegroundColorSpan(Color.WHITE), length3, length4 , 0);
	    
		dateDidChanged();// Set data to view
		initATLTime();
		currentTabIndex = atlIndexs.get(0);
		lastShowAcceptButton = currentTabIndex;
		listEvent.smoothScrollToPositionFromTop(currentTabIndex, 0);
		listEvent.post(showAcceptView);
		
	}
	
	Runnable showAcceptView = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			didTapToShowAcceptViewAtIndex(currentTabIndex);
		}
	};



	private void initATLTime() {
		// TODO Auto-generated method stub
		int count = 0;
		atlDateArr.clear();
		atlIndexs.clear();
	
		altCurrentActive = 0;
		String hourFormat = getResources().getString(
				R.string.calendar_edit_event_when_hour_format);
		String dayFormat = getResources().getString(
				R.string.calendar_edit_event_when_day_format);
		preferDay.setText(DateFormat.format(dayFormat,
				currentCellData.startDateTime));
		preferHour.setText(DateFormat
				.format(hourFormat, currentCellData.startDateTime)
				.toString().toUpperCase());
		preferDayNew = currentCellData.startDateTime;
		atlDateArr.add(preferDayNew);
		changeBgColorOfCell(preferDayNew);
		count = 1;
		
		altCurrentActive = altCurrentActive < count ? altCurrentActive : count;
		setAltCurrentActive();
		
	/*
		preferDay.setText(DateFormat.format(dayFormat,
				currentCellData.alertCellPreferredDatetime));
		preferHour.setText(DateFormat
				.format(hourFormat, currentCellData.alertCellPreferredDatetime)
				.toString().toUpperCase());
		preferDayNew = currentCellData.alertCellPreferredDatetime;
		atlDateArr.add(preferDayNew);
		changeBgColorOfCell(preferDayNew);
		count = 1;
		if (currentCellData.alertCellAlt1DateTime != null) {// For init value
			alt2Day.setText(DateFormat.format(dayFormat,
					currentCellData.alertCellAlt1DateTime));
			alt2Hour.setText(DateFormat
					.format(hourFormat, currentCellData.alertCellAlt1DateTime)
					.toString().toUpperCase());
			alt2DayNew = currentCellData.alertCellAlt1DateTime;
			atlDateArr.add(alt2DayNew);
			changeBgColorOfCell(alt2DayNew);
			count = 2;
			alt2Img.setOnClickListener(this);// No listener if have no day
		}
	
		if (currentCellData.alertCellAlt2DateTime != null) { // For init
																// value
			alt3Day.setText(DateFormat.format(dayFormat,
					currentCellData.alertCellAlt2DateTime));
			alt3Hour.setText(DateFormat
					.format(hourFormat, currentCellData.alertCellAlt2DateTime)
					.toString().toUpperCase());
			alt3DayNew = currentCellData.alertCellAlt2DateTime;
			atlDateArr.add(alt3DayNew);
			changeBgColorOfCell(alt3DayNew);
			count = 3;
			alt3Img.setOnClickListener(this);// No listener if have no day
		}
		
		altCurrentActive = altCurrentActive < count ? altCurrentActive : count;
		setAltCurrentActive();
		*/
	}



	private void dateDidChanged() {
		// TODO Auto-generated method stub
		dateLable.setText(DateFormat
				.format(getResources().getString(
						R.string.calendar_day_view_day_format), currentDate));
		adapter.notifyDataSetChanged();
		
		
	}



	private void setAltCurrentActive() {
		altCurrentActive = altCurrentActive % 3;
		switch (altCurrentActive) {
		case 0:
			preferImg
					.setImageResource(R.drawable.calendar_editevent_alttimes_select_btn);
			alt2Img.setImageResource(R.drawable.calendar_editevent_alttimes_unselect_btn);
			alt3Img.setImageResource(R.drawable.calendar_editevent_alttimes_unselect_btn);
			// SEND ACCEPT INFO
			/*
			if (currentCellData.alertCellPreferredDatetime != null) {
				currentCellData.alertDateAccepted = AlertParseObjectParser
						.localDateTimeToServerTimeString(currentCellData.alertCellPreferredDatetime);
			}
			*/
			break;
		case 1:
			/*
			preferImg
					.setImageResource(R.drawable.calendar_editevent_alttimes_unselect_btn);
			alt2Img.setImageResource(R.drawable.calendar_editevent_alttimes_select_btn);
			alt3Img.setImageResource(R.drawable.calendar_editevent_alttimes_unselect_btn);
			// SEND ACCEPT INFO
			if (currentCellData.alertCellAlt1DateTime != null) {
				currentCellData.alertDateAccepted = AlertParseObjectParser
						.localDateTimeToServerTimeString(currentCellData.alertCellAlt1DateTime);
			}
			*/
			break;
		case 2:
			/*
			preferImg
					.setImageResource(R.drawable.calendar_editevent_alttimes_unselect_btn);
			alt2Img.setImageResource(R.drawable.calendar_editevent_alttimes_unselect_btn);
			alt3Img.setImageResource(R.drawable.calendar_editevent_alttimes_select_btn);
			// SEND ACCEPT INFO
			if (currentCellData.alertCellAlt2DateTime != null) {
				currentCellData.alertDateAccepted = AlertParseObjectParser
						.localDateTimeToServerTimeString(currentCellData.alertCellAlt2DateTime);
			}
			*/
			break;
		}
	
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
				cellData.calCellCalendarColor = ATLColor.RED;
				cellData.calCellTitle = currentCellData.title;
				cellData.eventResponseType_CellData = EventResponseType.Pending;
				cellData.isBlank = false;
				atlIndexs.add(Integer.valueOf(i));// TAN: To keep index of the
													// row
			}
	
		}
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v == backArrowBtn) {
			// TAN - still have bug at the first time call
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDate);
			Calendar calTemp = Calendar.getInstance();
			calTemp.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
					cal.get(Calendar.DAY_OF_MONTH) - 1, 0, 0, 0);
			currentDate = calTemp.getTime();
			aCalCellList.currentDateDidChanged(currentDate);
			dateDidChanged();
			int cIndexTemp = altCurrentActive;//to keep previous index 
			initATLTime();
			altCurrentActive = cIndexTemp;
			setAltCurrentActive();
			switch(altCurrentActive){
			case 0:
				handleScrollToDate(currentCellData.startDateTime);
				break;
			case 1:
//				handleScrollToDate(currentCellData.alertCellAlt1DateTime);
				break;
			case 2:
//				handleScrollToDate(currentCellData.alertCellAlt2DateTime);
				break;
			default:
			}
			
//			if(atlIndexs.size()>0){
//				currentTabIndex = atlIndexs.get(0);
//				listEvent.smoothScrollToPositionFromTop(currentTabIndex, 0);
//				listEvent.post(showAcceptView);
//			}

		} else if (v == nextArrowBtn) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDate);
			Calendar calTemp = Calendar.getInstance();
			calTemp.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
					cal.get(Calendar.DAY_OF_MONTH) + 1, 0, 0, 0);
			currentDate = calTemp.getTime();
			aCalCellList.currentDateDidChanged(currentDate);
			dateDidChanged();
			int cIndexTemp = altCurrentActive;//to keep previous index
			initATLTime();
			altCurrentActive = cIndexTemp;
			setAltCurrentActive();
			switch(altCurrentActive){
			case 0:
				handleScrollToDate(currentCellData.startDateTime);
				break;
			case 1:
//				handleScrollToDate(currentCellData.alertCellAlt1DateTime);
				break;
			case 2:
//				handleScrollToDate(currentCellData.alertCellAlt2DateTime);
				break;
			default:
			}
//			if(atlIndexs.size()>0){
//				currentTabIndex = atlIndexs.get(0);
//				listEvent.smoothScrollToPositionFromTop(currentTabIndex, 0);
//				listEvent.post(showAcceptView);
//			}
		} else if (v == alt2Img) {
			Log.v("touch on date", "alt2Img");

			/*
			// TODO scroll and run actionRe... when touch on confirm button
			currentDate = currentCellData.alertCellAlt1DateTime;
			aCalCellList.currentDateDidChanged(currentDate);
			dateDidChanged(); 
			initATLTime();
			handleScrollToDate(currentDate);
			//2013-02-08 TAN: comment out to fix bug out of index
//			currentTabIndex = atlIndexs.get(1);
//			listEvent.smoothScrollToPositionFromTop(currentTabIndex, 0);
//			listEvent.post(showAcceptView);
			altCurrentActive = 1;
			setAltCurrentActive();
			 */
		} else if (v == alt3Img) {
			Log.v("touch on date", "alt3Img");
			/*
			currentDate = currentCellData.alertCellAlt2DateTime;
			aCalCellList.currentDateDidChanged(currentDate);
			dateDidChanged(); 
			initATLTime();
			handleScrollToDate(currentDate);
			//2013-02-08 TAN: comment out to fix bug out of index
//			currentTabIndex = atlIndexs.get(2);
//			listEvent.smoothScrollToPositionFromTop(currentTabIndex, 0);
//			listEvent.post(showAcceptView);
			altCurrentActive = 2;
			setAltCurrentActive();
			*/
		} else if (v == preferImg) {
			Log.v("touch on date", "preferImg");
			currentDate = currentCellData.startDateTime;
			aCalCellList.currentDateDidChanged(currentDate);
			dateDidChanged();
			initATLTime();
			handleScrollToDate(currentDate);
//			currentTabIndex = atlIndexs.get(0);
//			listEvent.smoothScrollToPositionFromTop(currentTabIndex, 0);
//			listEvent.post(showAcceptView);
			altCurrentActive = 0;
			setAltCurrentActive();
		}
	}
	
	//=========================================================================
	//2013-02-08 TAN: create this method to handle scroll to alt time # start
	//=========================================================================
	
	private void handleScrollToDate(Date aDate) {
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
				currentTabIndex = i;
				listEvent.smoothScrollToPositionFromTop(currentTabIndex, 0);
				listEvent.post(showAcceptView);
			}
	
		}
	}
	//=========================================================================
	//2013-02-08 TAN: create this method to handle scroll to alt time # end
	//=========================================================================

	private void actionRequestWithType(int type) {
		
		switch (type) {
		case EVENT_REQUEST_DECIDELATER: {

			finish();
			overridePendingTransition(R.anim.stand_by, R.anim.out_to_bottom);
			break;
		}
		case EVENT_REQUEST_DECLINE: {
/*
			currentCellData.alertCellAcceptedDate = null;
			currentCellData.alertCellResponseStatus = EVENT_REQUEST_DECLINE;
			postEventAcceptedSent(AlertType.eventAccepted_Sent, currentCellData);
			*/
			break;
		}
		case EVENT_REQUEST_PREF: {
			// TODO
			// altCurrentActive = 1;
			// setAltCurrentActive();
			/*
			currentCellData.alertCellAcceptedDate = currentCellData.alertCellPreferredDatetime;
			currentCellData.alertTimeChosenLabel = EVENT_REQUEST_PREF;
			postEventAcceptedSent(AlertType.eventAccepted_Sent, currentCellData);
			*/
			break;
		}
		case EVENT_REQUEST_ALT1: {
			// TODO
			// altCurrentActive = 2;
			// setAltCurrentActive();
/*
			currentCellData.alertCellAcceptedDate = currentCellData.alertCellAlt1DateTime;
			currentCellData.alertTimeChosenLabel = EVENT_REQUEST_ALT1;
			postEventAcceptedSent(AlertType.eventAccepted_Sent, currentCellData);
			*/
			break;
		}
		case EVENT_REQUEST_ALT2: {
			// TODO
			// altCurrentActive = 0;
			// setAltCurrentActive();
			/*
			currentCellData.alertCellAcceptedDate = currentCellData.alertCellAlt2DateTime;
			currentCellData.alertTimeChosenLabel = EVENT_REQUEST_ALT2;
			postEventAcceptedSent(AlertType.eventAccepted_Sent, currentCellData);
			*/
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void didPostDataFinish(Object data, int alertType, String result) {
		// TODO
		/*
		 * We have 4 case: 1. Decline: Do nothing 2. Pref: Save Pref with Green
		 * 3. Alt1: Save Alt1 with Green 4 Alt2: Save Alt2 with Green
		 */
		AlertCellData alert = (AlertCellData) data;
		switch (alertType) {
		case AlertType.eventAccepted_Sent: {
			// TODO:
			switch (Integer.valueOf(result)) {
			case EVENT_REQUEST_PREF:
				CalendarUtilities.saveEventByAlertDataAndDate(alert,
						alert.alertCellPreferredDatetime, this);
				break;
			case EVENT_REQUEST_ALT1:
				CalendarUtilities.saveEventByAlertDataAndDate(alert,
						alert.alertCellAlt1DateTime, this);
				break;
			case EVENT_REQUEST_ALT2:
				CalendarUtilities.saveEventByAlertDataAndDate(alert,
						alert.alertCellAlt2DateTime, this);
				break;

			default:
				break;
			}
			break;
		}
		case AlertType.eventRejected_Sent:
			// TODO do nothing
			break;
		default:
			break;
		}
		// switch (0) {
		// case EVENT_REQUEST_DECLINE:
		//
		// break;
		//
		// case EVENT_REQUEST_PREF:
		// Log.v("EVENT_REQUEST_PREF", "EVENT_REQUEST_PREF");
		// break;
		// case EVENT_REQUEST_ALT1:
		// Log.v("EVENT_REQUEST_ALT1", "EVENT_REQUEST_ALT1");
		// break;
		// case EVENT_REQUEST_ALT2:
		// Log.v("EVENT_REQUEST_ALT2", "EVENT_REQUEST_ALT2");
		// break;
		// default:
		// break;
		// }
		finish();
		overridePendingTransition(R.anim.stand_by, R.anim.out_to_bottom);
	}

	public void postEventAcceptedSent(int eventacceptedSent,
			AlertCellData currentCellData) {
		// TODO Auto-generated method stub
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			// do stuff with the user

			ATLAlertWebAccess eventAcceptedWebAccess = new ATLAlertWebAccess();
			eventAcceptedWebAccess.userId = currentUser.getObjectId();
			eventAcceptedWebAccess.inviteId = currentCellData.alertCellAtlasId;
			eventAcceptedWebAccess.invitee = currentUser.getObjectId();
			eventAcceptedWebAccess.inviter = currentCellData.alertCellSenderId;
			eventAcceptedWebAccess.fromDisplayname = currentUser
					.getString("displayname");
			eventAcceptedWebAccess.eventDuration = currentCellData.alertCellEventDuration;
			eventAcceptedWebAccess.eventLocation = currentCellData.alertCellEventLocation;
			eventAcceptedWebAccess.eventTitle = currentCellData.alertCellEventTitle;
			eventAcceptedWebAccess.timeLabel = currentCellData.alertTimeChosenLabel;

			// eventAcceptedWebAccess.userId = AtlasServerConnect
			// .getParseUserUserObjectId();
			// eventAcceptedWebAccess.userDisplayname = AtlasServerConnect
			// .getParseUserUserNameDisplay();
			// eventAcceptedWebAccess.username = AtlasServerConnect
			// .getParseUserUserName();
			// eventAcceptedWebAccess.userEmail = AtlasServerConnect
			// .getParseUserUserEmail();

			eventAcceptedWebAccess.delegate = this;
			eventAcceptedWebAccess.getPage_AsyncWithType(
					AlertType.eventAccepted_Sent, currentCellData);
		} else {

		}

	}

	@Override
	public void didGetDataFinish(Object data, int alertType, String result) {
		// TODO Auto-generated method stub

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

	
	//========================================================
	// ATLAlertEventRequestCellDelegate - implement # START
	//========================================================
	private int lastShowAcceptButton = -1;
	private int currentTabIndex = -1;
	@Override
	public void didTapToShowAcceptViewAtIndex(int index) {
		// TODO Auto-generated method stub
		Log.v("Task", "index: " + index);
//		Toast.makeText(this, "Touched Index " + index, Toast.LENGTH_SHORT).show();
		boolean isALTCell = false;
		for(int i : atlIndexs){
			if(i == index){
				isALTCell = true;
			}
		}
	
		if (lastShowAcceptButton != -1) {
				// Edit old data
				ATLCalCellData cellOldData = (ATLCalCellData) listEvent
						.getAdapter().getItem(lastShowAcceptButton);
				int firstVisible = listEvent.getFirstVisiblePosition();
				Log.v("Event", "lastShowAcceptButton: " + lastShowAcceptButton);
				Log.v("Event", "firstVisible: " + firstVisible);
				cellOldData.isShowOffHour = false;
				if(isALTCell){
					ATLCalCellData newCellData = (ATLCalCellData) listEvent
							.getAdapter().getItem(index);
					newCellData.isShowOffHour = true;
					lastShowAcceptButton = index;
				}
		}
		
		listEvent.smoothScrollToPositionFromTop(index, 0);
		adapter.notifyDataSetChanged();
	}


	private boolean isViewActive = true;

	@Override
	public void didAcceptAtIndex(int index) {
		// TODO Auto-generated method stub
		
		if(isViewActive){
			isViewActive = false;
			int size = atlIndexs.size();
			for(int i = 0; i< size; i++){
				if(atlIndexs.get(i) == index){
					switch(i){
					case 0:// prefer date
						actionRequestWithType(EVENT_REQUEST_PREF);
						break;
					case 1:// alt2 date
						actionRequestWithType(EVENT_REQUEST_ALT1);
						break;
					case 2:// alt3 date
						actionRequestWithType(EVENT_REQUEST_ALT2);
						break;
					default:
						break;
					}
					break;
				}
			}
		}
		
		
	}
	
	//========================================================
	// ATLAlertEventRequestCellDelegate - implement # END
	//========================================================

}
