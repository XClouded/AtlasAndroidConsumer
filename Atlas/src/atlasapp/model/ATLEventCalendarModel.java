package atlasapp.model;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import android.os.AsyncTask;
import android.text.format.DateFormat;
import atlasapp.common.ATLUser;
import atlasapp.common.DB;
import atlasapp.common.DBCallBackInterface;

import atlasapp.database.ATLEventModelCallBackInterface;
import atlasapp.database.DatabaseConstants.ACTION;
import atlasapp.database.DatabaseConstants.EVENT_STATUS;
import atlasapp.database.DatabaseConstants.ITEM_TYPE;
import atlasapp.database.EventProperties;
import atlasapp.database.ItemUserProperties;
import atlasapp.section_calendar.CurrentSessionEvents;

import com.google.api.client.util.DateTime;

/**
 * 
 * @author sharon nachum
 *
 */
public class ATLEventCalendarModel implements DBCallBackInterface{
	
	private EventProperties primaryEventProperties;
	private ArrayList<EventProperties> allAltEvents;

	public static ATLEventCalendarModelCallBackInterface eventCalendarModelListener;
	
	private HashMap<String,String> h;

	private static int currentEventRecordId;
	
	public ATLEventModelCallBackInterface atlEventModelListener;
	
	///////properties
	protected int tablePrimaryId = -1;
	public  String webEventId = "";
	public  String title="";
	protected  String location="";
	protected  String notes="";
	public  Date startDateTime = null;
	protected  int duration =0;
	// the order we display the alternative events 
	protected  int displayOrder = 0;
	//the inviter id
	protected  String atlasId = "";
	// objectId of primary web event id 
	public  String primaryWebEventId = "";
	protected  EVENT_STATUS status= EVENT_STATUS.PENDING;
	protected  ACTION action = ACTION.SAVE;
	protected  boolean isPrimary = false;
	protected String deviceId;
	protected int primaryEventId = -1;
	protected Date endDateTime;
	public Date modifiedDatetime;
	protected int localTableEventId = -1;
	protected int localTablePrimaryEventId=-1;

	private static int currentEventRecordPrimaryId;
	private boolean success;
	
	
	///////
	public ATLEventCalendarModel()
	{
		//for call back from DBCallBackInterface (writeCallBack  to db)
		DB.dbCallBackInterface = this;
	}
	
	
	
	
//	public void readLastInsertEventRecord() {
//		fromHashMap(Utilities.eHash(DB.q("SELECT * FROM event WHERE event_id='" + currentEventRecordId + "' LIMIT 1")));
//	}
	/**
	 * Gets an object of event properties and write it to the local 
	 * event table
	 * @param eventProperties
	 * @return the event_id 
	 */
	public synchronized static int write(EventProperties eventProperties) {
		
		currentEventRecordId = -1;
		eventProperties = EventProperties.deNull(eventProperties);
		if (eventProperties!=null)
		{
		eventProperties.localTablePrimaryEventId = currentEventRecordPrimaryId;
		String startTime = (eventProperties.startDateTime!=null)? 
				eventProperties.startDateTime.toString(): "";
		String endTime = (eventProperties.endDateTime!=null)? 
						eventProperties.endDateTime.toString(): "";
		
		String sql = "insert into event (" 
				+ "web_event_id," 
				+ "device_event_id,"
				+ "primary_event_id,"
				+ "device_event_duration," 
				+ "primary_web_event_id,"
				+ "atlas_id,"
				+ "display_order,"
				+ "status,"
				+ "device_event_start_datetime,"
				+ "device_event_end_datetime,"
				+ "device_event_title,"
				+ "device_event_location,"
				+ "device_event_notes,"
				+ "modified_datetime,"
				+ "action) values (" 
				+ "'" + DB.prep(eventProperties.webEventId) + "',"
				+ "'" + DB.prep(eventProperties.deviceId) + "'," 
				+ "'" + DB.prep(eventProperties.localTablePrimaryEventId) + "',"   
				+ "'" + DB.prep(eventProperties.duration) + "'," 
				+ "'" + DB.prep(eventProperties.primaryWebEventId) + "'," 
				+ "'" + DB.prep(eventProperties.atlasId) + "'," 
				+ "'" + DB.prep(eventProperties.displayOrder) + "'," 
				+ "'" + DB.prep(eventProperties.status.ordinal()) + "'," 
				+ "'" + startTime + "'," 
				+ "'" + endTime+ "'," 
				+ "'" + DB.prep(eventProperties.title) + "',"
				+ "'" + DB.prep(eventProperties.location) + "'," 
				+ "'" + DB.prep(eventProperties.notes) + "',"
				+ "'" + DB.prep(eventProperties.modifiedDatetime.toString()) + "',"
				+ "'" + DB.prep(eventProperties.action.getActionName()) + "');";
		DB.q(sql);
		// event_id
		int currentEventRecordId = Integer.parseInt(DB.lastInsertId());
		eventProperties.localTableEventId = currentEventRecordId;
		// store the current event primary id
		if (eventProperties.isPrimary)
			currentEventRecordPrimaryId = currentEventRecordId;
		}
		return currentEventRecordId;
			
	}
	/**
	 * Update the local event table by event_id, according to the hash map 
	 * key - column
	 * value - updated
	 * @param eventUpdatedColumns
	 * @param eventLocalId
	 * @return number of rows updated on the DB
	 */
	public synchronized int upadteEventByEventLocalId(HashMap<String,Object> eventUpdatedColumns, int eventLocalId)
	{
		if (eventUpdatedColumns!=null && eventLocalId!=-1)
		{
			HashMap<String,String> updateEventHash = new HashMap<String,String>();
			String id = DB.prep(eventLocalId);
			  Iterator iterator = eventUpdatedColumns.keySet().iterator();
			  String  keyColumn;
		      String valueColumn;
			  while(iterator.hasNext()) 
			  {
				  keyColumn=(String)iterator.next();
				  valueColumn = DB.prep(eventUpdatedColumns.get(keyColumn));
				  
				  updateEventHash.put(keyColumn, valueColumn);
			  }
			
			
			 
			   return DB.update("event", updateEventHash, "event_id", 
			    new String []{id}); 
		}else
			return 0;
	}
	public static ATLEventCalendarModel getEventTitleByWebEventId(String webEventId)
	{
		ATLEventCalendarModel event =null;
		
		if (webEventId!=null && !webEventId.equals(""))
		{
			 ArrayList<HashMap<String,String>> allPrimaryIdEvents = DB.q("SELECT * FROM event WHERE web_event_id='" + webEventId + "' ");
			 EventProperties eventAltProperties;
				//ArrayList<ATLEventModel> events = new ArrayList<ATLEventModel>();
				for (HashMap<String,String> record:allPrimaryIdEvents)
				{
					event = new ATLEventCalendarModel();
					eventAltProperties = event.fromHashMapToEventProperties(record);
//					if (eventAltProperties!=null)
//						eventProperties.add(eventAltProperties);
					//events.add(event);
				}
			 
		}
		return event;
	}
	
	
	
