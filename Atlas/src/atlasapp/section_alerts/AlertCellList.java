//  ==================================================================================================================
//  AlertCellList.java
//  AtlasConsumerAndroid
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================

package atlasapp.section_alerts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import android.R.string;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;
import atlasapp.common.ATLConstants.AlertType;
import atlasapp.common.CalendarUtilities;
import atlasapp.common.DateTimeUtilities;
import atlasapp.common.Utilities;
import atlasapp.database.DatabaseConstants.ITEM_USER_TASK_STATUS;
import atlasapp.database.EventController;
import atlasapp.database.EventControllerCallBackInterface;
import atlasapp.database.EventProperties;
import atlasapp.database.ItemUserProperties;
import atlasapp.model.ATLAlertModel;
import atlasapp.model.ATLEventCalendarModel;
import atlasapp.section_tasks.ATLTaskPriorityScrollListAdapter;


/**
 * @author Ryan Tan
 * 
 */
public class AlertCellList implements EventControllerCallBackInterface {

	private EventController eventController;

	public ArrayList<ATLEventCalendarModel> alertList;
	public HashMap<String, ATLEventCalendarModel> newAlertList;
	public HashMap<String, ATLEventCalendarModel> pastAlertList;

	// PROPERTIES
	public ArrayList<AlertCellData> alertListArray;

	public ArrayList<AlertCellData> newAlertListArray;
	public ArrayList<AlertCellData> pastAlertListArray;

	public int alertListCount;
	public Date alertsListDate;
	public boolean alertListSimulate;

	boolean show_Displayed = false;
	boolean show_Read = false;
	boolean show_Handled = false;
	boolean show_All = false;

	// METHODS

	public AlertCellList() {
		// TODO Auto-generated constructor stub
		// SIMULATION: TURN OFF WHEN REAL DATA IS AVAILABLE
		// ==================================<<<<<<
		// WHEN ON, FAKE CELL DATA WILL BE CREATED

		eventController = new EventController();
		// / initialize ATLMockDB to listen to call back methods
		// / from event controller....
		eventController.eventControllerCallBackListener = this;

		this.alertListSimulate = false;
		this.alertListArray = new ArrayList<AlertCellData>();
		newAlertListArray = new ArrayList<AlertCellData>();
		pastAlertListArray = new ArrayList<AlertCellData>();
		this.alertsListDate = new Date();

		alertList = new ArrayList<ATLEventCalendarModel>();
		newAlertList = new HashMap<String, ATLEventCalendarModel>();
		pastAlertList = new HashMap<String, ATLEventCalendarModel>();

	}

