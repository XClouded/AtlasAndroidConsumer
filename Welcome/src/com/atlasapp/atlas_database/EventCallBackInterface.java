package com.atlasapp.atlas_database;

import java.util.ArrayList;
import java.util.HashMap;

import com.atlasapp.model.ATLContactModel;

public interface EventCallBackInterface {


	void getAllEventsAssociatedCallBack(HashMap<String,ArrayList<EventProperties>> event,
			boolean success, ArrayList<String> eventsPrimaryWebEventId);
	/**
	 * Creates the event on parse and update the evnets properties 
	 * with the objectId's given to them from prase and the primary_web_event_id
	 * @param success
	 * @param eventPropertiesUpdated
	 */
	void setCalendarEventCallBack(boolean success, ArrayList<EventProperties> eventPropertiesUpdated);

	
	public void setAllEventMembersCallBack(HashMap<String,ArrayList<String>> allEventMembers, boolean success);
	void eventUpdatedCallBack(boolean success);

}
