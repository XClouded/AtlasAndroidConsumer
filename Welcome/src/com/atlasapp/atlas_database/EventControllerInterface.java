package com.atlasapp.atlas_database;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.atlasapp.atlas_database.DatabaseConstants.ITEM_TYPE;
import com.atlasapp.atlas_database.DatabaseConstants.ITEM_TYPE_PRIORITY_ORDER;
import com.atlasapp.atlas_database.DatabaseConstants.ITEM_USER_TASK_STATUS;
import com.atlasapp.atlas_database.DatabaseConstants.SERVER_MESSAGE;
import com.atlasapp.common.ATLEventCalendarModel;
import com.atlasapp.model.ATLContactModel;

public interface EventControllerInterface {
	
	/**
	 * Gets the list of the event's members in array list type ATLContactModel &
	 * the EventProperties as a list of all date time alt ,type Event each.
	 * and sends an invites to all the members on Parse DB 
	 * @param event
	 * @param eventsMembers
	 * get update response from: void createCalendarEventCallBack(boolean success)
	 */
	public void createCalendarEvent(ArrayList<EventProperties> eventProperties, ArrayList<ATLContactModel> invitees );

	
	/**
	 * Get user event calendar invite from the database in a hash map
	 * where :
	 * key - primary_web_event_id
	 * value -an event  =  array list of EventProperties 
	 * and each  events' members in a hash map
	 * key - events' primary web event id
	 * value - list of all events members atlas-id's  
	 * @param userEvents
	 * get the hash map from the call back method : 
	 * 	void getAllUserEventsCallBack(HashMap<String,ArrayList<EventProperties>> userEvents, 
				ArrayList<ATLContactModel> eventsMembers,boolean success)
	 */
	public void refreshUserCalendarEvents();
	
	/**
	 * Reads the local DB for all the current events invites 
	 * records for the user
	 * @return hash map where key  - primary web event id
	 * 							value - array list containing the event associated  
	 */
	public HashMap<String,ATLEventCalendarModel> getAllUserCalendarInvites();


	/**
	 * Gets the event primary web event id (as listed on Parse DB)
	 * and return the event's records from the local 'event'
	 * table and all the invitess associated in the
	 * getEventByPrimaryWebEventIdCallBack method
	 * @param webItemId
	 */
	public void getEventByPrimaryWebEventId(String primaryWebItemId);
	/**
	 * Gets the primary web event id (as set on Parse) and the 
	 * event alternative time choosen by the invitee , by the
	 * event web_item_id
	 * and update Parse and Local DB with the invitee response
	 * 
	 * 
	 * gets success update response from respondToEventInviteCallBack
	 * method for EventControllerCallBack listeners
	 * @param primaryWebItemId
	 * @param choosenEventWebEventId
	 */
	public void respondToEventInvite(String primaryWebItemId, String choosenEventWebEventId); 

	/**
	 * Booking the current event ,represented by the primary web item id
	 * with the choosenEventWebEventId web_item_id cho sen by the inviter
	 * @param primaryWebItemId
	 * @param choosenEventWebEventId
	 */
	public void bookEvent(String primaryWebItemId, String choosenEventWebEventId); 


}