	public boolean load() {
		// load data for one day
		if (this.alertListSimulate) {

			initFakeDataWhenSimulate();

		} // END IF SIMULATE

		else {

			alertList = new ArrayList<ATLEventCalendarModel>();
			newAlertList = new HashMap<String, ATLEventCalendarModel>();
			pastAlertList = new HashMap<String, ATLEventCalendarModel>();
			HashMap<String, ATLEventCalendarModel> alertHashMap = eventController
					.getAllUserCalendarInvites();

			Iterator iterator = alertHashMap.keySet().iterator();
			String keyColumn;
			ATLEventCalendarModel valueColumn;
			ArrayList<String> webEventIds = new ArrayList<String>();
			while (iterator.hasNext()) {
				// Toast.makeText(alertsActivity.getApplicationContext(),
				// "Event :", Toast.LENGTH_SHORT).show();
				Log.v("Event ", "Events number:" + alertList.size());
				keyColumn = (String) iterator.next();
				valueColumn = alertHashMap.get(keyColumn);
				/*
				Log.v("Event Title:", "Event Title:" + valueColumn.title);
				Log.v("Event webEventId:", "Event webEventId:"
						+ valueColumn.primaryWebEventId + "Event status:" + valueColumn.status);
				webEventIds.add(valueColumn.webEventId);

				if (!valueColumn.atlasId.equals(AtlasAndroidUser
						.getParseUserID())) {
					alertList.add(valueColumn);
				} else {
					getEventByPrimaryWebEventId(valueColumn.primaryWebEventId);
				}
				*/
				// alertList.add(valueColumn);
				// Toast.makeText(alertsActivity.getApplicationContext(),
				// "Event Title:"+valueColumn.title, Toast.LENGTH_SHORT).show();
				// Toast.makeText(alertsActivity.getApplicationContext(),
				// "Event webEventId:"+valueColumn.webEventId,
				// Toast.LENGTH_SHORT).show();

			}

			// ArrayList<ItemUserProperties> items =
			// ATLItemUserModel.getAllItemUserRecordsByWebItemIds(webEventIds);
			// for(ItemUserProperties item: items){
			//
			// Log.v("item status:", "item status:"
			// + item.status);
			// }
			alertListCount = alertList.size();
			/*
			 * this.alertListArray = new ArrayList<AlertCellData>();
			 * this.newAlertListArray = new ArrayList<AlertCellData>();
			 * this.pastAlertListArray = new ArrayList<AlertCellData>();
			 * 
			 * ATLAlertDatabaseAdapter dbHelper = new ATLAlertDatabaseAdapter();
			 * ArrayList<ATLAlertModel> list =
			 * dbHelper.loadAllAlertInDatabase();
			 * 
			 * for (ATLAlertModel alertModel : list) { AlertCellData cellData =
			 * new AlertCellData(alertModel); addCell(cellData); }
			 */
		}
		/*
		 * Collections.sort(alertListArray, new ATLAlertCellDataComparator());
		 * Collections.sort(newAlertListArray, new
		 * ATLAlertCellDataComparator()); Collections.sort(pastAlertListArray,
		 * new ATLAlertCellDataComparator()); alertListCount =
		 * alertListArray.size();
		 */
		return true;
	}

	public void loadNewAndPastList() {

	}

	public void getEventByPrimaryWebEventId(String webItemId) {
		eventController.getEventByPrimaryWebEventId(webItemId);
	}

	@Override
	public void getEventByPrimaryWebEventIdCallBack(boolean success,
			ArrayList<EventProperties> event,
			ArrayList<ItemUserProperties> invitees) {
		Log.v("getEventByPrimaryWebEventIdCallBack",
				"getEventByPrimaryWebEventIdCallBack");
		if (success && event != null && event.size() > 0 && invitees != null
				&& invitees.size() > 0) {
			// Toast.makeText(ctx.getApplicationContext(),
			// "Event Found!", Toast.LENGTH_LONG).show();
			// Toast.makeText(ctx.getApplicationContext(),
			// "Event Title: "+event.get(0).title, Toast.LENGTH_LONG).show();

			String chosenEventId = "";
			boolean isAccepted = false;
			boolean isDecline = false;
			for (ItemUserProperties invitee : invitees) {
				if (invitee != null) {
					// Log.v("ItemUserProperties ObjectId:", invitee.objectId);
					Log.v("ItemUserProperties status:", "status : "
							+ invitee.status);
					 Log.v("ItemUserProperties atlasId:", "atlasId : "
					 + invitee.atlasId);
					// Log.v("ItemUserProperties priorityOrder:",
					// "priorityOrder : " + invitee.priorityOrder);
					if (invitee.status == ITEM_USER_TASK_STATUS.COMPLITED || invitee.status == ITEM_USER_TASK_STATUS.ACCEPTED) {
						Log.v("invitee.objectId:equals", "invitee.objectId: "
								+ invitee.objectId);
						// eventController.bookEvent(primaryEventId,
						// invitee.webItemId);
						isAccepted = true;
						chosenEventId = invitee.webItemId;
						break;
						// TODO save to calendar
					}else if(invitee.status == ITEM_USER_TASK_STATUS.DECLINED){
						isDecline = true;
						chosenEventId = invitee.webItemId;
					}
				}
			}
			for (EventProperties properties : event) {
				if (properties != null) {
					Log.v("EventProperties ObjectId:",
							properties.primaryWebEventId);
					Log.v("EventProperties status:", "event status : "
							+ properties.status);
					if (!chosenEventId.equals("")) {
						Log.v("EventProperties chosenEventId:",
								"event chosenEventId : " + chosenEventId
										+ " properties.objectId : "
										+ properties.objectId);
						// primaryEventId = properties.primaryWebEventId;
						if (chosenEventId.equals(properties.objectId)) {
							if(isAccepted){
								CalendarUtilities.acceptEventByDate(
										properties.startDateTime, Utilities.ctx);
							}else if(isDecline){
								CalendarUtilities.deleteGroupEventByDate(
										properties.startDateTime, Utilities.ctx);
							}
							break;
						}
					}
				}
			}

		} else {
			// Toast.makeText(ctx.getApplicationContext(),
			// "No Event found by that primary key:", Toast.LENGTH_LONG).show();

		}

	}

