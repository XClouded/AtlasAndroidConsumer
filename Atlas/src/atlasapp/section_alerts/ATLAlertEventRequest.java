//  ==================================================================================================================
//  ATLAlertEventRequest.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2013-02-25 TAN:     Create class
//  ==================================================================================================================

package atlasapp.section_alerts;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.BufferType;
import atlasapp.common.ATLColor;
import atlasapp.common.ATLConstants.AlertType;
import atlasapp.common.ATLConstants.EventResponseType;
import atlasapp.common.CalendarUtilities;
import atlasapp.database.DatabaseConstants.ITEM_USER_TASK_STATUS;
import atlasapp.database.EventController;
import atlasapp.database.EventControllerCallBackInterface;
import atlasapp.database.EventProperties;
import atlasapp.database.ItemUserProperties;
import atlasapp.model.ATLAlertModel;
import atlasapp.section_appentry.R;
import atlasapp.section_calendar.ATLCalCellData;
import atlasapp.section_calendar.ATLCalCellList;
import atlasapp.section_calendar.ATLCalendarManager;
import atlasapp.section_calendar.CalendarDayView;


import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * @author Ryan Tan
 * 
 */
public class ATLAlertEventRequest extends Activity implements OnClickListener,
		ATLAlertWebAccessCallBackInterface, ATLAlertEventRequestCellDelegate,
		EventControllerCallBackInterface {

	private EventController eventController;
	static public final int EVENT_REQUEST_DECIDELATER = 0;
	static public final int EVENT_REQUEST_DECLINE = 1;
	static public final int EVENT_REQUEST_PREF = 2;
	static public final int EVENT_REQUEST_ALT1 = 3;
	static public final int EVENT_REQUEST_ALT2 = 4;
	public int chosenItem = 0;
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

	private AlertCellData currentCellData = new AlertCellData();
	public EventProperties eventPropertiesPre = new EventProperties();
	public EventProperties eventPropertiesAlt1 = new EventProperties();
	public EventProperties eventPropertiesAlt2 = new EventProperties();
	private boolean isSimulateMode = false;

	private Date preferDayNew;
	private Date alt2DayNew;
	private Date alt3DayNew;
	
	private ProgressBar prBar;

	static final int CELL_BACKGROUND_ORANGE = Color.rgb(255, 211, 170);
	static final int CELL_BACKGROUND_WHITE = Color.WHITE;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		eventController = new EventController();
		// / initialize ATLMockDB to listen to call back methods
		// / from event controller....
		eventController.eventControllerCallBackListener = this;

		setContentView(R.layout.alert_event_request);

		// Get data from singleton objects
		{
			// currentCellData = AlertRequestEventSingleton.getInstance()
			// .getEventCellData();
			// currentDate = currentCellData.startDateTime;
			if (isSimulateMode) {
				eventPropertiesPre = new EventProperties();
				eventPropertiesAlt1 = new EventProperties();
				eventPropertiesAlt2 = new EventProperties();
				currentDate = eventPropertiesPre.startDateTime;
				if (currentDate == null) {
					currentDate = new Date();
					eventPropertiesPre.startDateTime = currentDate;
					eventPropertiesAlt1.startDateTime = currentDate;
					eventPropertiesAlt2.startDateTime = currentDate;
				}
			} else {
//				currentCellData = AlertRequestEventSingleton.getInstance().getAlertCellData();
//				Log.v("currentCellData.getEventPrimaryKey", currentCellData.getEventPrimaryKey());
//				String primaryKey = currentCellData.getEventPrimaryKey();
//				AlertRequestEventSingleton.getInstance().loadEventPropertiesByKey(primaryKey);
				
				eventPropertiesPre = AlertRequestEventSingleton.getInstance()
						.getEventPropertiesPre();
				eventPropertiesAlt1 = AlertRequestEventSingleton.getInstance()
						.getEventPropertiesAlt1();
				eventPropertiesAlt2 = AlertRequestEventSingleton.getInstance()
						.getEventPropertiesAlt2();

				currentDate = eventPropertiesPre.startDateTime;
				if (currentDate == null) {
					currentDate = new Date();
					eventPropertiesPre.startDateTime = currentDate;
				}
			}
		}

		initAttributes();
		setListeners();
		bindingData();
	}

	private void initAttributes() {
		prBar = (ProgressBar) findViewById(R.id.progressBar1);
		
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

//		CalendarDayView.calListModel = ATLCalendarManager.getCalendarList(this);
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
				// actionRequestWithType(EVENT_REQUEST_DECLINE);
				eventController.respondToEventInvite(
						eventPropertiesPre.objectId, null);
				chosenItem = EVENT_REQUEST_DECLINE;
			}
		});
	}

	private void bindingData() {
		// this is the text we'll be operating on
		String key = "You've been invited by ";
		String key1 = " to \"";
		String key2 = "\".";
		// SpannableString title = new SpannableString(key +
		// currentCellData.alertCellSenderName
		// + key1 + currentCellData.alertCellEventTitle + key2);
		SpannableString title = new SpannableString(key + "sender" + key1
				+ eventPropertiesPre.title + key2);
		// make "key" (characters 0 to key.length()) White
		title.setSpan(new ForegroundColorSpan(Color.WHITE), 0, key.length(), 0);

		// make "alertCellSenderName" (characters 0 to key.length()) White Bold
		int length1 = key.length() + "sender".length();
		title.setSpan(new ForegroundColorSpan(Color.WHITE), key.length(),
				length1, 0);
		title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
				key.length(), length1, 0);
		// make key1 (characters 0 to key.length()) White Bold

		int length2 = length1 + key1.length();
		title.setSpan(new ForegroundColorSpan(Color.WHITE), length1, length2, 0);

		int length3 = length2 + eventPropertiesPre.title.length();
		title.setSpan(new ForegroundColorSpan(ATLColor.blue_event_title),
				length2, length3, 0);
		title.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), length2,
				length3, 0);
		int length4 = length3 + key2.length();
		title.setSpan(new ForegroundColorSpan(Color.WHITE), length3, length4, 0);
		eventTitle.setText(title, BufferType.SPANNABLE);

		dateDidChanged();// Set data to view
		initATLTime();

		if (atlIndexs.size() > 0) {
			currentTabIndex = atlIndexs.get(0);
		} else {
			currentTabIndex = 5;// hard coded
		}

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
				eventPropertiesPre.startDateTime));
		preferHour.setText(DateFormat
				.format(hourFormat, eventPropertiesPre.startDateTime)
				.toString().toUpperCase());
		preferDayNew = eventPropertiesPre.startDateTime;
		atlDateArr.add(preferDayNew);
		changeBgColorOfCell(preferDayNew);
		count = 1;

		if (eventPropertiesAlt1.startDateTime != null) {
			// For init value
			alt2Day.setText(DateFormat.format(dayFormat,
					eventPropertiesAlt1.startDateTime));
			alt2Hour.setText(DateFormat
					.format(hourFormat, eventPropertiesAlt1.startDateTime)
					.toString().toUpperCase());
			alt2DayNew = eventPropertiesAlt1.startDateTime;
			atlDateArr.add(alt2DayNew);
			changeBgColorOfCell(alt2DayNew);
			count = 2;
			alt2Img.setOnClickListener(this);// No listener if have no day
		}

		if (eventPropertiesAlt2.startDateTime != null) { // For init //value
			alt3Day.setText(DateFormat.format(dayFormat,
					eventPropertiesAlt2.startDateTime));
			alt3Hour.setText(DateFormat
					.format(hourFormat, eventPropertiesAlt2.startDateTime)
					.toString().toUpperCase());
			alt3DayNew = eventPropertiesAlt2.startDateTime;
			atlDateArr.add(alt3DayNew);
			changeBgColorOfCell(alt3DayNew);
			count = 3;
			alt3Img.setOnClickListener(this);// No listener if have no day
		}

		altCurrentActive = altCurrentActive < count ? altCurrentActive : count;
		setAltCurrentActive();

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
			 * if (currentCellData.alertCellPreferredDatetime != null) {
			 * currentCellData.alertDateAccepted = AlertParseObjectParser
			 * .localDateTimeToServerTimeString
			 * (currentCellData.alertCellPreferredDatetime); }
			 */
			break;
		case 1:

			preferImg
					.setImageResource(R.drawable.calendar_editevent_alttimes_unselect_btn);
			alt2Img.setImageResource(R.drawable.calendar_editevent_alttimes_select_btn);
			alt3Img.setImageResource(R.drawable.calendar_editevent_alttimes_unselect_btn);

			/*
			 * // SEND ACCEPT INFO if (currentCellData.alertCellAlt1DateTime !=
			 * null) { currentCellData.alertDateAccepted =
			 * AlertParseObjectParser .localDateTimeToServerTimeString
			 * (currentCellData.alertCellAlt1DateTime); }
			 */
			break;
		case 2:

			preferImg
					.setImageResource(R.drawable.calendar_editevent_alttimes_unselect_btn);
			alt2Img.setImageResource(R.drawable.calendar_editevent_alttimes_unselect_btn);
			alt3Img.setImageResource(R.drawable.calendar_editevent_alttimes_select_btn);

			/*
			 * // SEND ACCEPT INFO if (currentCellData.alertCellAlt2DateTime !=
			 * null) { currentCellData.alertDateAccepted =
			 * AlertParseObjectParser .localDateTimeToServerTimeString
			 * (currentCellData.alertCellAlt2DateTime); }
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
				cellData.calCellTitle = currentCellData.alertCellEventTitle;
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
			int cIndexTemp = altCurrentActive;// to keep previous index
			initATLTime();
			altCurrentActive = cIndexTemp;
			setAltCurrentActive();
			switch (altCurrentActive) {
			case 0:
				handleScrollToDate(eventPropertiesPre.startDateTime);
				break;
			case 1:
				handleScrollToDate(eventPropertiesAlt1.startDateTime);
				break;
			case 2:
				handleScrollToDate(eventPropertiesAlt2.startDateTime);
				break;
			default:
			}

			if (atlIndexs.size() > 0) {
				currentTabIndex = atlIndexs.get(0);
				listEvent.smoothScrollToPositionFromTop(currentTabIndex, 0);
				listEvent.post(showAcceptView);
			}

		} else if (v == nextArrowBtn) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(currentDate);
			Calendar calTemp = Calendar.getInstance();
			calTemp.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
					cal.get(Calendar.DAY_OF_MONTH) + 1, 0, 0, 0);
			currentDate = calTemp.getTime();
			aCalCellList.currentDateDidChanged(currentDate);
			dateDidChanged();
			int cIndexTemp = altCurrentActive;// to keep previous index
			initATLTime();
			altCurrentActive = cIndexTemp;
			setAltCurrentActive();
			switch (altCurrentActive) {
			case 0:
				handleScrollToDate(eventPropertiesPre.startDateTime);
				break;
			case 1:
				handleScrollToDate(eventPropertiesAlt1.startDateTime);
				break;
			case 2:
				handleScrollToDate(eventPropertiesAlt2.startDateTime);
				break;
			default:
			}

			if (atlIndexs.size() > 0) {
				currentTabIndex = atlIndexs.get(0);
				listEvent.smoothScrollToPositionFromTop(currentTabIndex, 0);
				listEvent.post(showAcceptView);
			}
		} else if (v == alt2Img) {
			Log.v("touch on date", "alt2Img");
			// TODO scroll and run actionRe... when touch on confirm button
			currentDate = eventPropertiesAlt1.startDateTime;
			aCalCellList.currentDateDidChanged(currentDate);
			dateDidChanged();
			initATLTime();
			handleScrollToDate(currentDate);
			// 2013-02-08 TAN: comment out to fix bug out of index //
			// currentTabIndex = atlIndexs.get(1); //
			// listEvent.smoothScrollToPositionFromTop(currentTabIndex, 0); //
			// listEvent.post(showAcceptView);
			altCurrentActive = 1;
			setAltCurrentActive();

		} else if (v == alt3Img) {
			Log.v("touch on date", "alt3Img");

			currentDate = eventPropertiesAlt2.startDateTime;
			aCalCellList.currentDateDidChanged(currentDate);
			dateDidChanged();
			initATLTime();
			handleScrollToDate(currentDate);
			// 2013-02-08 TAN: comment out to fix bug out of index //
			// currentTabIndex = atlIndexs.get(2); //
			// listEvent.smoothScrollToPositionFromTop(currentTabIndex, 0); //
			// listEvent.post(showAcceptView);
			altCurrentActive = 2;
			setAltCurrentActive();

		} else if (v == preferImg) {
			Log.v("touch on date", "preferImg");
			currentDate = eventPropertiesPre.startDateTime;
			aCalCellList.currentDateDidChanged(currentDate);
			dateDidChanged();
			initATLTime();
			handleScrollToDate(currentDate);
			// currentTabIndex = atlIndexs.get(0);
			// listEvent.smoothScrollToPositionFromTop(currentTabIndex, 0);
			// listEvent.post(showAcceptView);
			altCurrentActive = 0;
			setAltCurrentActive();
		}
	}

	// =========================================================================
	// 2013-02-08 TAN: create this method to handle scroll to alt time # start
	// =========================================================================

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

	// =========================================================================
	// 2013-02-08 TAN: create this method to handle scroll to alt time # end
	// =========================================================================

	private void actionRequestWithType(int type) {

		switch (type) {
		case EVENT_REQUEST_DECIDELATER: {

			finish();
			overridePendingTransition(R.anim.stand_by, R.anim.out_to_bottom);
			break;
		}
		case EVENT_REQUEST_DECLINE: {
			/*
			 * currentCellData.alertCellAcceptedDate = null;
			 * currentCellData.alertCellResponseStatus = EVENT_REQUEST_DECLINE;
			 * postEventAcceptedSent(AlertType.eventAccepted_Sent,
			 * currentCellData);
			 */
			break;
		}
		case EVENT_REQUEST_PREF: {
			// TODO
			// altCurrentActive = 1;
			// setAltCurrentActive();
			/*
			 * currentCellData.alertCellAcceptedDate =
			 * currentCellData.alertCellPreferredDatetime;
			 * currentCellData.alertTimeChosenLabel = EVENT_REQUEST_PREF;
			 * postEventAcceptedSent(AlertType.eventAccepted_Sent,
			 * currentCellData);
			 */
			break;
		}
		case EVENT_REQUEST_ALT1: {
			// TODO
			// altCurrentActive = 2;
			// setAltCurrentActive();
			/*
			 * currentCellData.alertCellAcceptedDate =
			 * currentCellData.alertCellAlt1DateTime;
			 * currentCellData.alertTimeChosenLabel = EVENT_REQUEST_ALT1;
			 * postEventAcceptedSent(AlertType.eventAccepted_Sent,
			 * currentCellData);
			 */
			break;
		}
		case EVENT_REQUEST_ALT2: {
			// TODO
			// altCurrentActive = 0;
			// setAltCurrentActive();
			/*
			 * currentCellData.alertCellAcceptedDate =
			 * currentCellData.alertCellAlt2DateTime;
			 * currentCellData.alertTimeChosenLabel = EVENT_REQUEST_ALT2;
			 * postEventAcceptedSent(AlertType.eventAccepted_Sent,
			 * currentCellData);
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

	// ========================================================
	// ATLAlertEventRequestCellDelegate - implement # START
	// ========================================================
	private int lastShowAcceptButton = -1;
	private int currentTabIndex = -1;

	@Override
	public void didTapToShowAcceptViewAtIndex(int index) {
		// TODO Auto-generated method stub
		Log.v("Task", "index: " + index);
		// Toast.makeText(this, "Touched Index " + index,
		// Toast.LENGTH_SHORT).show();
		boolean isALTCell = false;
		for (int i : atlIndexs) {
			if (i == index) {
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
			if (isALTCell) {
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

		if (isViewActive) {
			isViewActive = false;
			int size = atlIndexs.size();
			for (int i = 0; i < size; i++) {
				if (atlIndexs.get(i) == index) {
					prBar.setVisibility(View.VISIBLE);
					switch (i) {
					case 0:// prefer date
							// actionRequestWithType(EVENT_REQUEST_PREF);
						chosenItem = EVENT_REQUEST_PREF;
//						CalendarUtilities.saveEventByEventProperties(
//								eventPropertiesPre, this);
						
						eventController.respondToEventInvite(
								eventPropertiesPre.objectId,
								eventPropertiesPre.objectId);
						break;
					case 1:// alt2 date
							// actionRequestWithType(EVENT_REQUEST_ALT1);
						chosenItem = EVENT_REQUEST_ALT1;
//						CalendarUtilities.saveEventByEventProperties(
//								eventPropertiesAlt1, this);
						eventController.respondToEventInvite(
								eventPropertiesPre.objectId,
								eventPropertiesAlt1.objectId);
						break;
					case 2:// alt3 date
							// actionRequestWithType(EVENT_REQUEST_ALT2);
						chosenItem = EVENT_REQUEST_ALT2;
//						CalendarUtilities.saveEventByEventProperties(
//								eventPropertiesAlt2, this);
						eventController.respondToEventInvite(
								eventPropertiesPre.objectId,
								eventPropertiesAlt2.objectId);
						break;
					default:
						break;
					}
					break;
				}
			}
		}

	}

	@Override
	public void createCalendarEventCallBack(boolean success) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAllUserAnEventCallBack(
			HashMap<String, ArrayList<EventProperties>> userEvents,
			HashMap<String, ArrayList<String>> eventsMembers, boolean success) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getEventByPrimaryWebEventIdCallBack(boolean success,
			ArrayList<EventProperties> event,
			ArrayList<ItemUserProperties> invitees) {
		// TODO Auto-generated method stub

	}

	@Override
	public void respondToEventInviteCallBack(boolean success) {
		// TODO Auto-generated method stub
		prBar.setVisibility(View.INVISIBLE);
		if (success) {
			// Toast.makeText(ctx.getApplicationContext(),
			// "Respons to event successfully updated",
			// Toast.LENGTH_LONG).show();
			Log.v("Respons to event successfully updated",
					"Respons to event successfully updated");

			switch (chosenItem) {
			case EVENT_REQUEST_DECLINE:	 
			{
				AlertCellData cellData = AlertRequestEventSingleton.getInstance().getAlertCellData();
				ATLAlertModel alertModel = new ATLAlertModel(cellData);
				alertModel.alertAtlasId = cellData.getEventPrimaryKey()
						+"|"+ ITEM_USER_TASK_STATUS.DECLINED;
				alertModel.isHandled = true;
				alertModel.save();

//				CalendarUtilities.deleteGroupEventByDate(cellData.alertCellPreferredDatetime,
//						Utilities.ctx);
			}
				break;
			case EVENT_REQUEST_PREF:{
				// CalendarUtilities.saveEventByAlertDataAndDate(alert,
				// alert.alertCellPreferredDatetime, this);
				CalendarUtilities.saveEventByEventProperties(
						eventPropertiesPre, this);
				
				AlertCellData cellData = AlertRequestEventSingleton.getInstance().getAlertCellData();
				ATLAlertModel alertModel = new ATLAlertModel(cellData);
				alertModel.alertAtlasId = cellData.getEventPrimaryKey()
						+"|"+ ITEM_USER_TASK_STATUS.ACCEPTED;
				alertModel.isAccepted = true;
				alertModel.alertAcceptedDate = eventPropertiesPre.startDateTime;
				alertModel.save();
				
			}
				break;
			case EVENT_REQUEST_ALT1:{
				// CalendarUtilities.saveEventByAlertDataAndDate(alert,
				// alert.alertCellAlt1DateTime, this);
				CalendarUtilities.saveEventByEventProperties(
						eventPropertiesAlt1, this);
				
				AlertCellData cellData = AlertRequestEventSingleton.getInstance().getAlertCellData();
				ATLAlertModel alertModel = new ATLAlertModel(cellData);
				alertModel.alertAtlasId = cellData.getEventPrimaryKey()
						+"|"+ ITEM_USER_TASK_STATUS.ACCEPTED;
				alertModel.isAccepted = true;
				alertModel.alertAcceptedDate = eventPropertiesAlt1.startDateTime;
				alertModel.save();
				break;
			}
			case EVENT_REQUEST_ALT2:{
				// CalendarUtilities.saveEventByAlertDataAndDate(alert,
				// alert.alertCellAlt2DateTime, this);
//				break;
				CalendarUtilities.saveEventByEventProperties(
						eventPropertiesAlt2, this);
				
				AlertCellData cellData = AlertRequestEventSingleton.getInstance().getAlertCellData();
				ATLAlertModel alertModel = new ATLAlertModel(cellData);
				alertModel.isAccepted = true;
				alertModel.alertAcceptedDate = eventPropertiesAlt2.startDateTime;
				alertModel.alertAtlasId = cellData.getEventPrimaryKey()
						+"|"+ ITEM_USER_TASK_STATUS.ACCEPTED;
				alertModel.save();
			}
				break;

			default:
				break;
			}
			
			
			finish();
			overridePendingTransition(R.anim.stand_by, R.anim.out_to_bottom);
		} else {
			// Toast.makeText(ctx.getApplicationContext(),
			// "Respons to event failed to updated", Toast.LENGTH_LONG).show();
			
			finish();
			overridePendingTransition(R.anim.stand_by, R.anim.out_to_bottom);
			Log.v("Respons to event failed to updated",
					"Respons to event failed to updated");
		}
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

	// ========================================================
	// ATLAlertEventRequestCellDelegate - implement # END
	// ========================================================

}
