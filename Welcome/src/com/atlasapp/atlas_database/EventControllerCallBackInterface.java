package com.atlasapp.atlas_database;

import java.util.ArrayList;
import java.util.HashMap;

import com.atlasapp.model.ATLContactModel;


public interface EventControllerCallBackInterface {

	public void createCalendarEventCallBack(boolean success);
	/**
	 * Gets all users events from database in a hash map, where
	 * key - primary_web_event_id
	 * value - array list containing all the events with the same
	 * 			primary_web_event_id (alt events times), in EventProperties type.
	 * and the eventsMembers :
	 * hash map :
	 * key - primary web event id
	 * value - array list of all the events' members atlas_id's 
	 * @param userEvents
	 * @param success - true if the event was successfully retrieved from the database
	 */
	public void getAllUserAnEventCallBack(HashMap<String,ArrayList<EventProperties>> userEvents, 
			HashMap<String,ArrayList<String>> eventsMembers,boolean success);
	public void getAllUserEventsCallBack(
			HashMap<String, ArrayList<EventProperties>> allEventsRetrievedFromItemUser,
			ArrayList<ItemUserProperties> allUserItemUsersPropertiesRefreshedList, boolean success);
	/**
	 * Call back from getEventByPrimaryWebEventId method for retrieving 
	 * all events properties and invitees ,by primary web item id (as set on Parse)
	 * @param success - success on retrieving all the Event properties
	 * @param event - all event properties 
	 * @param invitees
	 */
	public void getEventByPrimaryWebEventIdCallBack(boolean success,
			ArrayList<EventProperties> event, ArrayList<ItemUserProperties> invitees);
					
	public void respondToEventInviteCallBack(boolean success);
	public void bookEventCallBack(boolean success);
}