	private void initFakeDataWhenSimulate() {
		// fake the cells
		AlertCellData newCell1 = new AlertCellData();
		newCell1.alertCellPreferredDatetime = this.alertsListDate;
		newCell1.alertCellEventTitle = "Nghia's birthday";
		newCell1.isSentMessageSuccessfully = true;
		newCell1.alertCellRecieverName = "Ryan Tan";
		newCell1.currentType = AlertType.eventInvited_Sent;
		newCell1.isHandled = false;
		this.addCell(newCell1);

		// fake the cells
		AlertCellData newCell2 = new AlertCellData();
		newCell2.alertCellPreferredDatetime = this.alertsListDate;
		newCell2.alertCellEventTitle = "Nghia's birthday";
		newCell2.isSentMessageSuccessfully = false;
		newCell2.alertCellRecieverName = "Nguyen Minh Tan";
		newCell2.currentType = AlertType.eventInvited_Sent;
		newCell2.isHandled = false;
		this.addCell(newCell2);

		// fake the cells
		AlertCellData newCell3 = new AlertCellData();
		newCell3.alertCellPreferredDatetime = this.alertsListDate;
		newCell3.alertCellEventTitle = "Nghia's birthday";
		newCell3.currentType = AlertType.eventInvited_Received;
		newCell3.alertCellSenderName = "Tan Nguyen";
		newCell3.alertCellDescription = "Meeting about Resources";
		newCell3.isHandled = false;
		this.addCell(newCell3);

		// fake the cells
		AlertCellData newCell4 = new AlertCellData();
		newCell4.alertCellPreferredDatetime = this.alertsListDate;
		newCell4.alertCellEventTitle = "Nghia's birthday";
		newCell4.isSentMessageSuccessfully = true;
		newCell4.currentType = AlertType.eventAccepted_Sent;
		newCell4.alertCellAcceptedDate = new Date();
		newCell4.alertCellDescription = "Meeting ATLAS";
		newCell4.isHandled = false;
		this.addCell(newCell4);

		// fake the cells
		AlertCellData newCell5 = new AlertCellData();
		newCell5.alertCellPreferredDatetime = this.alertsListDate;
		newCell5.alertCellEventTitle = "Nghia's birthday";
		newCell5.isSentMessageSuccessfully = false;
		newCell5.currentType = AlertType.eventAccepted_Sent;
		newCell5.alertCellAcceptedDate = new Date();
		newCell5.alertCellDescription = "Meeting ATLAS";
		newCell5.alertCellRecieverName = "Tan Nguyen";
		newCell5.isHandled = false;
		this.addCell(newCell5);

		// fake the cells
		AlertCellData newCell6 = new AlertCellData();
		newCell6.alertCellPreferredDatetime = this.alertsListDate;
		newCell6.alertCellEventTitle = "Nghia's birthday";
		newCell6.isSentMessageSuccessfully = false;
		newCell6.currentType = AlertType.eventAccepted_Received;
		newCell6.alertCellAcceptedDate = new Date();
		newCell6.alertCellDescription = "Meeting ATLAS";
		newCell6.alertCellSenderName = "Tan Nguyen";
		newCell6.isHandled = false;
		this.addCell(newCell6);

		// fake the cells //eventAccepted_Sent
		AlertCellData newCell7 = new AlertCellData();
		newCell7.alertCellPreferredDatetime = this.alertsListDate;
		newCell7.alertCellEventTitle = "Nghia's birthday";
		newCell7.isSentMessageSuccessfully = false;
		newCell7.currentType = AlertType.eventRejected_Sent;
		newCell7.alertCellAcceptedDate = new Date();
		newCell7.alertCellDescription = "Meeting ATLAS";
		newCell7.alertCellSenderName = "Tan Nguyen";
		newCell7.alertCellRecieverName = "Ryan";
		newCell7.isHandled = false;
		this.addCell(newCell7);
		// fake the cells
		AlertCellData newCell8 = new AlertCellData();
		newCell8.alertCellPreferredDatetime = this.alertsListDate;
		newCell8.alertCellEventTitle = "Nghia's birthday";
		newCell8.isSentMessageSuccessfully = true;
		newCell8.currentType = AlertType.eventRejected_Sent;
		newCell8.alertCellAcceptedDate = new Date();
		newCell8.alertCellDescription = "Meeting ATLAS";
		newCell8.alertCellSenderName = "Tan Nguyen";
		newCell8.alertCellRecieverName = "Ryan";
		newCell8.isHandled = false;
		this.addCell(newCell8);
		// fake the cells//eventRejected_Received
		AlertCellData newCell9 = new AlertCellData();
		newCell9.alertCellPreferredDatetime = this.alertsListDate;
		newCell9.alertCellEventTitle = "Nghia's birthday";
		newCell9.isSentMessageSuccessfully = true;
		newCell9.currentType = AlertType.eventRejected_Received;
		newCell9.alertCellAcceptedDate = new Date();
		newCell9.alertCellDescription = "Meeting ATLAS";
		newCell9.alertCellSenderName = "Tan Nguyen";
		newCell9.alertCellRecieverName = "Ryan";
		newCell9.isHandled = true;
		this.addCell(newCell9);
		// fake the cells//taskAssigned_Sent
		AlertCellData newCell10 = new AlertCellData();
		newCell10.alertCellPreferredDatetime = this.alertsListDate;
		newCell10.alertCellEventTitle = "Nghia's birthday";
		newCell10.isSentMessageSuccessfully = true;
		newCell10.currentType = AlertType.taskAssigned_Sent;
		newCell10.alertCellAcceptedDate = new Date();
		newCell10.alertCellDescription = "Meeting ATLAS";
		newCell10.alertCellSenderName = "Tan Nguyen";
		newCell10.alertCellSenderId = "1208ZXY";
		newCell10.alertCellRecieverName = "Ryan";
		newCell10.isHandled = true;
		newCell10.alertCellTaskPriority = ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_HIGH;
		newCell10.alertCellUuid = "newCell10.alertCellUuid";
		this.addCell(newCell10);
		// fake the cells//taskAssigned_Sent
		AlertCellData newCell11 = new AlertCellData();
		newCell11.alertCellPreferredDatetime = this.alertsListDate;
		newCell11.alertCellEventTitle = "Nghia's birthday";
		newCell11.isSentMessageSuccessfully = false;
		newCell11.currentType = AlertType.taskAssigned_Sent;
		newCell11.alertCellAcceptedDate = new Date();
		newCell11.alertCellDescription = "Meeting ATLAS";
		newCell11.alertCellSenderName = "Tan Nguyen";
		newCell11.alertCellSenderId = "1208ZXY";
		newCell11.alertCellRecieverName = "Ryan";
		newCell11.isHandled = true;
		newCell11.alertCellTaskPriority = ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_MEDIUM;
		newCell11.alertCellUuid = "newCell11.alertCellUuid";
		this.addCell(newCell11);
		// fake the cells//taskAssigned_Received
		AlertCellData newCell12 = new AlertCellData();
		newCell12.alertCellPreferredDatetime = this.alertsListDate;
		newCell12.alertCellEventTitle = "Nghia's birthday";
		newCell12.isSentMessageSuccessfully = false;
		newCell12.currentType = AlertType.taskAssigned_Received;
		newCell12.alertCellAcceptedDate = new Date();
		newCell12.alertCellDescription = "Meeting ATLAS";
		newCell12.alertCellSenderName = "Tan Nguyen";
		newCell12.alertCellRecieverName = "Ryan";
		newCell12.isHandled = true;
		newCell12.alertCellSenderId = "1208ZXY";
		newCell12.alertCellTaskPriority = ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_LOW;
		newCell12.alertCellUuid = "newCell12.alertCellUuid";
		this.addCell(newCell12);

		// fake the cells//taskAccepted_Sent
		AlertCellData newCell13 = new AlertCellData();
		newCell13.alertCellPreferredDatetime = this.alertsListDate;
		newCell13.alertCellEventTitle = "Nghia's birthday";
		newCell13.isSentMessageSuccessfully = true;
		newCell13.currentType = AlertType.taskAccepted_Sent;
		newCell13.alertCellAcceptedDate = new Date();
		newCell13.alertCellDescription = "Meeting ATLAS";
		newCell13.alertCellSenderName = "Tan Nguyen";
		newCell13.alertCellRecieverName = "Ryan";
		newCell13.isHandled = true;
		newCell13.alertCellTaskPriority = ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_HIGH;
		newCell13.alertCellSenderId = "1208ZXY";
		newCell13.alertCellUuid = "newCell13.alertCellUuid";
		this.addCell(newCell13);

		// fake the cells//taskAccepted_Sent
		AlertCellData newCell14 = new AlertCellData();
		newCell14.alertCellPreferredDatetime = this.alertsListDate;
		newCell14.alertCellEventTitle = "Nghia's birthday";
		newCell14.isSentMessageSuccessfully = false;
		newCell14.currentType = AlertType.taskAccepted_Sent;
		newCell14.alertCellAcceptedDate = new Date();
		newCell14.alertCellDescription = "Meeting ATLAS";
		newCell14.alertCellSenderName = "Tan Nguyen";
		newCell14.alertCellRecieverName = "Ryan";
		newCell14.isHandled = true;
		newCell14.alertCellTaskPriority = ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_LOW;
		newCell14.alertCellSenderId = "1208ZXY";
		newCell14.alertCellUuid = "newCell14.alertCellUuid";
		this.addCell(newCell14);

		// fake the cells//taskAccepted_Sent
		AlertCellData newCell15 = new AlertCellData();
		newCell15.alertCellPreferredDatetime = this.alertsListDate;
		newCell15.alertCellEventTitle = "Nghia's birthday";
		newCell15.isSentMessageSuccessfully = false;
		newCell15.currentType = AlertType.taskAccepted_Received;
		newCell15.alertCellAcceptedDate = new Date();
		newCell15.alertCellDescription = "Meeting ATLAS";
		newCell15.alertCellSenderName = "Tan Nguyen";
		newCell15.alertCellRecieverName = "Ryan";
		newCell15.isHandled = true;
		newCell15.alertCellTaskPriority = ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_MEDIUM;
		newCell15.alertCellSenderId = "1208";
		newCell15.alertCellUuid = "newCell15.alertCellUuid";
		this.addCell(newCell15);

		// fake the cells//taskReject_Sent
		AlertCellData newCell17 = new AlertCellData();
		newCell17.alertCellPreferredDatetime = this.alertsListDate;
		newCell17.alertCellEventTitle = "Nghia's birthday";
		newCell17.isSentMessageSuccessfully = false;
		newCell17.currentType = AlertType.taskReject_Sent;
		newCell17.alertCellAcceptedDate = new Date();
		newCell17.alertCellDescription = "Meeting ATLAS";
		newCell17.alertCellSenderName = "Tan Nguyen";
		newCell17.alertCellRecieverName = "Ryan";
		newCell17.isHandled = true;
		newCell17.alertCellTaskPriority = ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_HIGH;
		newCell17.alertCellSenderId = "1208";
		newCell17.alertCellUuid = "newCell17.alertCellUuid";
		this.addCell(newCell17);

		// fake the cells//taskReject_Sent
		AlertCellData newCell18 = new AlertCellData();
		newCell18.alertCellPreferredDatetime = this.alertsListDate;
		newCell18.alertCellEventTitle = "Nghia's birthday";
		newCell18.isSentMessageSuccessfully = true;
		newCell18.currentType = AlertType.taskReject_Sent;
		newCell18.alertCellAcceptedDate = new Date();
		newCell18.alertCellDescription = "Meeting ATLAS";
		newCell18.alertCellSenderName = "Tan Nguyen";
		newCell18.alertCellRecieverName = "Ryan";
		newCell18.isHandled = true;
		newCell18.alertCellTaskPriority = ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_LOW;
		newCell18.alertCellSenderId = "1208";
		newCell18.alertCellUuid = "newCell18.alertCellUuid";
		this.addCell(newCell18);

		// fake the cells//taskReject_Receive
		AlertCellData newCell19 = new AlertCellData();
		newCell19.alertCellPreferredDatetime = this.alertsListDate;
		newCell19.alertCellEventTitle = "Nghia's birthday";
		newCell19.isSentMessageSuccessfully = true;
		newCell19.currentType = AlertType.taskReject_Receive;
		newCell19.alertCellAcceptedDate = new Date();
		newCell19.alertCellDescription = "Meeting ATLAS";
		newCell19.alertCellSenderName = "Tan Nguyen";
		newCell19.alertCellRecieverName = "Ryan";
		newCell19.isHandled = true;
		newCell19.alertCellTaskPriority = ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_MEDIUM;
		newCell19.alertCellSenderId = "1208ABC";
		newCell19.alertCellUuid = "newCell19.alertCellUuid";
		this.addCell(newCell19);

		// fake the cells//taskComplete_Received
		AlertCellData newCell20 = new AlertCellData();
		newCell20.alertCellPreferredDatetime = this.alertsListDate;
		newCell20.alertCellEventTitle = "Nghia's birthday";
		newCell20.isSentMessageSuccessfully = true;
		newCell20.currentType = AlertType.taskComplete_Received;
		newCell20.alertCellAcceptedDate = new Date();
		newCell20.alertCellDescription = "Meeting ATLAS";
		newCell20.alertCellSenderName = "Tan Nguyen";
		newCell20.alertCellRecieverName = "Ryan";
		newCell20.isHandled = true;
		newCell20.alertCellTaskPriority = ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_HIGH;
		newCell20.alertCellSenderId = "1208ABC";
		newCell20.alertCellUuid = "newCell20.alertCellUuid";
		this.addCell(newCell20);
		// fake the cells//taskComplete_Sent
		AlertCellData newCell21 = new AlertCellData();
		newCell21.alertCellPreferredDatetime = this.alertsListDate;
		newCell21.alertCellEventTitle = "Nghia's birthday";
		newCell21.isSentMessageSuccessfully = true;
		newCell21.currentType = AlertType.taskComplete_Sent;
		newCell21.alertCellAcceptedDate = new Date();
		newCell21.alertCellDescription = "Meeting ATLAS";
		newCell21.alertCellSenderName = "Tan Nguyen";
		newCell21.alertCellRecieverName = "Ryan";
		newCell21.isHandled = true;
		newCell21.isTaskCompleted = true;
		newCell21.alertCellTaskPriority = ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_HIGH;
		newCell21.alertCellSenderId = "1208ABC";
		newCell21.alertCellUuid = "newCell21.alertCellUuid";
		this.addCell(newCell21);
		// fake the cells//taskComplete_Sent
		AlertCellData newCell22 = new AlertCellData();
		newCell22.alertCellPreferredDatetime = this.alertsListDate;
		newCell22.alertCellEventTitle = "Nghia's birthday";
		newCell22.isSentMessageSuccessfully = false;
		newCell22.currentType = AlertType.taskComplete_Sent;
		newCell22.alertCellAcceptedDate = new Date();
		newCell22.alertCellDescription = "Meeting ATLAS";
		newCell22.alertCellSenderName = "Tan Nguyen";
		newCell22.alertCellRecieverName = "Ryan";
		newCell22.isHandled = true;
		newCell22.alertCellTaskPriority = ATLTaskPriorityScrollListAdapter.TASK_PRIORITY_INDEX_HIGH;
		newCell22.isTaskCompleted = true;
		newCell22.alertCellSenderId = "1208ABC";
		newCell22.alertCellUuid = "newCell22.alertCellUuid";
		this.addCell(newCell22);

		// fake the cells//notesShare_Sent
		AlertCellData newCell23 = new AlertCellData();
		newCell23.alertCellPreferredDatetime = this.alertsListDate;
		newCell23.alertCellEventTitle = "Nghia's birthday";
		newCell23.isSentMessageSuccessfully = true;
		newCell23.currentType = AlertType.notesShare_Sent;
		newCell23.alertCellAcceptedDate = new Date();
		newCell23.alertCellDescription = "Meeting ATLAS";
		newCell23.alertCellSenderName = "Tan Nguyen";
		newCell23.alertCellRecieverName = "Ryan";
		newCell23.isHandled = true;
		this.addCell(newCell23);
		// fake the cells//notesShare_Sent
		AlertCellData newCell24 = new AlertCellData();
		newCell24.alertCellPreferredDatetime = this.alertsListDate;
		newCell24.alertCellEventTitle = "Nghia's birthday";
		newCell24.isSentMessageSuccessfully = false;
		newCell24.currentType = AlertType.notesShare_Sent;
		newCell24.alertCellAcceptedDate = new Date();
		newCell24.alertCellDescription = "Meeting ATLAS";
		newCell24.alertCellSenderName = "Tan Nguyen";
		newCell24.alertCellRecieverName = "Ryan";
		newCell24.isHandled = false;
		this.addCell(newCell24);

		// fake the cells//notesShare_Received
		AlertCellData newCell25 = new AlertCellData();
		newCell25.alertCellPreferredDatetime = this.alertsListDate;
		newCell25.alertCellEventTitle = "Nghia's birthday";
		newCell25.isSentMessageSuccessfully = false;
		newCell25.currentType = AlertType.notesShare_Received;
		newCell25.alertCellAcceptedDate = new Date();
		newCell25.alertCellDescription = "Meeting ATLAS";
		newCell25.alertCellSenderName = "Tan Nguyen";
		newCell25.alertCellRecieverName = "Ryan";
		newCell25.isHandled = false;
		this.addCell(newCell25);
	}

