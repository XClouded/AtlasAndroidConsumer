//  ==================================================================================================================
//  AlertFragment.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================

package com.atlasapp.section_alerts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.atlasapp.common.ATLDragAndDropView;
import com.atlasapp.common.ATLEventCalendarModel;
import com.atlasapp.atlas_database.EventController;
import com.atlasapp.atlas_database.EventControllerCallBackInterface;
import com.atlasapp.atlas_database.EventProperties;
import com.atlasapp.atlas_database.ItemUserProperties;
import com.atlasapp.common.ATLConstants.AlertType;
import com.atlasapp.common.CalendarUtilities;
import com.atlasapp.section_appentry.R;
import com.atlasapp.section_main.ATLMultiSectionListView;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * @author Ryan Tan
 * 
 */
public class AlertFragment extends Fragment implements
		ATLAlertWebAccessCallBackInterface, OnClickListener, EventControllerCallBackInterface {

	private EventController eventController;
	LayoutInflater mInflater;
//	public Context mContext;
	View mLayout;
	ATLMultiSectionListView alertsList;
	View findFriendList;
	ImageButton reloadBtn;
	private FragmentActivity alertsActivity;

	public AlertFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		alertsActivity = getActivity();
		mInflater = LayoutInflater.from(alertsActivity);
		mLayout = (View) mInflater.inflate(R.layout.alerts, null);
		eventController = new EventController();
		/// initialize ATLMockDB to listen to call back methods
		/// from event controller....
		eventController.eventControllerCallBackListener = this;
		initAttributes();
		setListener();
//		bindingData(); // will be called in onResume();
		return mLayout;
	}

	@Override
	public void onResume() {
		super.onResume();
		// TODO
		bindingData();
		eventController.refreshUserCalendarEvents();
//		getNewAlertFromServer();
	}

	private void initAttributes() {
		// TODO Auto-generated method stub
		findFriendList = (View) mLayout
				.findViewById(R.id.alert_find_friend_view);
		alertsList = (ATLMultiSectionListView) mLayout
				.findViewById(R.id.listNotes);
		alertsList.setPinnedHeaderView(LayoutInflater.from(alertsActivity)
				.inflate(R.layout.listview_header, alertsList, false));

		reloadBtn = (ImageButton) mLayout
				.findViewById(R.id.alert_reload_imageButton);

	}

	private void bindingData() {
		// TODO Auto-generated method stub

		AlertCellList alertCellList = new AlertCellList();
		AlertListAdapter adaper = new AlertListAdapter(alertCellList,
				alertsActivity);
		alertsList.setAdapter(adaper);
		/*
		 * findFriendList .setAdapter(new ArrayAdapter<String>(getActivity(),
		 * android.R.layout.simple_list_item_1, new String[] {
		 * " Find Friends on Atlas", " Invite Friends on Atlas" }));
		 */
	}

	private void setListener() {
		// TODO Auto-generated method stub
		reloadBtn.setOnClickListener(this);
	}

	// ==================================================================================================================
	// ==================================================================================================================
	// BEGINE - ATLAlertWebAccessCallBackInterface
	// ==================================================================================================================
	// ==================================================================================================================

	@Override
	public void didGetDataFinish(Object data, int alertType, String result) {
		// TODO Auto-generated method stub
		// Reload data
		// Toast.makeText(alertsActivity, "" + "  " + alertType,
		// Toast.LENGTH_SHORT).show();
	}

	@Override
	public void didGetEventList(List<ParseObject> parseObjectList,
			int alertType, String result) {
		// TODO Auto-generated method stub
		switch (alertType) {
		case AlertType.eventInvited_Received: {
			AlertParseObjectParser.delegate = this;
			ArrayList<AlertCellData> newAlertList = AlertParseObjectParser
					.toListAlertCellData(parseObjectList);

			AlertCellList newAlertCellList = new AlertCellList();
			// if (newAlertList.size() > 0) {
			for (AlertCellData cellData : newAlertList) {
				// Save each AlertCellData get from server to Database
				newAlertCellList.addAlertCellDataToDatabase(cellData);
			}
			AlertListAdapter adaper = new AlertListAdapter(newAlertCellList,
					alertsActivity);
			alertsList.setAdapter(adaper);
			break;
		}
		case AlertType.eventAccepted_Received: {
			AlertParseObjectParser.delegate = this;
			ArrayList<AlertCellData> alertCellDataList = AlertParseObjectParser
					.alertCellDataListFromParseObjectList(parseObjectList,
							alertType);

			AlertCellList newAlertCellList = new AlertCellList();
			for (AlertCellData cellData : alertCellDataList) {
				// Save each AlertCellData get from server to Database
				newAlertCellList.addAlertCellDataToDatabase(cellData);

				// TODO manage when get accept received
				// 1. decline: set status for 3 event check isaccepted
				// 2. pref: set accept status for pref, delete alt1, alt2
				// 3. alt1: set accept status for alt1, delete pref, alt2
				// 4. alt2: set accept status for alt2, delete alt1, pref
				if (cellData.isAccepted) {
					CalendarUtilities.acceptEventByDate(
							cellData.alertCellAcceptedDate, alertsActivity);
				} else {
					CalendarUtilities.deleteGroupEventByDate(
							cellData.alertCellAcceptedDate, alertsActivity);
				}
			}
			AlertListAdapter adaper = new AlertListAdapter(newAlertCellList,
					alertsActivity);
			alertsList.setAdapter(adaper);
			break;
		}
		case AlertType.taskAssigned_Received: {
			AlertParseObjectParser.delegate = this;
			ArrayList<AlertCellData> alertCellDataList = AlertParseObjectParser
					.alertCellDataListFromParseObjectList(parseObjectList,
							alertType);

			AlertCellList newAlertCellList = new AlertCellList();
			for (AlertCellData cellData : alertCellDataList) {
				// Save each AlertCellData get from server to Database
				newAlertCellList.addAlertCellDataToDatabase(cellData);
			}
			AlertListAdapter adaper = new AlertListAdapter(newAlertCellList,
					alertsActivity);
			alertsList.setAdapter(adaper);
			break;
		}
		case AlertType.taskAccepted_Received: {
			AlertParseObjectParser.delegate = this;
			ArrayList<AlertCellData> alertCellDataList = AlertParseObjectParser
					.alertCellDataListFromParseObjectList(parseObjectList,
							alertType);

			AlertCellList newAlertCellList = new AlertCellList();
			for (AlertCellData cellData : alertCellDataList) {
				// Save each AlertCellData get from server to Database
				newAlertCellList.addAlertCellDataToDatabase(cellData);
			}
			AlertListAdapter adaper = new AlertListAdapter(newAlertCellList,
					alertsActivity);
			alertsList.setAdapter(adaper);
			break;
		}
		default:
			break;
		}
		// }

	}

	@Override
	public void didGetSenderName() {
		// TODO Auto-generated method stub
		// alertsList.getAdapter().notifyDataSetChanged();
	}

	// ==================================================================================================================
	// ==================================================================================================================
	// END - ATLAlertWebAccessCallBackInterface
	// ==================================================================================================================
	// ==================================================================================================================

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (ATLDragAndDropView.isAtLeft) {
			if (v == reloadBtn) {
				// Load data from Parse
				eventController.refreshUserCalendarEvents();
	//			getAllEvents();
	//			getNewAlertFromServer();
			}
		}
		else{
			// Do nothing here
//			Toast.makeText(getActivity(), "Notice \nAlerts is not visible",
//					Toast.LENGTH_SHORT).show();
		}
	}

	private void getNewAlertFromServer() {
		// TODO Auto-generated method stub

		getEventsInvitedRecieved();
		getEventsAccepted_Recieved();

		getTaskListAssignedReceived();

		getTaskListAcceptedReceived();
		getTaskListCompletedReceived();
	}

	private void getTaskListCompletedReceived() {
		// TODO Auto-generated method stub
		ATLAlertWebAccess taskCompletedWebAccess = new ATLAlertWebAccess();
		taskCompletedWebAccess.delegate = this;
		taskCompletedWebAccess.getPage_AsyncWithType(
				AlertType.taskComplete_Received, null);
	}

	private void getTaskListAcceptedReceived() {
		// TODO Auto-generated method stub
		ATLAlertWebAccess taskAcceptedWebAccess = new ATLAlertWebAccess();
		taskAcceptedWebAccess.delegate = this;
		taskAcceptedWebAccess.getPage_AsyncWithType(
				AlertType.taskAccepted_Received, null);
	}

	private void getTaskListAssignedReceived() {
		// TODO Auto-generated method stub
		ATLAlertWebAccess taskAssignedWebAccess = new ATLAlertWebAccess();
		taskAssignedWebAccess.delegate = this;
		taskAssignedWebAccess.getPage_AsyncWithType(
				AlertType.taskAssigned_Received, null);
	}

	private void getEventsAccepted_Recieved() {
		// TODO Auto-generated method stub
		// Use Settings screen to register a username and email for testing!

		// ==================================================================================================================
		// ==================================================================================================================
		// Protomess: Get EventAccepted
		// ==================================================================================================================
		// ==================================================================================================================

		// CHANGE THIS TO USE ACTUAL DATA:
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			ATLAlertWebAccess eventAcceptedWebAccess = new ATLAlertWebAccess();
			eventAcceptedWebAccess.userId = currentUser.getObjectId();
			eventAcceptedWebAccess.inviter = currentUser.getObjectId();
			eventAcceptedWebAccess.userEmail = currentUser.getEmail();
			eventAcceptedWebAccess.delegate = this;
			eventAcceptedWebAccess.getPage_AsyncWithType(
					AlertType.eventAccepted_Received, null);
		} else {

		}
		// ==================================================================================================================
		// ==================================================================================================================
		// Protomess: END Get EventAccepted
		// ==================================================================================================================
		// ==================================================================================================================
	}

	private void getEventsInvitedRecieved() {
		// TODO Auto-generated method stub
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			// do stuff with the user

			ATLAlertWebAccess eventInviteWebAccess = new ATLAlertWebAccess();
			eventInviteWebAccess.userId = currentUser.getObjectId();
			eventInviteWebAccess.userEmail = currentUser.getEmail();

			eventInviteWebAccess.delegate = this;
			eventInviteWebAccess.getPage_AsyncWithType(
					AlertType.eventInvited_Received, null);
		} else {

		}
	}

	@Override
	public void didPostDataFinish(Object data, int alertType, String result) {
		// TODO Auto-generated method stub
//		AlertCellData alertCellData = (AlertCellData) data;
//		CalendarUtilities.saveEventByAlertDataAndDate(alertCellData, alertCellData.alertCellPreferredDatetime, alertsActivity);
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
		Log.v("getAllUserAnEventCallBack","getAllUserAnEventCallBack");

		bindingData();
		if (success)
		{
			if (userEvents!=null && userEvents.size()>0)
			{
				  Toast.makeText(alertsActivity.getApplicationContext(), "number of new userEvents found"+userEvents.size(), Toast.LENGTH_LONG).show();

			}
			else
			{
				  Toast.makeText(alertsActivity.getApplicationContext(), "No NEW Event found", Toast.LENGTH_LONG).show();

			}
		}
		else
		{
			  Toast.makeText(alertsActivity.getApplicationContext(), "FAILED No Event found ", Toast.LENGTH_LONG).show();

		}
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
		bindingData();
		if (success)
		{
			if (allEventsRetrievedFromItemUser!=null && allEventsRetrievedFromItemUser.size()>0)
			{
				  Toast.makeText(alertsActivity.getApplicationContext(), "number of new Events found"+allEventsRetrievedFromItemUser.size(), Toast.LENGTH_LONG).show();

			}
			else
			{
				  Toast.makeText(alertsActivity.getApplicationContext(), "No NEW Event found", Toast.LENGTH_LONG).show();

			}
		}
		else
		{
			  Toast.makeText(alertsActivity.getApplicationContext(), "FAILED No Event found ", Toast.LENGTH_LONG).show();

		}
	}
}
