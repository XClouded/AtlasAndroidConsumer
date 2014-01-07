//  ==================================================================================================================
//  ATLAlertFragment_2.java
//  ATLAS
//  Copyright (c) 2012 ATLAS Apps. All rights reserved.
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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atlasapp.atlas_database.EventController;
import com.atlasapp.atlas_database.EventControllerCallBackInterface;
import com.atlasapp.atlas_database.EventProperties;
import com.atlasapp.atlas_database.ItemUserProperties;
import com.atlasapp.common.ATLColor;
import com.atlasapp.section_appentry.R;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

/**
 * @author Ryan Tan
 * 
 */
public class ATLAlertFragment_2 extends Fragment implements
		EventControllerCallBackInterface {

	private EventController eventController;
	LayoutInflater mInflater;
	// public Context mContext;
	View mLayout;
	// PullToRefreshListView alertsList;
	View findFriendList;
	private Activity alertsActivity;

	TwoStateTextView headerSectionYourMoveView;
	TwoStateTextView headerSectionPendingView;
	TwoStateTextView headerSectionBookedView;
	private PullToRefreshListView alertYourMoveList;
	private PullToRefreshListView alertPendingList;
	private PullToRefreshListView alertBookedList;
	private ATLAlertListAdapter_2 adaper1;
	private ATLAlertListAdapter_2 adaper2;
	private ATLAlertListAdapter_2 adaper3;
	private ATLAlertCellList_2 alertCellList_2 = null;

	private static ATLAlertFragment_2 instance;

	public static ATLAlertFragment_2 getInstance() {
		if (instance == null) {
			instance = new ATLAlertFragment_2();
		}
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		alertsActivity = getActivity();
		mInflater = LayoutInflater.from(alertsActivity);
		mLayout = (View) mInflater.inflate(R.layout.alerts_3, null);

		eventController = new EventController();
		eventController.eventControllerCallBackListener = this;

		initAttributes();

		headerSectionYourMoveView.stateChanged();
		headerSectionPendingView.resetState();
		headerSectionBookedView.resetState();

		setListener();
//		bindingData(); // will be called in onResume();
		return mLayout;
	}

	@Override
	public void onResume() {
		super.onResume();
		// TODO
		 bindingData();
		 refresh();
//		refresh();
		// getNewAlertFromServer();
	}

	private void initAttributes() {
		// TODO Auto-generated method stub
		findFriendList = (View) mLayout
				.findViewById(R.id.alert_find_friend_view);
		// alertsList = (PullToRefreshListView) mLayout
		// .findViewById(R.id.pull_to_refresh_listview);

		alertYourMoveList = (PullToRefreshListView) mLayout
				.findViewById(R.id.pull_to_refresh_yourmove_listview);
		alertPendingList = (PullToRefreshListView) mLayout
				.findViewById(R.id.pull_to_refresh_pending_listview);
		alertPendingList.setVisibility(View.GONE);
		alertBookedList = (PullToRefreshListView) mLayout
				.findViewById(R.id.pull_to_refresh_booked_listview);
		alertBookedList.setVisibility(View.GONE);

		headerSectionYourMoveView = new TwoStateTextView(alertsActivity,
				R.drawable.alerts_segmented_controls_red,
				(TextView) mLayout
						.findViewById(R.id.alert_header_sections_yourmove_view));
		headerSectionPendingView = new TwoStateTextView(alertsActivity,
				R.drawable.alerts_segmented_controls_yellow,
				(TextView) mLayout
						.findViewById(R.id.alert_header_sections_pending_view));
		headerSectionBookedView = new TwoStateTextView(alertsActivity,
				R.drawable.alerts_segmented_controls_green,
				(TextView) mLayout
						.findViewById(R.id.alert_header_sections_booked_view));

	}

	private void bindingData() {
		// TODO Auto-generated method stub
		if(alertCellList_2 == null){
			alertCellList_2 = new ATLAlertCellList_2(new ArrayList<ATLAlert>());
		}

		adaper1 = new ATLAlertListAdapter_2(alertCellList_2.yourmoveList,
				alertsActivity);
		alertYourMoveList.setAdapter(adaper1);

		adaper2 = new ATLAlertListAdapter_2(alertCellList_2.pendingList,
				alertsActivity);
		alertPendingList.setAdapter(adaper2);

		adaper3 = new ATLAlertListAdapter_2(alertCellList_2.bookingList,
				alertsActivity);
		alertBookedList.setAdapter(adaper3);

		/*
		 * findFriendList .setAdapter(new ArrayAdapter<String>(getActivity(),
		 * android.R.layout.simple_list_item_1, new String[] {
		 * " Find Friends on Atlas", " Invite Friends on Atlas" }));
		 */
	}

	private void bindingDataByAlertList(ArrayList<ATLAlert> alertList) {
		// TODO Auto-generated method stub
		alertCellList_2 = new ATLAlertCellList_2(alertList);

		adaper1 = new ATLAlertListAdapter_2(alertCellList_2.yourmoveList,
				alertsActivity);
		alertYourMoveList.setAdapter(adaper1);

		adaper2 = new ATLAlertListAdapter_2(alertCellList_2.pendingList,
				alertsActivity);
		alertPendingList.setAdapter(adaper2);

		adaper3 = new ATLAlertListAdapter_2(alertCellList_2.bookingList,
				alertsActivity);
		alertBookedList.setAdapter(adaper3);

	}

	private void setListener() {
		// TODO Auto-generated method stub
		alertYourMoveList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView,
							View view, int i, long l) {
						Intent intent = new Intent();
						ATLAlert mCellData = alertCellList_2.yourmoveList
								.get(i);
						AlertRequestEventSingleton.getInstance().setAlert(
								mCellData);
						intent.setClass(alertsActivity,
								ATLAlertEventRequest_2.class);
						alertsActivity.startActivity(intent);
						((Activity) alertsActivity).overridePendingTransition(
								R.anim.in_from_bottom, R.anim.stand_by);
					}
				});
		alertYourMoveList.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				refresh();
				// Your code to refresh the list contents goes here

				// Make sure you call listView.onRefreshComplete()
				// when the loading is done. This can be done from here or any
				// other place, like on a broadcast receive from your loading
				// service or the onPostExecute of your AsyncTask.

				// For the sake of this sample, the code will pause here to
				// force a delay when invoking the refresh
			}
		});

		alertPendingList.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				refresh();
			}
		});

		alertPendingList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView,
							View view, int i, long l) {
// NGHIA COMMENT FOR TESTING
//						Intent intent = new Intent();
//						intent.setClass(alertsActivity,
//								ATLAlertRespondMatrix.class);
//						alertsActivity.startActivity(intent);
//						((Activity) alertsActivity).overridePendingTransition(
//								R.anim.in_from_bottom, R.anim.stand_by);
					}
				});

		alertBookedList.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				refresh();
			}
		});
		alertBookedList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView,
							View view, int i, long l) {
// NGHIA COMMENT FOR TESTING
//						Intent intent = new Intent();
//						intent.setClass(alertsActivity,
//								ATLAlertRespondMatrix.class);
//						alertsActivity.startActivity(intent);
//						((Activity) alertsActivity).overridePendingTransition(
//								R.anim.in_from_bottom, R.anim.stand_by);
					}
				});

		headerSectionYourMoveView.imgTextView
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						headerSectionYourMoveView.stateChanged();
						headerSectionPendingView.resetState();
						headerSectionBookedView.resetState();
						alertYourMoveList.setVisibility(View.VISIBLE);
						alertPendingList.setVisibility(View.GONE);
						alertBookedList.setVisibility(View.GONE);
					}
				});

		headerSectionPendingView.imgTextView
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						headerSectionPendingView.stateChanged();
						headerSectionYourMoveView.resetState();
						headerSectionBookedView.resetState();

						alertYourMoveList.setVisibility(View.GONE);
						alertPendingList.setVisibility(View.VISIBLE);
						alertBookedList.setVisibility(View.GONE);
					}
				});

		headerSectionBookedView.imgTextView
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						headerSectionBookedView.stateChanged();
						headerSectionYourMoveView.resetState();
						headerSectionPendingView.resetState();

						alertYourMoveList.setVisibility(View.GONE);
						alertPendingList.setVisibility(View.GONE);
						alertBookedList.setVisibility(View.VISIBLE);
					}
				});
	}

	/*
	 * //
	 * ========================================================================
	 * ========================================== //
	 * ============================
	 * ==============================================
	 * ======================================== // BEGINE -
	 * ATLAlertWebAccessCallBackInterface //
	 * ====================================
	 * ======================================
	 * ======================================== //
	 * ==============================
	 * ============================================
	 * ========================================
	 * 
	 * @Override public void didGetDataFinish(Object data, int alertType, String
	 * result) { // TODO Auto-generated method stub // Reload data //
	 * Toast.makeText(alertsActivity, "" + "  " + alertType, //
	 * Toast.LENGTH_SHORT).show(); }
	 * 
	 * @Override public void didGetEventList(List<ParseObject> parseObjectList,
	 * int alertType, String result) { // TODO Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public void didGetSenderName() { // TODO Auto-generated method
	 * stub // alertsList.getAdapter().notifyDataSetChanged(); }
	 * 
	 * //
	 * ========================================================================
	 * ========================================== //
	 * ============================
	 * ==============================================
	 * ======================================== // END -
	 * ATLAlertWebAccessCallBackInterface //
	 * ====================================
	 * ======================================
	 * ======================================== //
	 * ==============================
	 * ============================================
	 * ========================================
	 */
	boolean isRefreshing = false;

	public void refresh() {
		if (!isRefreshing) {
			isRefreshing = true;
			eventController.refreshUserCalendarEvents();
		}
	}

	/*
	 * private void getNewAlertFromServer() { // TODO Auto-generated method stub
	 * 
	 * getEventsInvitedRecieved(); getEventsAccepted_Recieved();
	 * 
	 * getTaskListAssignedReceived();
	 * 
	 * getTaskListAcceptedReceived(); getTaskListCompletedReceived(); }
	 * 
	 * private void getTaskListCompletedReceived() { // TODO Auto-generated
	 * method stub ATLAlertWebAccess taskCompletedWebAccess = new
	 * ATLAlertWebAccess(); taskCompletedWebAccess.delegate = this;
	 * taskCompletedWebAccess.getPage_AsyncWithType(
	 * AlertType.taskComplete_Received, null); }
	 * 
	 * private void getTaskListAcceptedReceived() { // TODO Auto-generated
	 * method stub ATLAlertWebAccess taskAcceptedWebAccess = new
	 * ATLAlertWebAccess(); taskAcceptedWebAccess.delegate = this;
	 * taskAcceptedWebAccess.getPage_AsyncWithType(
	 * AlertType.taskAccepted_Received, null); }
	 * 
	 * private void getTaskListAssignedReceived() { // TODO Auto-generated
	 * method stub ATLAlertWebAccess taskAssignedWebAccess = new
	 * ATLAlertWebAccess(); taskAssignedWebAccess.delegate = this;
	 * taskAssignedWebAccess.getPage_AsyncWithType(
	 * AlertType.taskAssigned_Received, null); }
	 * 
	 * private void getEventsAccepted_Recieved() { // TODO Auto-generated method
	 * stub // Use Settings screen to register a username and email for testing!
	 * 
	 * //
	 * ========================================================================
	 * ========================================== //
	 * ============================
	 * ==============================================
	 * ======================================== // Protomess: Get EventAccepted
	 * //
	 * ========================================================================
	 * ========================================== //
	 * ============================
	 * ==============================================
	 * ========================================
	 * 
	 * // CHANGE THIS TO USE ACTUAL DATA: ParseUser currentUser =
	 * ParseUser.getCurrentUser(); if (currentUser != null) { ATLAlertWebAccess
	 * eventAcceptedWebAccess = new ATLAlertWebAccess();
	 * eventAcceptedWebAccess.userId = currentUser.getObjectId();
	 * eventAcceptedWebAccess.inviter = currentUser.getObjectId();
	 * eventAcceptedWebAccess.userEmail = currentUser.getEmail();
	 * eventAcceptedWebAccess.delegate = this;
	 * eventAcceptedWebAccess.getPage_AsyncWithType(
	 * AlertType.eventAccepted_Received, null); } else {
	 * 
	 * } //
	 * ======================================================================
	 * ============================================ //
	 * ==========================
	 * ================================================
	 * ======================================== // Protomess: END Get
	 * EventAccepted //
	 * ==========================================================
	 * ======================================================== //
	 * ==============
	 * ============================================================
	 * ======================================== }
	 * 
	 * private void getEventsInvitedRecieved() { // TODO Auto-generated method
	 * stub ParseUser currentUser = ParseUser.getCurrentUser(); if (currentUser
	 * != null) { // do stuff with the user
	 * 
	 * ATLAlertWebAccess eventInviteWebAccess = new ATLAlertWebAccess();
	 * eventInviteWebAccess.userId = currentUser.getObjectId();
	 * eventInviteWebAccess.userEmail = currentUser.getEmail();
	 * 
	 * eventInviteWebAccess.delegate = this;
	 * eventInviteWebAccess.getPage_AsyncWithType(
	 * AlertType.eventInvited_Received, null); } else {
	 * 
	 * } }
	 * 
	 * @Override public void didPostDataFinish(Object data, int alertType,
	 * String result) { // TODO Auto-generated method stub // AlertCellData
	 * alertCellData = (AlertCellData) data; //
	 * CalendarUtilities.saveEventByAlertDataAndDate(alertCellData, //
	 * alertCellData.alertCellPreferredDatetime, alertsActivity); }
	 */
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

	private void populateAlert(
			HashMap<String, ArrayList<EventProperties>> allEventsRetrievedFromItemUser,
			ArrayList<ItemUserProperties> allUserItemUsersPropertiesRefreshedList) {
		// TODO Auto-generated method stub

		Iterator iterator = allEventsRetrievedFromItemUser.keySet().iterator();
		String keyColumn;
		ArrayList<EventProperties> valueColumn = null;
		// ArrayList<String> webEventIds = new ArrayList<String>();
		ArrayList<ATLAlert> alertList = new ArrayList<ATLAlert>();
		while (iterator.hasNext()) {
			keyColumn = (String) iterator.next();
			valueColumn = allEventsRetrievedFromItemUser.get(keyColumn);
			ATLAlert alert = new ATLAlert();
			String web_event_id1 = "";
			String web_event_id2 = "";
			String web_event_id3 = "";

			for (EventProperties event : valueColumn) {
				switch (event.displayOrder) {
				case 0:
					web_event_id1 = event.webEventId;
					break;
				case 1:
					web_event_id2 = event.webEventId;
					break;
				case 2:
					web_event_id3 = event.webEventId;
					break;
				default:
					break;
				}
				alert.eventList.add(event);
			}

			for (ItemUserProperties item : allUserItemUsersPropertiesRefreshedList) {
				if (item.webItemId.equals(web_event_id1)
						|| item.webItemId.equals(web_event_id2)
						|| item.webItemId.equals(web_event_id3)) {
					alert.itemUserList.add(item);
				}
			}
			alert.load();
			alertList.add(alert);
		}
		bindingDataByAlertList(alertList);

	}

	@Override
	public void getEventByPrimaryWebEventIdCallBack(boolean success,
			ArrayList<EventProperties> events,
			ArrayList<ItemUserProperties> itemUsers) {
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
		// TODO Auto-generated method stu
		Log.v("getAllUserEventsCallBack", "getAllUserEventsCallBack");
		if (success) {
			if (allEventsRetrievedFromItemUser != null
					&& allEventsRetrievedFromItemUser.size() > 0) {
				populateAlert(allEventsRetrievedFromItemUser,
						allUserItemUsersPropertiesRefreshedList);
				isRefreshing = false;
				alertYourMoveList.onRefreshComplete();
				alertPendingList.onRefreshComplete();
				alertBookedList.onRefreshComplete();
				// alertsList.onRefreshComplete();

				Toast.makeText(alertsActivity.getApplicationContext(),
						" Alert status updated successfully!", Toast.LENGTH_LONG).show();

			} else {
				Toast.makeText(alertsActivity.getApplicationContext(),
						"Alert status updated successfully!", Toast.LENGTH_LONG).show();

			}
		} else {
			Toast.makeText(alertsActivity.getApplicationContext(),
					"Alert status updated unsuccessfully!", Toast.LENGTH_LONG).show();

		}

	}

	private class TwoStateTextView extends RelativeLayout implements
			OnClickListener {
		public static final int ACTIVE_STATE = 1;
		public static final int IN_ACTIVE_STATE = 0;
		public TextView imgTextView;
		int imgID0;
		int imgID1;
		public int state = IN_ACTIVE_STATE;

		public TwoStateTextView(Context context, int imgID1, TextView imgButon) {
			super(context);
			// TODO Auto-generated constructor stub
			imgTextView = new TextView(context);
			this.imgID1 = imgID1;
			this.imgTextView = imgButon;
			imgTextView.setBackgroundResource(Color.TRANSPARENT);
			imgTextView.setTextColor(ATLColor.LTGRAY);
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (state == IN_ACTIVE_STATE) {
				state = ACTIVE_STATE;
				imgTextView.setBackgroundResource(imgID1);
			}

		}

		public void resetState() {
			state = IN_ACTIVE_STATE;
			imgTextView.setBackgroundResource(Color.TRANSPARENT);
			imgTextView.setTextColor(ATLColor.LTGRAY);
		}

		public void stateChanged() {
			if (state == IN_ACTIVE_STATE) {
				state = ACTIVE_STATE;
				imgTextView.setBackgroundResource(imgID1);
				imgTextView.setTextColor(ATLColor.BLACK);
			}
		}

	}

}