	public boolean save() {
		return true;
	}

	public void clear() {
		this.alertListArray.clear();
	}

	public void addCell(AlertCellData noteCellData) {
		if (noteCellData.isHandled) {
			this.pastAlertListArray.add(noteCellData);
		} else {
			this.newAlertListArray.add(noteCellData);
		}
		this.alertListArray.add(noteCellData);
	}

	public void removeCell(AlertCellData noteCellData) {
		if (noteCellData.isHandled) {
			this.pastAlertListArray.remove(noteCellData);
		} else {
			this.newAlertListArray.remove(noteCellData);
		}
		this.alertListArray.remove(alertListCount);
	}

	public void currentDateDidChanged(Date currentDate) {
		// TODO Auto-generated method stub
		this.alertsListDate = currentDate;
		this.load();
	}

	public void addAlertCellDataToDatabase(AlertCellData cellData) {
		ATLAlertModel alertModel = new ATLAlertModel(cellData);

		ATLAlertDatabaseAdapter dbHelper = new ATLAlertDatabaseAdapter();
		boolean isExit = dbHelper.isExistInDatabase(alertModel);
		if (isExit) {
			dbHelper.updateATLAlertModel(alertModel);
		} else {
			dbHelper.insertATLAlertModel(alertModel);
		}

	}

	public class ATLAlertCellDataComparator implements
			Comparator<AlertCellData> {
		// NGHIA sort for newest to top.
		@Override
		public int compare(AlertCellData o1, AlertCellData o2) {
			return DateTimeUtilities.toString(o2.alertCellModifiedDate)
					.compareTo(
							DateTimeUtilities
									.toString(o1.alertCellModifiedDate));
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
	public void respondToEventInviteCallBack(boolean success) {
		// TODO Auto-generated method stub

	}

	@Override
	public void bookEventCallBack(boolean success) {
		// TODO Auto-generated method stub
		Log.v("bookEventCallBack", "bookEventCallBack");
		if (success) {
			Log.v("bookEventCallBack", "Successfully");
		}
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

}