	/**
	 * update the local event table with the list of events object
	 * delete or write the new ones
	 * @param allEventsRetrievedFromItemUser
	 * @return
	 */
	public static void  updateAllEventRecordsLocally(
			HashMap<String, ArrayList<EventProperties>> allEventsRetrievedFromItemUser) {

		
		if (allEventsRetrievedFromItemUser!=null && allEventsRetrievedFromItemUser.size()>0)
		{
			ArrayList<String> eventsToDelete = new ArrayList<String>();

			ArrayList<String> allWebItemUserIds = getAllCurrentWebEventIdsOnLocal();
			allWebItemUserIds = (allWebItemUserIds!=null)? allWebItemUserIds: new ArrayList<String>();
			
//			ArrayList<String> webEventIds = getWebEventIdsListFromHash(allEventsRetrievedFromItemUser);
			
//			ArrayList<String> newRecordsToWrite = (ArrayList<String>)webEventIds.clone();
//			newRecordsToWrite.removeAll(allWebItemUserIds);
//			
//			ArrayList<String> recordsToUpdate = (ArrayList<String>)webEventIds.clone();
//			recordsToUpdate.retainAll(allWebItemUserIds);
//			
			Iterator iterator = allEventsRetrievedFromItemUser.keySet().iterator();
			  String  keyColumn;
			  ArrayList<EventProperties> valueColumn;
			  while(iterator.hasNext()) 
			  {
				  keyColumn=(String)iterator.next();
				  valueColumn = (ArrayList<EventProperties>) allEventsRetrievedFromItemUser.get(keyColumn);
				  for (EventProperties eventProperties:valueColumn)    
				  {
					  eventProperties = EventProperties.deNull(eventProperties);
					  if (allWebItemUserIds.contains(eventProperties.webEventId))
					  {
						  if (eventProperties.status.equals(EVENT_STATUS.NOT_THE_ONE))
						  {
							  eventsToDelete.add(eventProperties.webEventId);
						  }
						  else
						  {
							  
							  HashMap<String,String> updateEventHash = new HashMap<String,String>();
							  updateEventHash.put("display_order",  DB.prep(eventProperties.displayOrder));
							  updateEventHash.put("status",  DB.prep(eventProperties.status.ordinal()));
							  updateEventHash.put("device_event_start_datetime", eventProperties.startDateTime.toString());
							  updateEventHash.put("device_event_end_datetime",  eventProperties.endDateTime.toString());
							  updateEventHash.put("action",  DB.prep(eventProperties.action.getActionName()));
							  DB.update("event", updateEventHash, "web_event_id", 
									    new String []{eventProperties.webEventId}); 
//						  String sql = "update event SET" 
//								  + " display_order ='"+ DB.prep(eventProperties.displayOrder)+ "'," 
//								  + " status ='"+ DB.prep(eventProperties.status.ordinal()) + "'," 
//								  + "device_event_start_datetime ='"+ eventProperties.startDateTime.toString() + "'," 
//								  + "device_event_end_datetime ='" + eventProperties.endDateTime.toString() + "', "
//								  + "action ='" + DB.prep(eventProperties.action.getActionName()) + "' "
//								  + " WHERE web_event_id = '"+eventProperties.objectId+ "' ";  
//						  DB.q(sql);
						  }
					  }
					  else
					  {
						  // new item_user to write
						  write(eventProperties);
					  }
				  }
			  }
			  if (eventsToDelete.size()>0)
				  deleteEventsByEventWebId(eventsToDelete);
		}
	}
	private static ArrayList<String> getWebEventIdsListFromHash(
			HashMap<String, ArrayList<EventProperties>> allEventsRetrievedFromItemUser) {
		Iterator iterator = allEventsRetrievedFromItemUser.keySet().iterator();
		  
		ArrayList<String> webEventIdList = new ArrayList<String>();
		  String  keyColumn;
		  ArrayList<EventProperties> valueColumn;
		  while(iterator.hasNext()) 
		  {
			  keyColumn=(String)iterator.next();
			  valueColumn = (ArrayList<EventProperties>) allEventsRetrievedFromItemUser.get(keyColumn);
			  webEventIdList.add(keyColumn);
		  } 
		return webEventIdList;
	}




	private static ArrayList<String> getAllRecordsBySQL(String sql)
	{
		
		ArrayList<String> allEventRecords =null;
		if (sql!=null && !sql.equals(""))
		{
			allEventRecords = DB.queryOneParam(sql);
			
		
		}
		return allEventRecords;
	}
	private static ArrayList<String> getAllCurrentWebEventIdsOnLocal() {

		ArrayList<String> eventsIdsRecordsOnLocal = null;
			String sql = "SELECT web_event_id FROM event ";
			eventsIdsRecordsOnLocal = getAllRecordsBySQL(sql);
	return eventsIdsRecordsOnLocal;
	}




	/**
	 * Deletes an event from the local db by event_id (local event id\localTableEventId)
	 * @param eventId
	 * @return number of rows affected
	 */
	public synchronized  int deleteEventsByEventId(int eventId)
	{
		int rowsDeleted = 0;
		
		if (eventId!=-1)
		{
			String id = DB.prep(eventId);
			if (id!=null && !id.equals(""))
				rowsDeleted = DB.delete("event", "event_id", new String[]{id}); 
		}
		
		return rowsDeleted;
	}
	
