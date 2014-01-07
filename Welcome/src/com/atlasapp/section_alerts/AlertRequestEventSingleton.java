//  ==================================================================================================================
//  AlertRequestEventSingleton.java
//  ALAS
//  Copyright (c) 2012 AtlasConsumerAndroid Apps. All rights reserved.
//  ==================================================================================================================
//
//  ==================================================================================================================
//  HISTORY
//  YYYY-MM-DD NAME:    Description of changes
//  ==================================================================================================================
//  2012-11-29 TAN:     Init class
//  ==================================================================================================================

package com.atlasapp.section_alerts;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.util.Log;
import android.widget.Toast;

import com.atlasapp.atlas_database.EventController;
import com.atlasapp.atlas_database.EventControllerCallBackInterface;
import com.atlasapp.atlas_database.EventProperties;
import com.atlasapp.atlas_database.ItemUserProperties;
import com.atlasapp.common.ATLEventCalendarModel;

public class AlertRequestEventSingleton implements
		EventControllerCallBackInterface {
	public static boolean isNewAlert = false;

	private static AlertRequestEventSingleton instance;
	private AlertCellData alertCellData = new AlertCellData();
	private ATLEventCalendarModel eventCellData = new ATLEventCalendarModel();

	public EventProperties eventPropertiesPre = new EventProperties();
	public EventProperties eventPropertiesAlt1 = new EventProperties();
	public EventProperties eventPropertiesAlt2 = new EventProperties();
	private EventController eventController;
	private ATLAlert alert = new ATLAlert();

	public static AlertRequestEventSingleton getInstance() {
		if (instance == null) {
			isNewAlert = true;
			instance = new AlertRequestEventSingleton();
		}
		return instance;
	}

	public void destroy() {
		if (instance != null) {
			instance = null;
			alertCellData = null;
			alert = null;
			isNewAlert = true;
		}
	}

	public void setAlert(ATLAlert data) {
		alert = data;
	}

	public ATLAlert getAlert() {
		return alert;
	}

	public void setData(AlertCellData alertCellData) {
		isNewAlert = false;
		AlertCellData temp = new AlertCellData();
		temp.copy(alertCellData);
		this.alertCellData = temp;
	}

	public AlertCellData getAlertCellData() {
		return alertCellData;
	}

	public void setEventCellData(ATLEventCalendarModel cellData) {
		isNewAlert = false;
		ATLEventCalendarModel temp = new ATLEventCalendarModel();
		// temp.copy(alertCellData);
		this.eventCellData = temp;
	}

	public ATLEventCalendarModel getEventCellData() {
		return eventCellData;

	}

	public EventProperties getEventPropertiesPre() {
		return eventPropertiesPre;
	}

	public void setEventPropertiesPre(EventProperties eventPropertiesPre) {
		this.eventPropertiesPre = eventPropertiesPre;
	}

	public EventProperties getEventPropertiesAlt1() {
		return eventPropertiesAlt1;
	}

	public void setEventPropertiesAlt1(EventProperties eventPropertiesAlt1) {
		this.eventPropertiesAlt1 = eventPropertiesAlt1;
	}

	public EventProperties getEventPropertiesAlt2() {
		return eventPropertiesAlt2;
	}

	public void setEventPropertiesAlt2(EventProperties eventPropertiesAlt2) {
		this.eventPropertiesAlt2 = eventPropertiesAlt2;
	}

	public void loadEventProperties(ATLEventCalendarModel data) {
		ArrayList<EventProperties> eventList = ATLEventCalendarModel
				.getEventByPrimaryWebEventId(data.primaryWebEventId);
		for (EventProperties event : eventList) {
			Log.v("event.startDateTime", "");
			switch (event.displayOrder) {
			case 0:
				eventPropertiesPre = event;
				// eventPropertiesPre.startDateTime = new Date();
				break;
			case 1:
				eventPropertiesAlt1 = event;
				// eventPropertiesAlt1.startDateTime = new Date();
				break;
			case 2:
				eventPropertiesAlt2 = event;
				// eventPropertiesAlt2.startDateTime = new Date();
				break;
			default:
				break;
			}
		}
		// Log.v("eventList size", " eventList size : ");
		// getEventByPrimaryWebEventId(data.primaryWebEventId);
	}

	public void loadEventPropertiesByKey(String primaryKey) {
		ArrayList<EventProperties> eventList = ATLEventCalendarModel
				.getEventByPrimaryWebEventId(primaryKey);
		for (EventProperties event : eventList) {
			Log.v("event.startDateTime", "");
			switch (event.displayOrder) {
			case 0:
				eventPropertiesPre = event;
				// eventPropertiesPre.startDateTime = new Date();
				break;
			case 1:
				eventPropertiesAlt1 = event;
				// eventPropertiesAlt1.startDateTime = new Date();
				break;
			case 2:
				eventPropertiesAlt2 = event;
				// eventPropertiesAlt2.startDateTime = new Date();
				break;
			default:
				break;
			}
		}
		// Log.v("eventList size", " eventList size : ");
		// getEventByPrimaryWebEventId(data.primaryWebEventId);
	}

	public void getEventByPrimaryWebEventId(String webItemId) {
		eventController = new EventController();
		// / initialize ATLMockDB to listen to call back methods
		// / from event controller....
		eventController.eventControllerCallBackListener = this;

		eventController.getEventByPrimaryWebEventId(webItemId);
	}

	@Override
	public void getEventByPrimaryWebEventIdCallBack(boolean success,
			ArrayList<EventProperties> events,
			ArrayList<ItemUserProperties> invitees) {

		if (success && events != null && events.size() > 0 && invitees != null
				&& invitees.size() > 0) {
			for (EventProperties event : events) {
				Log.v("event.startDateTime", "");
				switch (event.displayOrder) {
				case 0:
					eventPropertiesPre = event;
					// eventPropertiesPre.startDateTime = new Date();
					break;
				case 1:
					eventPropertiesAlt1 = event;
					// eventPropertiesAlt1.startDateTime = new Date();
					break;
				case 2:
					eventPropertiesAlt2 = event;
					// eventPropertiesAlt2.startDateTime = new Date();
					break;
				default:
					break;
				}
			}
			// Toast.makeText(ctx.getApplicationContext(),
			// "Event Found!", Toast.LENGTH_LONG).show();
			// Toast.makeText(ctx.getApplicationContext(),
			// "Event Title: "+event.get(0).title, Toast.LENGTH_LONG).show();
			// for (EventProperties properties:event)
			// {
			// Toast.makeText(ctx.getApplicationContext(),
			// "Event ObjectId:"+properties.objectId,
			// Toast.LENGTH_SHORT).show();
			//
			// }
		} else {
			// Toast.makeText(ctx.getApplicationContext(),
			// "No Event found by that primary key:", Toast.LENGTH_LONG).show();

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

	}

	@Override
	public void getAllUserEventsCallBack(
			HashMap<String, ArrayList<EventProperties>> allEventsRetrievedFromItemUser,
			ArrayList<ItemUserProperties> allUserItemUsersPropertiesRefreshedList,
			boolean success) {
		// TODO Auto-generated method stub

	}

}