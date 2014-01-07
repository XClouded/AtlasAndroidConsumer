package atlasapp.section_calendar;

import java.util.ArrayList;
import java.util.HashMap;

import atlasapp.database.EventProperties;
import atlasapp.database.ItemUserProperties;

public class CurrentSessionEvents {
	
	
	
	private boolean eventUpdated;
	public void setEventUpdated(boolean updated)
	{
		currentSessionEvents.eventUpdated = updated;
	}
	public boolean isEventUpdated()
	{
		return currentSessionEvents.eventUpdated;
	}
	
	
	private boolean itemUserUpdated;
	public void setItemUserUpdated(boolean updated)
	{
		currentSessionEvents.itemUserUpdated = updated;
	}
	public boolean isItemUserUpdated()
	{
		return currentSessionEvents.itemUserUpdated;
	}
	
	
	private HashMap<String, ArrayList<EventProperties>> allEventsRetrievedFromItemUser;
	private HashMap<String, ArrayList<ItemUserProperties>> allItemUserRetrieved;
	
	private ArrayList<ItemUserProperties> allUserItemUsersPropertiesRefreshedList;
	
	
	
	public HashMap<String, ArrayList<ItemUserProperties>> getAllEItemUserRetrievedFromItemUser()
	{
		return currentSessionEvents.allItemUserRetrieved;
	}
	public void setAllItemUserPropertiesRefreshedList( HashMap<String, ArrayList<ItemUserProperties>> itemEventRec)
	{
		 currentSessionEvents.allItemUserRetrieved = itemEventRec;
	}
	
	
	public ArrayList<ItemUserProperties> getAllUserItemUsersPropertiesRefreshedList()
	{
		return currentSessionEvents.allUserItemUsersPropertiesRefreshedList;
	}
	public void setAllUserItemUsersPropertiesRefreshedList( ArrayList<ItemUserProperties> itemUserRec)
	{
		 currentSessionEvents.allUserItemUsersPropertiesRefreshedList = itemUserRec;
	}
	
	public HashMap<String, ArrayList<EventProperties>> getAllEventsRetrievedFromItemUser()
	{
		return currentSessionEvents.allEventsRetrievedFromItemUser;
	}
	public void setAllEventsPropertiesRefreshedList( HashMap<String, ArrayList<EventProperties>> itemEventRec)
	{
		 currentSessionEvents.allEventsRetrievedFromItemUser = itemEventRec;
	}
	
	
	private static CurrentSessionEvents currentSessionEvents;
	
	private  HashMap<String,String> allCurrentWebEventsIds;
	public void  setCurrentEventsWebIDS(HashMap<String,String> allWebEventsIds)
	{
		currentSessionEvents.allCurrentWebEventsIds = allWebEventsIds;
		
	}
	public HashMap<String,String> getAllCurrentWebEventsIds()
	{
		return currentSessionEvents.allCurrentWebEventsIds;
	}
	
	
	

	private  HashMap<String,ArrayList<String>> allEventsIds;
	public void  setCurrentEventsIDS(HashMap<String,ArrayList<String>> allEventsIds)
	{
		currentSessionEvents.allEventsIds = allEventsIds;
		
	}
	public HashMap<String,ArrayList<String>> getAllCurrentEventsIds()
	{
		return currentSessionEvents.allEventsIds;
	}
	
	private ArrayList<String> webEventsIdsList;
	public ArrayList<String> getAllCurrentWebEventsIdsList()
	{
		return currentSessionEvents.webEventsIdsList;
	}
	public void setAllCurrentWebEventsIdsList(ArrayList<String> eventsIds)
	{
		 currentSessionEvents.webEventsIdsList = eventsIds;
	}
	
	private CurrentSessionEvents(){}
	
	
	public static CurrentSessionEvents getCurrentSessionEventsSingleton()
	{
		if (currentSessionEvents==null)
			currentSessionEvents = new CurrentSessionEvents();
		return currentSessionEvents;
	}

}