	/**
	 * Deletes all the  events  from the local db by web_event_id (parse ObjectId's)
	 * @param webEventId - object id
	 * @return number of rows affected
	 */
	public static synchronized  int deleteEventsByEventWebId(ArrayList<String> webEventIds)
	{
		int rowsDeleted = 0;
		if (webEventIds!=null && webEventIds.size()>0)
		{
			String[] list = new String[webEventIds.size()];
			int index = 0;
			for (String item:webEventIds)
				if (item!=null)
				{
					list[index] = item;
					index++;
				}
			if (list!=null && list.length>0)
				rowsDeleted = DB.delete("event", "web_event_id", list); 

		}
		
		return rowsDeleted;
	}
	
	private Date toDateTime(String dateString)
	{
		SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
	//	DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
		Date dt = null;
		if (dateString!=null && !dateString.equals(""))
		try {
			dt = df.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dt;
	}
	private void writeEventToLocalDBEventTable(ArrayList<EventProperties> record) {

		EventProperties primaryEvent = null;
		ArrayList<EventProperties> allAltEvents = new ArrayList<EventProperties>();
		
		if (record!=null && record.size()>0)
		{
			for (EventProperties rec:record)
			{
				if (rec.isPrimary)
					primaryEvent = rec;
				else
					allAltEvents.add(rec);
			}
			writeEventToLocalDBEventTable(allAltEvents,primaryEvent);
		}
	}
	/**
	 * Gets an array list of an event and write it to the local event table
	 * 
	 * return a call back on savedEventOnLocalDB for all listeners
	 * 
	 * @param eventAlt all the events record alternative times (not including primary)
	 * @param primaryEvent - isPrimary set to true!
	 */
	public void writeEventToLocalDBEventTable(ArrayList<EventProperties> event, EventProperties primaryEvent)
	{
		if (primaryEvent!=null)
		{
			this.primaryEventProperties = primaryEvent;
			this.allAltEvents = event;
			primaryEventProperties.primaryEventId = -1;
			
			
			currentEventRecordPrimaryId = -1;
			int eventId = -1;
			eventId = write(primaryEvent);
			primaryEvent.localTableEventId = eventId;
			if (event!=null && event.size()>0 )
			for (EventProperties eventAlt:event)
			{
				if (!eventAlt.isPrimary)
				{
				eventId = write(eventAlt);
				eventAlt.localTableEventId = eventId;
				eventAlt.localTablePrimaryEventId = currentEventRecordPrimaryId;
				}
//				if (!success)
//					break;
			}
			atlEventModelListener.savedEventOnLocalDB(true, event, primaryEvent);
		}
		else
		{
			atlEventModelListener.savedEventOnLocalDB(false, null,null);
		}
	}
	@Override
	public void writeCallBack(boolean success)
	{
		this.success = success;
		
	}
	/**
	 * Return all the current events on local DB as a hash map 
	 * key - primary event id  (as use in the local event table)
	 * value -  hash map's where
	 * each hash map represent an event :
	 * key - primary_event_id (same as the hash key, for efficient processing...)
	 * value - array list of EventsProperties of all the event's time alternatives
	 * @return
	 */
	public HashMap<Integer, HashMap<Integer, ArrayList<EventProperties>>> getAllEvents()
	{
		HashMap<Integer, HashMap<Integer, ArrayList<EventProperties>>> allLocalEvents = null;
		
		
		HashMap<String,ATLEventCalendarModel>  allEventRecords = readAll();
		
		if (allEventRecords!=null && allEventRecords.size()>0)
		{
			allLocalEvents = new HashMap<Integer,HashMap<Integer,ArrayList<EventProperties>>>();
			HashMap<Integer,ArrayList<EventProperties>> eventHashMap ;
			ArrayList<EventProperties> eventsALT;
			HashMap<Integer,ArrayList<EventProperties>> eventsHash;
			Integer primaryEventId;
			
			
			  Iterator iterator = allEventRecords.keySet().iterator();
			  String  keyColumn;
		      ATLEventCalendarModel atlEventModel;
			  while(iterator.hasNext()) 
			  {
				  keyColumn=(String)iterator.next();
			
				  atlEventModel = (ATLEventCalendarModel)allEventRecords.get(keyColumn);
			//for (ATLEventCalendarModel atlEventModel:allEventRecords)
			//{
				if (atlEventModel!=null)
				{
					primaryEventId = (atlEventModel.localTablePrimaryEventId==-1)?
							atlEventModel.localTableEventId : atlEventModel.localTablePrimaryEventId;
					if (!allLocalEvents.containsKey(primaryEventId))
					{
						eventsALT = new ArrayList<EventProperties>();
						eventHashMap = new HashMap<Integer,ArrayList<EventProperties>> ();
					}else
					{
						eventHashMap = allLocalEvents.get(primaryEventId);
						eventsALT = eventHashMap.get(primaryEventId);
					}
					eventsALT.add(atlEventModel.toEventPropertiesFromLatestProperties());
					eventHashMap.put(primaryEventId, eventsALT);
					allLocalEvents.put(primaryEventId, eventHashMap);
				}
			}
		}
		
		return allLocalEvents;
	}

	
	/**
	 * Gets the primary_web_event_id (as updated on Parse)
	 * and return the Event from the local DB
	 * 
	 * @return
	 */
	public static ArrayList<EventProperties> getEventByPrimaryWebEventId(String primaryWebEventId)
	{
		 ArrayList<EventProperties> eventProperties = new  ArrayList<EventProperties>();
		 
		 if (primaryWebEventId!=null && !primaryWebEventId.equals(""))
		 {
			 ArrayList<HashMap<String,String>> allPrimaryIdEvents = DB.q("SELECT * FROM event WHERE primary_web_event_id='" + primaryWebEventId + "' or web_event_id='" + primaryWebEventId + "'");
			 EventProperties eventAltProperties;
			 ATLEventCalendarModel event;
				//ArrayList<ATLEventModel> events = new ArrayList<ATLEventModel>();
				for (HashMap<String,String> record:allPrimaryIdEvents)
				{
					event = new ATLEventCalendarModel();
					eventAltProperties = event.fromHashMapToEventProperties(record);
					if (eventAltProperties!=null)
						eventProperties.add(eventAltProperties);
					//events.add(event);
				}
			 
			 
		 }
		 
		 return eventProperties;
	}
	/**
	 * Gets the local primary event id and return Event 
	 * @return
	 */
	public ArrayList<EventProperties> getEventByPrimaryLocalEventId(String primaryLocalEventId)
	{
 ArrayList<EventProperties> eventProperties = new  ArrayList<EventProperties>();
		 
		 if (primaryWebEventId!=null && !primaryWebEventId.equals(""))
		 {
			 ArrayList<HashMap<String,String>> allPrimaryIdEvents = DB.q("SELECT * FROM event WHERE primary_event_id='" + primaryLocalEventId + "' or event_id='" + primaryLocalEventId + "'");
			 EventProperties eventAltProperties;
			 ATLEventCalendarModel event;
				//ArrayList<ATLEventModel> events = new ArrayList<ATLEventModel>();
				for (HashMap<String,String> record:allPrimaryIdEvents)
				{
					event = new ATLEventCalendarModel();
					eventAltProperties = event.fromHashMapToEventProperties(record);
					if (eventAltProperties!=null)
						eventProperties.add(eventAltProperties);
					//events.add(event);
				}
			 
			 
		 }
		 
		 return eventProperties;
	}
	/**
	 * Return a list of all the primary event id's 
	 * on local event db 
	 * @return
	 */
	public ArrayList<Integer> getALlPrimaryEventsIds()
	{
		ArrayList<Integer> primaryEventsIds = new ArrayList<Integer>();
		
		HashMap<String,ATLEventCalendarModel> allEventRecords = readAll();
		
		
		  Iterator iterator = allEventRecords.keySet().iterator();
		  String  keyColumn;
	      ATLEventCalendarModel record;
		 
		
		if (allEventRecords!=null && allEventRecords.size()>0)
		{
			
			 while(iterator.hasNext()) 
			  {
				  keyColumn=(String)iterator.next();
			
				  record = (ATLEventCalendarModel)allEventRecords.get(keyColumn);
			
		//	for (ATLEventCalendarModel record:allEventRecords)
		//	{
				Integer primaryId = record.primaryEventId;
				if (primaryId!=-1 && !primaryEventsIds.contains(primaryId))
					primaryEventsIds.add(primaryId);
				
			}
		}
		
		
		
		return primaryEventsIds;
	}
	/**
	 * Search the local DB 'event' table 
	 * @param primaryWebEventId - primary web event id (object id of the primary event on Parse)
	 * @return a list of all the alternative events associated with this primary event
	 */
	public static ArrayList<String> getAllEventWebEventIdByPrimaryWebEventId(String primaryWebEventId)
	{
		ArrayList<String> allEventIds = new ArrayList<String>();
		
		
		if (primaryWebEventId!=null && !primaryWebEventId.equals(""))
		{
			 ArrayList<HashMap<String,String>> allPrimaryWebEventIdEvents = DB.q("SELECT * FROM event WHERE primary_web_event_id='" + primaryWebEventId+"'");
			 String webEventId = "";
			 if (allPrimaryWebEventIdEvents!=null && allPrimaryWebEventIdEvents.size()>0)
			 {
				 for (HashMap<String,String> event:allPrimaryWebEventIdEvents)
				 {
					 webEventId = "";
					 if (event!=null)
					 {
						 webEventId = event.get("web_event_id");
						 if (webEventId!=null && !webEventId.equals(""))
							 allEventIds.add(webEventId); 
					 }
				 }
			 }
			 
		}
		
		return allEventIds;
	}
//	public String toString() {
//		toHashMap();
//		String s = "";
//		for (String key : h.keySet()) {
//			s += key + " = " + h.get(key) + "\n";
//		}
//		return s;
//	}
	/**
	 * writing to a hash map from event properties
	 * @param eventProperties
	 * @return
	 */
	public HashMap<String, String> toHashMap(EventProperties eventProperties) {
		if (eventProperties!=null)
		{
		h = new HashMap<String, String>();
		h.put("event_id", Integer.toString(eventProperties.localTableEventId));
		h.put("web_event_id", eventProperties.objectId);
		h.put("device_event_id", eventProperties.deviceId);
		h.put("primary_event_id", Integer.toString(eventProperties.localTablePrimaryEventId));
		h.put("primary_web_event_id", eventProperties.primaryWebEventId);
		h.put("atlas_id", eventProperties.atlasId);
		h.put("display_order", Integer.toString(eventProperties.displayOrder));
		h.put("status",  Integer.toString(eventProperties.status.ordinal()));
		h.put("device_event_start_datetime", eventProperties.endDateTime.toString());
		h.put("device_event_duration",  Integer.toString(eventProperties.duration));
		h.put("device_event_title", eventProperties.title);
		h.put("device_event_location", eventProperties.location);
		h.put("device_event_notes", eventProperties.notes);
		h.put("action", Integer.toString(eventProperties.action.ordinal()));
		h.put("modified_datetime", eventProperties.modifiedDatetime.toString());
		}
		return h;
	}
	
	private Date toDate(String startTime)
	{
		Date date = null;
		//String startTime = "2011-09-05 15:00:23";
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy"); 
		if (startTime!=null && !startTime.equals(""))
			try {
				date =  dateFormat.parse(startTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return date;
		 
	}
	
	/**
	 * Get an event record from the latest properties
	 * @return
	 */
	private EventProperties toEventPropertiesFromLatestProperties()
	{
		EventProperties eventProperties = new EventProperties();
		eventProperties.localTableEventId = localTableEventId;
		eventProperties.objectId =webEventId;
		
		eventProperties.deviceId = deviceId;
		eventProperties.localTablePrimaryEventId =localTablePrimaryEventId;
		eventProperties.primaryWebEventId = primaryWebEventId;
		eventProperties.atlasId = atlasId;
		eventProperties.displayOrder =displayOrder;
		
		eventProperties.status = status;
		
		eventProperties.startDateTime = startDateTime;
		eventProperties.endDateTime = endDateTime;
		eventProperties.title = title;
		eventProperties.location = location;
		eventProperties.notes = notes;
		
		eventProperties.action = action;
		eventProperties.modifiedDatetime = modifiedDatetime;

		eventProperties.webEventId =webEventId;
			
		
		return eventProperties;
		
	}
	private EventProperties fromHashMapToEventProperties(HashMap<String, String> event) {
		
		EventProperties eventProperties = new EventProperties();
		localTableEventId = Integer.parseInt((String) event.get("event_id"));
		webEventId = (String) event.get("web_event_id");
		deviceId = (String) event.get("device_event_id");
		localTablePrimaryEventId =Integer.parseInt((String) event.get("primary_event_id"));
		primaryWebEventId = (String) event.get("primary_web_event_id");
		atlasId = (String) event.get("atlas_id");
		displayOrder =Integer.parseInt((String) event.get("display_order"));
		EVENT_STATUS eventStatus = ((String) event.get("status"))!=null?
				Integer.parseInt((String) event.get("status"))==1? EVENT_STATUS.THE_ONE:
					Integer.parseInt((String) event.get("status"))==2? EVENT_STATUS.NOT_THE_ONE:
					EVENT_STATUS.PENDING: EVENT_STATUS.PENDING;
		status = eventStatus;
		duration = Integer.parseInt((String) event.get("device_event_duration"));
		startDateTime = toDate( (String) event.get("device_event_start_datetime"));
		endDateTime = toDate( (String) event.get("device_event_end_datetime"));
		title = (String) event.get("device_event_title");
		location = (String) event.get("device_event_location");
		notes = (String) event.get("device_event_notes");
		ACTION eventAction =( (String) event.get("action")!=null)?
				(((String) event.get("action")).equals("del"))? ACTION.DELETE:
					ACTION.SAVE:ACTION.SAVE;

		action = eventAction;
		modifiedDatetime = modifiedToDate( (String) event.get("modified_datetime"));

		eventProperties = toEventPropertiesFromLatestProperties();
		return eventProperties;

	}
	private Date modifiedToDate(String startTime) {
		Date date = null;
		//String startTime = "2011-09-05 15:00:23";
		//2013-05-08T23:21:20.449Z
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		if (startTime!=null && !startTime.equals(""))
			try {
				date =  dateFormat.parse(startTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return date;
	}




	/**
	 * Reads the local 'event' table .  
	 * 
	 * @return all the current records in a hash map where:
	 * key = > primary web event id
	 * value = > ATLEventCalendarModel object with all the properties set to that event
	 */
	public static HashMap<String,ATLEventCalendarModel> readAll()
	{
		ArrayList<HashMap<String,String>> eventsH = DB.q("SELECT * FROM event order by modified_datetime");
		ATLEventCalendarModel event;
		HashMap<String,ATLEventCalendarModel> events = new HashMap<String,ATLEventCalendarModel>();
		if (eventsH!=null && eventsH.size()>0)
		for (HashMap<String,String> record:eventsH)
		{
			event = new ATLEventCalendarModel();
			event.fromHashMapToEventProperties(record);
			events.put(event.primaryWebEventId, event);
		}
	
	return events;
	
	}

	/**
	 * Search the local event table for the webEventId and return the
	 * corresponding record as EventProperty
	 * @param webItemId
	 * @param currentItemUserProperties 
	 * @return
	 */
	public static EventProperties getEventByWebEventId(String webItemId)
	{

		EventProperties event = null;
		if (webItemId!=null && !webItemId.equals(""))
		{
			ArrayList<HashMap<String,String>> eventsH = DB.q("SELECT * FROM event WHERE web_event_id='" + webItemId+"'");
			ATLEventCalendarModel eventModel;
			HashMap<String,ATLEventCalendarModel> events = new HashMap<String,ATLEventCalendarModel>();
			if (eventsH!=null && eventsH.size()>0)
				for (HashMap<String,String> record:eventsH)
				{
					eventModel = new ATLEventCalendarModel();
					event = eventModel.fromHashMapToEventProperties(record);
				}
		}
		return event;
	}


	/**
	 * Gets an HashMap containing all the current user events 
	 * that are on Parse, and write to the local table the ones 
	 * that arent already
	 * @param allEventsRetrievedFromItemUser
	 */
	public void writeAllEventsToLocalDBEventTable(
			HashMap<String, ArrayList<EventProperties>> allEventsRetrievedFromItemUser) {

		
		if (allEventsRetrievedFromItemUser!=null && allEventsRetrievedFromItemUser.size()>0)
		{
			HashMap<String,ATLEventCalendarModel> allLocalEvents = readAll();
			if (allLocalEvents!=null && allLocalEvents.size()>0)
			{
				  Iterator iterator = allEventsRetrievedFromItemUser.keySet().iterator();
				  String  webEventId;
				  ArrayList<EventProperties> record;
				 
				  while(iterator.hasNext()) 
					{
					  	webEventId=(String)iterator.next();
					  	if (webEventId!=null && !allLocalEvents.containsKey(webEventId))
					  	{
					  		/// new event to write on local DB
							record = (ArrayList<EventProperties>)allEventsRetrievedFromItemUser.get(webEventId);
							writeEventToLocalDBEventTable(record);
					  	}
					}
					
			}
		}
		
		
		
	}
	
	/**
	 * Delete all the events rows from the 'event'
	 * local table
	 */
	public static void deleteAllEvents()
	{
		DB.deleteAllEventsFromEventTable();
	}
	
	/**
	 * search the event table 
	 * return hash map of all the primary_web_event_id's (value)
	 * by web_event_id (key)
	 * @return
	 */
	public static HashMap<String,String> getPrimaryIdsByWebEventIds()
	{
		HashMap<String,String> primaryIdsByWebEventIdsHash = new HashMap<String,String>();
		 ArrayList<HashMap<String,String>> allPrimaryWebEventIdEvents = DB.q("SELECT web_event_id,primary_web_event_id FROM event ");
		 String webEventId = "";
		 String primaryWevEventId="";
		 if (allPrimaryWebEventIdEvents!=null && allPrimaryWebEventIdEvents.size()>0)
		 {
			 for (HashMap<String,String> event:allPrimaryWebEventIdEvents)
			 {
				 webEventId = "";
				 primaryWevEventId = "";
				 if (event!=null)
				 {
					 webEventId = event.get("web_event_id");
					 primaryWevEventId = event.get("primary_web_event_id");
					 primaryWevEventId = (primaryWevEventId==null || primaryWevEventId.equals(""))? webEventId:primaryWevEventId;
					 if (webEventId!=null && !webEventId.equals(""))
						 primaryIdsByWebEventIdsHash.put(webEventId,primaryWevEventId); 
				 }
			 }
		 }
		
		
		return primaryIdsByWebEventIdsHash;
		
	}
	public static ArrayList<String> getAllEventWebEventsIds() {
		//		HashMap<String,String> primaryIdsByWebEventIdsHash = new HashMap<String,String>();
		ArrayList<HashMap<String,String>> allWebEventIdEvents = DB.q("SELECT web_event_id FROM event ");
		String webEventId = "";
		//		 String primaryWevEventId="";
		ArrayList<String> webEventsIds = new ArrayList<String>();
		if (allWebEventIdEvents!=null && allWebEventIdEvents.size()>0)
		{
			for (HashMap<String,String> event:allWebEventIdEvents)
			{
				webEventId = "";
				//				 primaryWevEventId = "";
				if (event!=null)
				{
					webEventId = event.get("web_event_id");
					//					 primaryWevEventId = event.get("primary_web_event_id");
					//					 primaryWevEventId = (primaryWevEventId==null || primaryWevEventId.equals(""))? webEventId:primaryWevEventId;
					if (webEventId!=null && !webEventId.equals(""))
						webEventsIds.add(webEventId); 
				}
			}
		}


		return webEventsIds;
	}
	private static ArrayList<EventProperties> eventPropertiesToWrite;
	public static void WriteEventProperties(ArrayList<EventProperties> eventProperties1)
	{
		if (eventProperties1!=null && eventProperties1.size()>0)
		{
			eventPropertiesToWrite = eventProperties1;
			//			WriteInBackground writeInBackground = new WriteInBackground();
			//			writeInBackground.execute();
			//		}
			//	}
			//	private static ArrayList<EventProperties> eventPropertiesToWrite;
			//	public static class  WriteInBackground extends AsyncTask<String, Integer, String> {
			//		@Override
			//		protected String doInBackground(String... webItemIdArray) 
			//		{

			////////////////////////////////////
			if (eventPropertiesToWrite!=null && eventPropertiesToWrite.size()>0)
			{
				ArrayList<String> eventsToDelete = new ArrayList<String>();

				ArrayList<String> allWebItemUserIds = getAllCurrentWebEventIdsOnLocal();
				allWebItemUserIds = (allWebItemUserIds!=null)? allWebItemUserIds: new ArrayList<String>();

				for (EventProperties eventProperties:eventPropertiesToWrite)
				{
					if (eventProperties.webEventId!=null && !eventProperties.webEventId.equals(""))
					{
						eventProperties = EventProperties.deNull(eventProperties);
						if (allWebItemUserIds.contains(eventProperties.webEventId))
						{
							allWebItemUserIds.add(eventProperties.webEventId);
							/// update event...
							HashMap<String,String> updateEventHash = new HashMap<String,String>();
							updateEventHash.put("display_order",  DB.prep(eventProperties.displayOrder));
							updateEventHash.put("status",  DB.prep(eventProperties.status.ordinal()));
							updateEventHash.put("device_event_start_datetime", eventProperties.startDateTime.toString());
							updateEventHash.put("device_event_end_datetime",  eventProperties.endDateTime.toString());
							Date modifiedDatetime = (eventProperties.modifiedDatetime!=null)?eventProperties.modifiedDatetime:new Date();
							updateEventHash.put("modified_datetime",  eventProperties.modifiedDatetime.toString());



							updateEventHash.put("action",  DB.prep(eventProperties.action.getActionName()));
							DB.update("event", updateEventHash, "web_event_id", 
									new String []{eventProperties.webEventId}); 
						}else
						{
							//////////////////////////////////////

							//					eventProperties = EventProperties.deNull(eventProperties);
							if (eventProperties!=null)
							{
								String startTime = (eventProperties.startDateTime!=null)? 
										eventProperties.startDateTime.toString(): "";
										String endTime = (eventProperties.endDateTime!=null)? 
												eventProperties.endDateTime.toString(): "";
												String primaryWebEventId = (eventProperties.primaryWebEventId!=null && !eventProperties.primaryWebEventId.equals(""))?
														eventProperties.primaryWebEventId:eventProperties.webEventId;
												String sql = "insert into event (" 
														+ "web_event_id," 
														+ "device_event_id,"
														+ "primary_event_id,"
														+ "device_event_duration," 
														+ "primary_web_event_id,"
														+ "atlas_id,"
														+ "display_order,"
														+ "status,"
														+ "device_event_start_datetime,"
														+ "device_event_end_datetime,"
														+ "device_event_title,"
														+ "device_event_location,"
														+ "device_event_notes,"
														+ "modified_datetime,"
														+ "action) values (" 
														+ "'" + DB.prep(eventProperties.webEventId) + "',"
														+ "'" + DB.prep(eventProperties.deviceId) + "'," 
														+ "'" + DB.prep(eventProperties.localTablePrimaryEventId) + "',"   
														+ "'" + DB.prep(eventProperties.duration) + "'," 
														+ "'" + DB.prep(primaryWebEventId) + "'," 
														+ "'" + DB.prep(eventProperties.atlasId) + "'," 
														+ "'" + DB.prep(eventProperties.displayOrder) + "'," 
														+ "'" + DB.prep(eventProperties.status.ordinal()) + "'," 
														+ "'" + startTime + "'," 
														+ "'" + endTime+ "'," 
														+ "'" + DB.prep(eventProperties.title) + "',"
														+ "'" + DB.prep(eventProperties.location) + "'," 
														+ "'" + DB.prep(eventProperties.notes) + "',"
														+ "'" + DB.prep(eventProperties.modifiedDatetime.toString()) + "',"
														+ "'" + DB.prep(eventProperties.action.getActionName()) + "');";
												DB.q(sql);

							}

						}
					}
				}

				//				if (eventCalendarModelListener!=null)
				//					eventCalendarModelListener.eventCalendarUpdatedCallBack(true);
				////				CurrentSessionEvents currentSessionEvents = CurrentSessionEvents.getCurrentSessionEventsSingleton();
				//				currentSessionEvents.setEventUpdated(true);
			}
			//			else
			//				if (eventCalendarModelListener!=null)
			//					eventCalendarModelListener.eventCalendarUpdatedCallBack(true);
			//		

			//			return null;
		}
	}
	
	
	
	public static void getYourMoveInBackground()
	{
//		GetYourMoveInBackground getYourMoveInBackground = new GetYourMoveInBackground();
//		getYourMoveInBackground.execute();
//	}
//	private static class GetYourMoveInBackground extends AsyncTask<String, Integer, String> {
//		@Override
//		protected String doInBackground(String... args) 
//		{
			ArrayList<HashMap<String,String>> allYourMoveEvents = DB.q("SELECT * FROM event WHERE  (atlas_id !='"+ATLUser.getParseUserID()+"' AND status=0)");
			HashMap<String, ArrayList<EventProperties>> yourMoveHash =  fromHashToEventPropertiesByPrimaryYourMove(allYourMoveEvents);

			if (eventCalendarModelListener!=null)
				eventCalendarModelListener.getYourMoveInBackground(yourMoveHash,webEventIdsFoundYourMove,webEventIdByPrimryYourMove);
//			return null;
		}
		private static ArrayList<String> webEventIdsFoundYourMove;
		private static HashMap<String,String> webEventIdByPrimryYourMove;
		private  static HashMap<String, ArrayList<EventProperties>> fromHashToEventPropertiesByPrimaryYourMove( ArrayList<HashMap<String,String>> allEventRecords)
		{
			 HashMap<String,ArrayList<EventProperties>> eventProperties = new  HashMap<String,ArrayList<EventProperties>>();
			 EventProperties eventAltProperties;
			 String primaryEventId = "";
			 
			 ATLEventCalendarModel event;
			 ArrayList<EventProperties> eventList;
			 webEventIdsFoundYourMove = new ArrayList<String>();
			 webEventIdByPrimryYourMove = new HashMap<String,String>();
				//ArrayList<ATLEventModel> events = new ArrayList<ATLEventModel>();
				for (HashMap<String,String> record:allEventRecords)
				{
					event = new ATLEventCalendarModel();
					eventAltProperties = event.fromHashMapToEventProperties(record);
					if (eventAltProperties!=null)
					{
						primaryEventId = (eventAltProperties.primaryWebEventId!=null && !eventAltProperties.primaryWebEventId.equals(""))?
								eventAltProperties.primaryWebEventId: eventAltProperties.webEventId;
						if (!eventProperties.containsKey(primaryEventId))
							eventList = new ArrayList<EventProperties>();
						else
							eventList = eventProperties.get(primaryEventId);
						webEventIdsFoundYourMove.add(eventAltProperties.webEventId);
						webEventIdByPrimryYourMove.put(eventAltProperties.webEventId,primaryEventId);
						eventList.add(eventAltProperties);
						eventProperties.put(primaryEventId, eventList);//(eventAltProperties);
					}
					//events.add(event);
				}
			 
			 
		 
		 return eventProperties;
		}
	/**
	 * Scan all the local DB for the your move events and return them 
	 * in a hash map where key = primary web event id, and value = 
	 * array list of EventProperties
	 * @return
	 */
	public static HashMap<String, ArrayList<EventProperties>> getEventYourMove()
	{

		ArrayList<HashMap<String,String>> allYourMoveEvents = DB.q("SELECT * FROM event WHERE (atlas_id !='"+ATLUser.getParseUserID()+"' AND status=0)");
		return fromHashToEventPropertiesByPrimary(allYourMoveEvents);

	}  
	
	public static void getBookedInBackground()
	{
//		GetBookedInBackground getBookedInBackground = new GetBookedInBackground();
//		getBookedInBackground.execute();
//	}
//	private static class GetBookedInBackground extends AsyncTask<String, Integer, String> {
//		@Override
//		protected String doInBackground(String... args) 
//		{
			ArrayList<HashMap<String,String>> allComplitedEvents = DB.q("SELECT * FROM event WHERE status!=0 ");
			HashMap<String, ArrayList<EventProperties>> yourCompletedHash =  fromHashToEventPropertiesByPrimaryBooked(allComplitedEvents);

			if (eventCalendarModelListener!=null)
				eventCalendarModelListener.getBookedInBackground(yourCompletedHash,webEventIdsFoundBooked,webEventIdByPrimryBooked);
//			return null;
		}
		private static ArrayList<String> webEventIdsFoundBooked;
		private static  HashMap<String,String> webEventIdByPrimryBooked;
		private  static HashMap<String, ArrayList<EventProperties>> fromHashToEventPropertiesByPrimaryBooked( ArrayList<HashMap<String,String>> allEventRecords)
		{
			 HashMap<String,ArrayList<EventProperties>> eventProperties = new  HashMap<String,ArrayList<EventProperties>>();
			 EventProperties eventAltProperties;
			 String primaryEventId = "";
			 webEventIdsFoundBooked = new ArrayList<String>();
			 webEventIdByPrimryBooked = new HashMap<String,String>();
			 ATLEventCalendarModel event;
			 ArrayList<EventProperties> eventList;
				//ArrayList<ATLEventModel> events = new ArrayList<ATLEventModel>();
				for (HashMap<String,String> record:allEventRecords)
				{
					event = new ATLEventCalendarModel();
					
					eventAltProperties = event.fromHashMapToEventProperties(record);
					if (eventAltProperties!=null)
					{
						primaryEventId = (eventAltProperties.primaryWebEventId!=null && !eventAltProperties.primaryWebEventId.equals(""))?
								eventAltProperties.primaryWebEventId: eventAltProperties.webEventId;
						if (!eventProperties.containsKey(primaryEventId))
							eventList = new ArrayList<EventProperties>();
						else
							eventList = eventProperties.get(primaryEventId);
						webEventIdsFoundBooked.add(eventAltProperties.webEventId);
						eventList.add(eventAltProperties);
						webEventIdByPrimryBooked.put(eventAltProperties.webEventId,primaryEventId);
						eventProperties.put(primaryEventId, eventList);//(eventAltProperties);
					}
					//events.add(event);
				}
			 
			 
		 
		 return eventProperties;
		}
//	}
	/**
	 * Scan all the local DB for the completed events and return them 
	 * in a hash map where key = primary web event id, and value = 
	 * array list of EventProperties
	 * @return
	 */
	public static HashMap<String, ArrayList<EventProperties>> getEventComplited()
	{

		ArrayList<HashMap<String,String>> allComplitedEvents = DB.q("SELECT * FROM event WHERE status=3");
		return fromHashToEventPropertiesByPrimary(allComplitedEvents);

	}
	/**
	 * Scan all the local DB for the pending events and return them 
	 * in a hash map where key = primary web event id, and value = 
	 * array list of EventProperties
	 * @return
	 */
	public static HashMap<String, ArrayList<EventProperties>> getEventPending()
	{

		ArrayList<HashMap<String,String>> allPendingEvents = DB.q("SELECT * FROM event WHERE atlas_id='"+ATLUser.getParseUserID()+"' AND status=0");


		return fromHashToEventPropertiesByPrimary(allPendingEvents);

		//			
	}
	public static void getPendingInBackground()
	{
//		GetPendingInBackground getPendingInBackground = new GetPendingInBackground();
//		getPendingInBackground.execute();
//	}  
//	private static class GetPendingInBackground extends AsyncTask<String, Integer, String> 
//	{
//		@Override
//		protected String doInBackground(String... args) 
//		{
			ArrayList<HashMap<String,String>> allPendingEvents = DB.q("SELECT * FROM event WHERE atlas_id ='"+ATLUser.getParseUserID()+"' AND status=0");

			HashMap<String, ArrayList<EventProperties>> pendingHash =  fromHashToEventPropertiesByPrimarypending(allPendingEvents);

			if (eventCalendarModelListener!=null)
				eventCalendarModelListener.getPendingInBackground(pendingHash,webEventIdsFoundP,webEventIdByPrimryP);
//			return null;
		}
		private static ArrayList<String> webEventIdsFoundP;
		private static HashMap<String,String> webEventIdByPrimryP;
		private static  HashMap<String, ArrayList<EventProperties>> fromHashToEventPropertiesByPrimarypending( ArrayList<HashMap<String,String>> allEventRecords)
		{
			 HashMap<String,ArrayList<EventProperties>> eventProperties = new  HashMap<String,ArrayList<EventProperties>>();
			 EventProperties eventAltProperties;
			 String primaryEventId = "";
			 
			 ATLEventCalendarModel event;
			 ArrayList<EventProperties> eventList;
			 webEventIdsFoundP = new ArrayList<String>();
			 webEventIdByPrimryP = new HashMap<String,String>();
				//ArrayList<ATLEventModel> events = new ArrayList<ATLEventModel>();
				for (HashMap<String,String> record:allEventRecords)
				{
					event = new ATLEventCalendarModel();
					
					eventAltProperties = event.fromHashMapToEventProperties(record);
					if (eventAltProperties!=null)
					{
						primaryEventId = (eventAltProperties.primaryWebEventId!=null && !eventAltProperties.primaryWebEventId.equals(""))?
								eventAltProperties.primaryWebEventId: eventAltProperties.webEventId;
						if (!eventProperties.containsKey(primaryEventId))
							eventList = new ArrayList<EventProperties>();
						else
							eventList = eventProperties.get(primaryEventId);
						webEventIdsFoundP.add(eventAltProperties.webEventId);
						eventList.add(eventAltProperties);
						webEventIdByPrimryP.put(eventAltProperties.webEventId,primaryEventId);
						eventProperties.put(primaryEventId, eventList);//(eventAltProperties);
					}
					//events.add(event);
				}
			 
			 
		 
		 return eventProperties;
		}
//	}
	
	
	private static HashMap<String, ArrayList<EventProperties>> fromHashToEventPropertiesByPrimary( ArrayList<HashMap<String,String>> allEventRecords)
	{
		 HashMap<String,ArrayList<EventProperties>> eventProperties = new  HashMap<String,ArrayList<EventProperties>>();
		 EventProperties eventAltProperties;
		 String primaryEventId = "";
		 
		 ATLEventCalendarModel event;
		 ArrayList<EventProperties> eventList;
			//ArrayList<ATLEventModel> events = new ArrayList<ATLEventModel>();
			for (HashMap<String,String> record:allEventRecords)
			{
				event = new ATLEventCalendarModel();
				eventAltProperties = event.fromHashMapToEventProperties(record);
				if (eventAltProperties!=null)
				{
					primaryEventId = (eventAltProperties.primaryWebEventId!=null && !eventAltProperties.primaryWebEventId.equals(""))?
							eventAltProperties.primaryWebEventId: eventAltProperties.webEventId;
					if (!eventProperties.containsKey(primaryEventId))
						eventList = new ArrayList<EventProperties>();
					else
						eventList = eventProperties.get(primaryEventId);
					
					eventList.add(eventAltProperties);
					eventProperties.put(primaryEventId, eventList);//(eventAltProperties);
				}
				//events.add(event);
			}
		 
		 
	 
	 return eventProperties;
	}
	



//	private static class  GetItemUserEventInTheBackground extends AsyncTask<String, Integer, String> {
//		@Override
//		protected String doInBackground(String... webItemIdArray) 
//		{
//			EventProperties event = null;
//			String webItemId = webItemIdArray[0];
//			ArrayList<HashMap<String,String>> eventsH = DB.q("SELECT * FROM event WHERE web_event_id='" + webItemId+"'");
//			ATLEventCalendarModel eventModel;
//			HashMap<String,ATLEventCalendarModel> events = new HashMap<String,ATLEventCalendarModel>();
//			if (eventsH!=null && eventsH.size()>0)
//			for (HashMap<String,String> record:eventsH)
//			{
//				eventModel = new ATLEventCalendarModel();
//				event = eventModel.fromHashMapToEventProperties(record);
//			}
//			
//			currentItemUserProperties.setEventAssociated(event);
//			
//			return null;
//		}
//	}
	
}




	
