package atlasapp.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import atlasapp.common.ATLAlarmCellModel;
import atlasapp.common.ATLConstants.EmailTemplateType;
import atlasapp.common.ATLFriendLocalTable;
import atlasapp.common.ATLUser;
import atlasapp.common.DB;
import atlasapp.database.DatabaseConstants.EVENT_STATUS;
import atlasapp.database.DatabaseConstants.ITEM_TYPE;
import atlasapp.database.DatabaseConstants.ITEM_TYPE_PRIORITY_ORDER;
import atlasapp.database.DatabaseConstants.ITEM_USER_TASK_STATUS;
import atlasapp.database.DatabaseConstants.SIGN_NEW_USERS_CALLER;
import atlasapp.database.DatabaseConstants.TABLES_NAME;
import atlasapp.model.ATLContactModel;
import atlasapp.model.ATLEventCalendarModel;
import atlasapp.model.ATLEventCalendarModelCallBackInterface;
import atlasapp.model.ATLFriendModel;
import atlasapp.model.ATLItemUserModel;
import atlasapp.model.ATLItemUserModelCallBackInterface;
import atlasapp.section_alerts.ATLALertListController;
import atlasapp.section_calendar.ATLCalendarCellController;


/**
 * Creates an event calendar invite
 * @author sharon nachum
 *
 */
public class EventController implements ItemUserDelegateInterface,EventCallBackInterface,EventControllerInterface,UserDelagateInterface{
	
	
	public static EventControllerCallBackInterface eventControllerCallBackListener;
	private ArrayList<Event> event;
	/// All the events invitees invite properties
	private ArrayList<ItemUser> eventsMembers;
	private ArrayList<ATLContactModel> eventInvitess;
	
	private ArrayList<ItemUserProperties> itemUsersEventPropertiesUpdated;
	private ArrayList<EventProperties> eventPropertiesUpdated;
	
	
	private HashMap<String, ArrayList<EventProperties>> allUserEvents;
	private int itemUserCount;
	private ArrayList<String> eventsWebItemIds;
	private HashMap<String, ArrayList<EventProperties>> allEventsRetrievedFromItemUser;
	private ArrayList<ItemUserProperties> allUserItemUsersPropertiesList;
	private ArrayList<ItemUserProperties> allInviteesItemUsersPropertiesList;
	private Object getAllCalendarEventFromItemUsersFlas;
	private boolean getAllCalendarEventFromItemUsersFlag;
	private ArrayList<EventProperties> eventToBook;
	private String choosenEventWebEventId;
	private boolean upadteItemUserRecordsFlag;
	private ArrayList<String> eventsToDelete;
	private String calendarEventInviteMessage;
	private ArrayList<ItemUserProperties> respondInviteesList;
	private ArrayList<EventProperties> createEventProperties;
	private ArrayList<ATLContactModel> atlasFriends;
	private String respondMessageBack;
	/**
	 * @return return all the Events' properties as 
	 * a list of 'Event's properties after they updated on 
	 * the Parse DB 
	 */
	private ArrayList<EventProperties> getEventPropertiesUpdated()
	{
		return eventPropertiesUpdated;
	}
	/**
	 * @return all the Events members' invites properties (ItemUserProperties type each)
	 * 			that were updated on Parse DB from the current eventPropertiesUpdated
	 */
	private ArrayList<ItemUserProperties> getEventItemUsersInvitesProperties()
	{
		return itemUsersEventPropertiesUpdated;
	}
	public EventController()
	{
		
	}
	
	
	@Override
	public HashMap<String, ATLEventCalendarModel> getAllUserCalendarInvites() {
		
		return ATLEventCalendarModel.readAll();
	}
	@Override
	public void getEventByPrimaryWebEventId(String webItemId) {
		if (webItemId!=null && !webItemId.equals(""))
		{
			ArrayList<EventProperties> event = ATLEventCalendarModel.getEventByPrimaryWebEventId(webItemId);
			if (event!=null && event.size()>0)
			{
			
				//ArrayList<ItemUserProperties> invitees;
				ArrayList<String> webEventIds= new ArrayList<String> ();// ATLEventCalendarModel.getAllEventWebEventIdByPrimaryWebEventId(webItemId);
//				
				for (EventProperties eventAlt:event)
					webEventIds.add(eventAlt.webEventId);
				
				webEventIds.add(webItemId);
				
				///// TO DO : GET ALL ASSOCIATED ITEM_USER FROM LOCAL 
				//// DB
				if (webEventIds!=null && webEventIds.size()>0)
				{
					ArrayList<ItemUserProperties> invitees = ATLItemUserModel.getAllItemUserRecordsByWebItemIds(webEventIds);
					if (invitees!=null && invitees.size()>0)
					{
						eventControllerCallBackListener.getEventByPrimaryWebEventIdCallBack(true, event, invitees);
					}
					else
					{
						/// no invitees records
						eventControllerCallBackListener.getEventByPrimaryWebEventIdCallBack(false, null, null);

					}
				}else
					eventControllerCallBackListener.getEventByPrimaryWebEventIdCallBack(false, null, null);

			}else
				eventControllerCallBackListener.getEventByPrimaryWebEventIdCallBack(false, null, null);
		
		}
		else
		{
			eventControllerCallBackListener.getEventByPrimaryWebEventIdCallBack(false, null, null);
		}
		
	}
	
	
	@Override
	public void getUserMoveFromParse( ) 
	{
		ItemUser itemUser = new ItemUser();
		itemUser.itemUserListener = this;
	//	ArrayList<String> alreadyFetchedItemUserRecords = ATLItemUserModel.getUserMoveWebItemIds(ITEM_TYPE.EVENT);
		
		// call back from getUserMoveFromParseCallBack
		itemUser.getAllNewUserItemUserRecordsFromParse(ITEM_TYPE.EVENT,null);
		
		
	}
	/**
	 * 	Gets all the new item_user records for the user 
	 *  retrieve the corresponding events records from Parse 
	 *  and write them all to the local DB 
	 */
	@Override
	public void getUserMoveFromParseCallBack(
			final HashMap<String, ItemUserProperties> userMoveRecords,
			ArrayList<String> webItemIds,
			final ArrayList<ItemUserProperties> itemUserRecords) 
	{
		final HashMap<String, ArrayList<ItemUserProperties>> userMoveRecordsByPrimaryEventId = new HashMap<String, ArrayList<ItemUserProperties>>();
		if (userMoveRecords!=null && userMoveRecords.size()>0)
		{
			
			Event event = new Event();
			event.parseQuery.whereContainedIn("web_event_id", webItemIds);
			event.parseQuery.findInBackground(new FindCallback() {
				public void done(List<ParseObject> eventList, ParseException e)
				{
					EventProperties eventProp = null;
					ArrayList<EventProperties> eventListProperties = new ArrayList<EventProperties>();
					String primaryWebEventId = "";
					String webEventId = "";
					if (e==null && eventList!=null && eventList.size()>0)
					{
						for (ParseObject eventRecord:eventList)
						{
							if (eventRecord!=null)
							{
								primaryWebEventId = eventRecord.getString("primary_web_event_id");
								webEventId = eventRecord.getString("web_event_id");
								eventProp = new EventProperties(eventRecord);
								ArrayList<ItemUserProperties> eventsItemUserRecords = null;
								ItemUserProperties itemUserProperties;
								primaryWebEventId = (primaryWebEventId!=null && primaryWebEventId.equals(""))?webEventId:primaryWebEventId;
								if (!userMoveRecordsByPrimaryEventId.containsKey(primaryWebEventId))
								{
									eventsItemUserRecords = userMoveRecordsByPrimaryEventId.get(primaryWebEventId);
								}
								if (eventsItemUserRecords==null)
									eventsItemUserRecords = new ArrayList<ItemUserProperties>();
								
								itemUserProperties = userMoveRecords.get(webEventId);
								itemUserProperties.setEventAssociated(eventProp);
								eventsItemUserRecords.add(itemUserProperties);
								userMoveRecordsByPrimaryEventId.put(primaryWebEventId, eventsItemUserRecords);
								
							}
							eventListProperties.add(eventProp);
							
						}
						/// writing event to local table 
						ATLEventCalendarModel.WriteEventProperties(eventListProperties);
						ATLItemUserModel.updateAllItemUserRecordsLocally(ITEM_TYPE.EVENT,itemUserRecords);
						if (eventControllerCallBackListener!=null)
						eventControllerCallBackListener.getUserMoveFromParseCallBack(true, userMoveRecordsByPrimaryEventId);

						
					}else
					{
						if (eventControllerCallBackListener!=null)
						eventControllerCallBackListener.getUserMoveFromParseCallBack(false, userMoveRecordsByPrimaryEventId);

					}
					

				}
			});


			if (eventControllerCallBackListener!=null)
			this.eventControllerCallBackListener.getUserMoveFromParseCallBack(false, userMoveRecordsByPrimaryEventId);
		}
		else
		{
			if (eventControllerCallBackListener!=null)
			this.eventControllerCallBackListener.getUserMoveFromParseCallBack(false, null);
		}
		
	}
	
	/**
	 * Refresh all the user alerts from Parse DB
	 * and gets the call back from 
	 * getAllUserEventsCallBack method
	 */
	@Override
	public void refreshUserCalendarEvents()
	{
		getUserItemUserRecordsFromParse();
	}
	/**
	 * Search for all the item_user for user's event invites
	 */
	private void getUserItemUserRecordsFromParse()
	{
		ItemUser itemUser = new ItemUser();
		itemUser.itemUserListener = this;
		// call back from gotItemUserCallBack
		itemUser.getAllUserItemUserRecordsFromParse(ITEM_TYPE.EVENT);
	}
	
	
	
	/**
	 *Call back with the item_user records of the user from the Parse
	 *database - with the
	 * was_received and datetime_received updated and Store the
	 * item_records on Parse and Locally 
	 * and another list of all the invitees - to store locally
	 * Each type ItemUserProperties
	 */
	@Override
	public void gotItemUserCallBack(
			ArrayList<ItemUserProperties> allUserItemUsersPropertiesList,
			ArrayList<ItemUserProperties> allInviteesItemUsersPropertiesList
			, boolean success)
	{
		if (success && allUserItemUsersPropertiesList!=null && allUserItemUsersPropertiesList.size()>0)
		{
			this.allInviteesItemUsersPropertiesList = allInviteesItemUsersPropertiesList;

			this.allUserItemUsersPropertiesList = allUserItemUsersPropertiesList;
			/**
			 * The list of all item_users currently on Parse DB 
			 * as should be on local ....
			 * any item_user found on local that arent on the list
			 * should be deleted from local 
			 */
			ArrayList<ItemUserProperties> allItemUserRec = (ArrayList<ItemUserProperties>) allUserItemUsersPropertiesList.clone();
			if (allInviteesItemUsersPropertiesList!=null && allInviteesItemUsersPropertiesList.size()>0)
				allItemUserRec.addAll(allInviteesItemUsersPropertiesList);
			/**
			 * will contain the list of all the events records 
			 * to delete (by  object id  from Parse) 
			 * 
			 */
			ArrayList<String> webEventIdsToDelete = ATLItemUserModel.getEventsWebIdsRecordsThatAreNotOnParse(allItemUserRec);
			
			if (webEventIdsToDelete!=null && webEventIdsToDelete.size()>0)
				ATLItemUserModel.deleteItemUserByWebItemId(webEventIdsToDelete);
			//// Save all the updated user's item_user records on Parse + Local
			////// Locally + Parse
			//// call back from updateItemUserCallBack
			getAllCalendarEventFromItemUsersFlag = true;
			ATLItemUserModel.updateAllItemUserRecordsLocally(ITEM_TYPE.EVENT,allItemUserRec);

			ItemUser itemUser = new ItemUser();
			itemUser.itemUserListener = this;
			itemUser.updateItemUserOnParse(this.allUserItemUsersPropertiesList);
		}
		else
		{
			/// no user invites to show...
			eventControllerCallBackListener.getAllUserEventsCallBack(null,null, false);
		}
	}
	
	
	
	@Override
	public void updateBookedItemUserCallBack(boolean success)
	{
		if (success)
		{
			if (bookEventFlag)		
			{
				bookEventFlag = false;
				if (updatedItemUserRecords!=null && updatedItemUserRecords.size()>0 && eventUpdatedRecords!=null && eventUpdatedRecords.size()>0)
				{
					String pushMessage = ATLUser.getUserFirstName();
					pushMessage += (eventUpdatedRecords.get(0).atlasId.equals(ATLUser.getParseUserID()))? " has booked the ":
						" has responded to the ";
					pushMessage += eventUpdatedRecords.get(0).title+" event";
					
					ArrayList<String> pushInviteesList = new ArrayList<String>();
					if (eventUpdatedRecords.get(0).atlasId.equals(ATLUser.getParseUserID()))
					{
						eventInvitess = new ArrayList<ATLContactModel>();
						ATLContactModel itemInvitee;
						ArrayList<String> atlasIds = new ArrayList<String> ();
						for (ItemUserProperties invitee:updatedItemUserRecords)
						{
							if (!atlasIds.contains(invitee.atlasId) && !invitee.atlasId.equals("") &&
									!invitee.atlasId.equals(ATLUser.getParseUserID()))
							{
								itemInvitee = ATLContactModel.getContactByAtlasId(invitee.atlasId);
								if (itemInvitee!=null && itemInvitee.getAtlasId()!=null && !itemInvitee.getAtlasId().equals(""))
									eventInvitess.add(itemInvitee);
							}
						}
						
						/// inviter sends push to all invitee's 
						//sendPushNotificationAlert(updatedItemUserRecords, pushMessage);
						sendInitations(eventUpdatedRecords,updatedItemUserRecords, pushMessage,EmailTemplateType.EmailTemplateBooked);
					}
					else
					{
						pushInviteesList.add(eventUpdatedRecords.get(0).atlasId);
						/// invitee sends push to inviter
						sendPushNotificationAlertByAtlasIds(pushInviteesList, pushMessage);

					}
				
					
					if (eventUpdatedRecords.get(0).atlasId.equals(ATLUser.getParseUserID()))
					{
						ATLCalendarCellController.updateCalendarAlarmCell(eventUpdatedRecords,  ctx);
						
					}else
						ATLCalendarCellController.saveCalendarCellWithItemUser(updatedItemUserRecords,eventUpdatedRecords,  ctx);
					eventUpdatedRecords = null;
					updatedItemUserRecords = null;
					eventControllerCallBackListener.bookEventCallBack(true);
					
					
				}
				
				
				
			}
		
			
		}else
		{
			if (bookEventFlag)		
			{
				eventControllerCallBackListener.bookEventCallBack(false);
			}
		}
		
	}
	
	/**
	 * call back when ItemUser records were updated on parse
	 */
	@Override
	public void updateItemUserCallBack(boolean success) {
		if (success)
		{
			
			if (getAllCalendarEventFromItemUsersFlag)
				getAllCalendarEventFromItemUsers(allUserItemUsersPropertiesList);
			else
				if (upadteItemUserRecordsFlag)
					updatedEventRecords();
			
		}else
		{
		
			if (upadteItemUserRecordsFlag)
				eventControllerCallBackListener.respondToEventInviteCallBack(false);
			else
				if (getAllCalendarEventFromItemUsersFlag)
					eventControllerCallBackListener.getAllUserEventsCallBack(null,null, false);
			
		}
	}
	/**
	 * Update the event records on Parse
	 * get call back from eventUpdatedCallBack
	 */
	private void updatedEventRecords() 
	{
		if (eventToBook!=null && eventToBook.size()>0)
		{
			
			/// SAVE UPDATED EVENT RECORDS ON PARSE
			Event event = new Event();
			// call back on eventUpdatedCallBack
			event.eventDelagator = this;
			event.updateEvent(eventToBook);
		}
		else
		{
			sendPushNotificationAlert(respondInviteesList, respondMessageBack);
			ATLCalendarCellController.updateCalendarAlarmCell(eventUpdatedRecords,  ctx);
			eventControllerCallBackListener.respondToEventInviteCallBack(true);
		}

		
	}
	private void sendPushNotificationAlert(String inviteeAtlasId,
			String message)
	{
//		Log.v("EVENT CONTROLLER ", "PUSH"+inviteeAtlasId+" "+message	);


		if (message!=null && !message.equals("") && inviteeAtlasId!=null && !inviteeAtlasId.equals("") &&!inviteeAtlasId.equals(ATLUser.getParseUserID()))
		{
			String inviteesChannel = "ID"+inviteeAtlasId;
			JSONObject data = new JSONObject();
			try {  
				data = new JSONObject("{\"alert\": \""+message+"\"," +
						"\"badge\": \"Increment\"," +
						"\"action\": \"atlasapp.section_alerts.PUSH_NOTIFICATION\"," +
						"\"sound\": \"Incoming_Atlas_Push.mp3\"}");
			} catch (JSONException e) {
//				Log.v("EVENT CONTROLLER ", "PUSH"+e.getMessage());
				// TODO Auto-generated catch block     
				e.printStackTrace();
			}

//			Log.v("EVENT CONTROLLER ", "PUSHed");

			if (data!=null && inviteesChannel!=null && !inviteesChannel.equals("") && message!=null && !message.equals(""))
				AtlasServerConnect.pushNotification(inviteesChannel,message,data);
		}

	}
	
	private void sendPushNotificationAlert(ArrayList<ItemUserProperties> inviteesEventInvites,
			String message)
	{
		   
		if (message!=null && !message.equals("") && inviteesEventInvites!=null && inviteesEventInvites.size()>0)
		{
		LinkedList<String> inviteesChannels = new LinkedList<String>();
		JSONObject data = new JSONObject();
		try {  
			data = new JSONObject("{\"alert\": \""+message+"\"," +
					"\"badge\": \"Increment\"," +
					"\"action\": \"atlasapp.section_alerts.PUSH_NOTIFICATION\"," +
					"\"sound\": \"Incoming_Atlas_Push.mp3\"}");
		} catch (JSONException e) {
			// TODO Auto-generated catch block     
			e.printStackTrace();
		}
		for (ItemUserProperties invitee:inviteesEventInvites)
		{
			if (invitee!=null && invitee.atlasId!=null && !invitee.atlasId.equals("")
				&& 	!invitee.atlasId.equals(ATLUser.getParseUserID()) &&
				!inviteesChannels.contains("ID"+invitee.atlasId))
				inviteesChannels.add("ID"+invitee.atlasId);
		}  
		
	
		if (data!=null && inviteesChannels.size()>0 && message!=null && !message.equals(""))
			AtlasServerConnect.pushNotification(inviteesChannels,message,data);
		}
		
	}
	private void sendPushNotificationAlertByAtlasIds(ArrayList<String> inviteesEventInvitesAtlasIds,
			String message)
	{
		
		if (message!=null && !message.equals("") && inviteesEventInvitesAtlasIds!=null && inviteesEventInvitesAtlasIds.size()>0){
		LinkedList<String> inviteesChannels = new LinkedList<String>();
		JSONObject data = new JSONObject();
		try {  
			data = new JSONObject("{\"alert\": \""+message+"\"," +
					"\"badge\": \"Increment\"," +
					"\"action\": \"atlasapp.section_alerts.PUSH_NOTIFICATION\"," +
					"\"sound\": \"Incoming_Atlas_Push.mp3\"}");
		} catch (JSONException e) {
			// TODO Auto-generated catch block     
			e.printStackTrace();
		}
		for (String invitee:inviteesEventInvitesAtlasIds)
		{
			if (invitee!=null && !invitee.equals("")
				&& 	!invitee.equals(ATLUser.getParseUserID()) &&
						!inviteesChannels.contains("ID"+invitee))
				inviteesChannels.add("ID"+invitee);
		}  
		
	
		if (data!=null && inviteesChannels.size()>0 && message!=null && !message.equals(""))
			AtlasServerConnect.pushNotification(inviteesChannels,message,data);
		}
		
	}
	@Override
	public void eventUpdatedCallBack(boolean success) { 
		if (success)
		{
			eventToBook = null;
		//	ATLEventCalendarModel.deleteEventsByEventWebId(eventsToDelete);
		//	eventControllerCallBackListener.bookEventCallBack(true);
			ATLCalendarCellController.updateCalendarAlarmCell(eventUpdatedRecords,  ctx);
			sendPushNotificationAlert(respondInviteesList, respondMessageBack);
			eventControllerCallBackListener.respondToEventInviteCallBack(true);
		}
		else
		{
			//eventControllerCallBackListener.bookEventCallBack(false);
			eventControllerCallBackListener.respondToEventInviteCallBack(false);

		}
	}
	/**
	 * Gets a list of all the ItemUserProperties with "event" as their   
	 * ITEM_TYPE,  retrieve all the corresponding events from Parse
	 * and store them in a HashMap where:
	 * key - the primay_web_event_id
	 * value - a list of all the alt events associated with that 
	 * 			primary event id, type EventProperties
	 * @param allUserItemEventsInvites
	 */
	private void getAllCalendarEventFromItemUsers(ArrayList<ItemUserProperties> allUserItemEventsInvites)
	{
		getAllCalendarEventFromItemUsersFlag = false;
		if (allUserItemEventsInvites != null && allUserItemEventsInvites.size()>0)
		{
			allUserEvents = new HashMap<String, ArrayList<EventProperties>>();
			ArrayList<String> eventsWebItemIds ;
			
			eventsWebItemIds = getAllEventsWebItemIdsFromItemUserList(allUserItemEventsInvites);
			if (eventsWebItemIds!=null && eventsWebItemIds.size()>0)
			{
				Event event = new Event();
				/// sign controller to listen to Event's call back
				event.eventDelagator = this;
				itemUserCount = 0;
				this.eventsWebItemIds = eventsWebItemIds;
				// call back from getAllEventsAssociatedCallBack
				event.retrieveEventsFromWebItemIds(eventsWebItemIds);
			}
			else
			{
				eventControllerCallBackListener.getAllUserEventsCallBack(null,allUserItemUsersPropertiesList, false);

			}
		} 
		else
		{
			/// no user invites to show...
			eventControllerCallBackListener.getAllUserEventsCallBack(null,allUserItemUsersPropertiesList, false);
		}
	}
	
	
	
	/**
	 * Retrieved all event web_item_id from the item_users 
	 * list to retreive from the event table
	 * @param allUserItemEventsInvites
	 * @return
	 */
	private ArrayList<String> getAllEventsWebItemIdsFromItemUserList(
			ArrayList<ItemUserProperties> allUserItemEventsInvites) {
		ArrayList<String> eventsWebItemIds = null;
		
		if (allUserItemEventsInvites!=null && allUserItemEventsInvites.size()>0)
		{
			eventsWebItemIds = new ArrayList<String>();
			for (ItemUserProperties itemUserProperties:allUserItemEventsInvites)
			{
				if (itemUserProperties!=null && itemUserProperties.webItemId!=null
						&&!itemUserProperties.webItemId.equals(""))
					
					eventsWebItemIds.add(itemUserProperties.webItemId);
			}
		}
		
		
		
		return eventsWebItemIds;
	}
	Context mContext;
	public void createSelfCalendarEvent(
			ArrayList<EventProperties> eventProperties, Context mContext2) {
		if (eventProperties!=null && eventProperties.size()>0 )
		{  
			
			ATLCalendarCellController.saveSelfCalendarCellWithEvent(eventProperties, mContext2);
			
			eventControllerCallBackListener.createCalendarEventCallBack(true);
			
		}
		else
			eventControllerCallBackListener.createCalendarEventCallBack(false);
		
	}
	/**
	 * Gets the list of the event's members in array list type ATLContactModel &
	 * the EventProperties as a list of all date time alt ,type Event each.
	 * and sends an invites to all the members on Parse DB 
	 * @param mContext 
	 * @param event
	 * @param eventsMembers
	 * @return FAIL or SUCCESS message from Parse
	 */
	@Override
	public void createCalendarEvent(ArrayList<EventProperties> eventProperties, ArrayList<ATLContactModel> invitees, Context mContext )
	{
		if (eventProperties!=null && eventProperties.size()>0 && invitees!=null && invitees.size()>0 )
		{
			this.mContext = mContext;
			createCalendarEventForAtlasFriends(eventProperties,invitees);
			//validateEventsInvitees(invitees);
			
			
			
			
		}
		else
			eventControllerCallBackListener.createCalendarEventCallBack(false);
	}	
	private void createCalendarEventForAtlasFriends(ArrayList<EventProperties> eventProperties, ArrayList<ATLContactModel> invitees )
	{
		eventsMembers = new ArrayList<ItemUser>();
		
		eventInvitess =addUserInviterToList(invitees);
		
		createEventProperties = eventProperties;
		
		validateEventsInvitees(eventInvitess);
		
		 
		
		
	}
	/**
	 * Adding the user to the invitees list		
	 * @param invitees
	 * @return
	 */
	private ArrayList<ATLContactModel> addUserInviterToList(
			ArrayList<ATLContactModel> invitees) {
		ArrayList<ATLContactModel> inviteesWithUser = invitees;
		
		
		if (inviteesWithUser!=null && inviteesWithUser.size()>0)
		{
			HashMap<String, String> h = new HashMap<String,String>();
			h.put("FRIEND_ID", "-1");
			h.put("ATLAS_ID", ATLUser.getParseUserID());//"CyAdwqTj1x"(Tan),"BiyWaF9mDY" Nghia,"WGRUBN8JJC"(Sharon)
			h.put("FIRSTNAME", ATLUser.getUserNameDisplay());
			h.put("LASTNAME", "");
			h.put("COMPANY", "");
			h.put("PHONE_NUMBER", "");
			h.put("EMAIL_ADDRESS", ATLUser.getEmail());
			h.put("PIC_PATH", ATLUser.getParseUserID());
			String fromFacebook = (ATLUser.getfbID()!=null && !ATLUser.getfbID().equals(""))?"1":"0";
			h.put("FROM_FACEBOOK", fromFacebook);
			String fbID = (ATLUser.getfbID()!=null)? ATLUser.getfbID():"";
			h.put("FACEBOOK_ID", fbID);
			h.put("FROM_TWITTER", "0");
			h.put("FROM_LINKEDIN", "0");
			h.put("LINKEDIN_ID", "");
			h.put("FROM_ADDRESS_BOOK", "0");
			h.put("ADDRESS_BOOK_ID", "");
			
			h.put("IS_ATLAS_USER", "1");
			
			
			
			ATLFriendModel friend = new ATLFriendModel();
			
			friend.fromHashMap(h);
			ATLContactModel invitee = new ATLContactModel(friend);
			inviteesWithUser.add(invitee);
		}
		
		
		
		return inviteesWithUser;
	}
	/**
	 * Call back method on setEventOnParseDB from Event class
	 * saving the event on the 'event' table
	 */
	@Override
	public void setCalendarEventCallBack(boolean success,ArrayList<EventProperties> eventPropertiesUpdated) {

		if (success)
		{
			createEventProperties = eventPropertiesUpdated;
			///// sends invites to invitees (itemUser updates)
			upadteItemUsers(eventPropertiesUpdated);
		}
		else
		{
			eventControllerCallBackListener.createCalendarEventCallBack(false);

		}
	}
	/**
	 * Once an event is updated on Parse (on a call back method success)
	 * update the item user - for "sending" invites to all members 
	 * of invite
	 * get call back from itemUsersSavedCallBack method
	 */
	private void upadteItemUsers(ArrayList<EventProperties> eventPropertiesUpdated)
	{
		if (atlasFriends!=null && atlasFriends.size()>0 
				&& eventPropertiesUpdated!=null && eventPropertiesUpdated.size()>0)
		{
			this.eventPropertiesUpdated = eventPropertiesUpdated;
			ItemUser itemUser = new ItemUser();
			// sign as a listener to call back response
			itemUser.itemUserListener = this;
			itemUser.setItemUsers(atlasFriends,eventPropertiesUpdated);
		}
		else
			eventControllerCallBackListener.createCalendarEventCallBack(false);

	}
	/**
	 * Checks if all invitees got atlas id
	 * and if not create a new non atlas user on parse
	 * retreive the new atlas id and update local FRIEND db
	 * 
	 * call back on getSignNonAtlasUsersCallBack method
	 */
	private void validateEventsInvitees(ArrayList<ATLContactModel> eventInvitess) {
		
		
		
		ArrayList<ATLContactModel> newNonAtlasUserToSign = new ArrayList<ATLContactModel>();
		atlasFriends = new ArrayList<ATLContactModel>();
		if (eventInvitess!=null && eventInvitess.size()>0 )
		{
			for (ATLContactModel invitee:eventInvitess)
			{
				if (invitee.getAtlasId()!=null && !invitee.getAtlasId().equals(""))
				{
					atlasFriends.add(invitee);

				}else
				{
					if (invitee.getEmailAddress()!=null && !invitee.getEmailAddress().equals(""))
					///// invite address book contact that doesnt have atlas id yet
						newNonAtlasUserToSign.add(invitee);
						
				}  
			}  
			
			Log.v("EVENT CONTROLLER", "atlasFriends "+atlasFriends.size());

			Log.v("EVENT CONTROLLER", "newNonAtlasUserToSign "+newNonAtlasUserToSign.size());
			
			Log.v("EVENT CONTROLLER", "atlasFriends "+atlasFriends.get(0).getEmailAddress());
			Log.v("EVENT CONTROLLER", "atlasFriends "+atlasFriends.get(0).getAtlasId());
			
//			Log.v("EVENT CONTROLLER", "atlasFriends "+atlasFriends.get(1).getEmailAddress());
//			Log.v("EVENT CONTROLLER", "atlasFriends "+atlasFriends.get(1).getAtlasId());

			AtlasServerConnect parseConnect =   AtlasServerConnect.getSingletonObject(null);
			//// call back from getSignNonAtlasUsersCallBack
			parseConnect.checkIfNonFrinedEmailOnParse(SIGN_NEW_USERS_CALLER.EVENT_INVITE,newNonAtlasUserToSign,this);
		}
		else
		{
			getSignNonAtlasUsersCallBack(SIGN_NEW_USERS_CALLER.EVENT_INVITE,false,null);
			
		}
		
		
	}
	@Override
	public void getSignNonAtlasUsersCallBack(SIGN_NEW_USERS_CALLER caller,boolean success,
			HashMap<String,ATLContactModel> nonAtlasUsersByEmail) {
		if (caller!=null && caller.equals(SIGN_NEW_USERS_CALLER.EVENT_INVITE))
		{
			if (success && createEventProperties!=null && createEventProperties.size()>0)
			{
				if ((atlasFriends!=null && atlasFriends.size()==0 && 
						nonAtlasUsersByEmail!=null && 
						nonAtlasUsersByEmail.size()==0))
				{
					
					//// no invitees
					eventControllerCallBackListener.createCalendarEventCallBack(false);
					
				}
				else
				{
					  Log.v("EVENT CONTROLLER", " nonAtlasUsersByEmail.size() ");

					//// update the local database with the object id's 
					if (nonAtlasUsersByEmail!=null && 
							nonAtlasUsersByEmail.size()!=0)
					{
						  Log.v("EVENT CONTROLLER", " nonAtlasUsersByEmail.size() "+nonAtlasUsersByEmail.size());
						  Log.v("EVENT CONTROLLER", " atlasFriends.size() "+atlasFriends.size());

					  Iterator iterator = nonAtlasUsersByEmail.keySet().iterator();
					  String  keyColumnEmail;
					  ATLContactModel valueObjectId;
				      HashMap<String,String> atlasIdColumn  = new HashMap<String,String>();
					  while(iterator.hasNext()) 
					  {
							
						  keyColumnEmail=(String)iterator.next();
						  valueObjectId=(ATLContactModel)nonAtlasUsersByEmail.get(keyColumnEmail);
						  Log.v("EVENT CONTROLLER", "UPDATE LOCAL DB"+valueObjectId);

						  if (keyColumnEmail!=null && !keyColumnEmail.equals("")&& valueObjectId!=null &&
								  valueObjectId.getAtlasId()!=null && !valueObjectId.getAtlasId().equals(""))
						  {
							  ATLContactModel.updateFriendAtlasIdByEmail(keyColumnEmail, valueObjectId);
//							  atlasIdColumn.put("ATLAS_ID",valueObjectId.getAtlasId());
//							  DB.update("ATL_FRIEND", atlasIdColumn, "EMAIL_ADDRESS", new String[]{keyColumnEmail});
//							  atlasIdColumn  = new HashMap<String,String>();
							  atlasFriends.add(valueObjectId);
						  }
					  }
					}
					eventInvitess = atlasFriends;
					  Log.v("EVENT CONTROLLER", " eventInvitess.size() "+eventInvitess.size());

					event = new ArrayList<Event>();
					String title = (createEventProperties.get(0)!=null)?createEventProperties.get(0).title:"";
					if (title!=null && !title.equals(""))
						calendarEventInviteMessage =  ATLUser.getUserNameDisplay()+" has invited you to "+title+"";
					else
						calendarEventInviteMessage =  ATLUser.getUserNameDisplay()+" has invited you to a new event ";

					Event event = new Event();
					/// sign controller to listen to Event's call back
					event.eventDelagator = this;
					// call back from setCalendarEventCallBack()
					event.setEventOnParseDB(createEventProperties);
				}
			}  
			else
			{
				/// invalid invitees list
				eventControllerCallBackListener.createCalendarEventCallBack(false);
			}
		}
		
	}
	private void sendInitations(ArrayList<EventProperties> event,
			ArrayList<ItemUserProperties> itemUser,
			String calendarEventInviteMessage,
			EmailTemplateType emailTemplateType)
	{
		if (eventInvitess!=null && eventInvitess.size()>0)
		{
			ArrayList<ATLContactModel> nonUsers = new ArrayList<ATLContactModel>();
			for (ATLContactModel invitee:eventInvitess)
			{
				if (invitee.isAtlasUser())
				{
			    	
					sendPushNotificationAlert(invitee.getAtlasId(), calendarEventInviteMessage);				
				}
				else
				{
					
					nonUsers.add(invitee);
					
				}
				
			}
			if (nonUsers!=null && nonUsers.size()>0)
			{
				AtlasServerConnect parseConnect =   AtlasServerConnect.getSingletonObject(null);
				
				parseConnect.sendNonUserEventInvite(nonUsers,event,itemUser,emailTemplateType);
			}
		}
	}
	
	/**
	 * Call back method from setItemUsers from ItemUsers
	 * after to attempt to create itemUsers on ParseDB
	 */
	@Override
	public void itemUsersSavedCallBack(boolean success,
			ArrayList<ItemUserProperties> inviteesEventInvites) {
		if (success && inviteesEventInvites!=null && inviteesEventInvites.size()>0 )
		{
			itemUsersEventPropertiesUpdated = inviteesEventInvites;
			ATLItemUserModel.updateAllItemUserRecordsLocally(ITEM_TYPE.EVENT, inviteesEventInvites);
			/// Success call back....
			ATLCalendarCellController.saveCalendarCellWithEvent(createEventProperties, mContext);
			///////  EVENT WAS SUCCESSFULLY SAVED AND UPDATED ON PARSE 
			/////// Send push notifications to all invitees....
			calendarEventInviteMessage =ATLUser.getUserFirstName()+"  "+ATLUser.getUserLastName()+" has invited you to "+createEventProperties.get(0).title;
			
			
			
//			sendPushNotificationAlert(inviteesEventInvites,calendarEventInviteMessage);
		
			sendInitations(createEventProperties,
					itemUsersEventPropertiesUpdated,
					calendarEventInviteMessage,
					EmailTemplateType.EmailTemplateInvitation);
			eventControllerCallBackListener.createCalendarEventCallBack(true);

			
		}
		else
		{
			//// something wrong happened on updating item user table 
			//// on Parse DB
			eventControllerCallBackListener.createCalendarEventCallBack(false);

		}
	}
	/**
	 * Call back when attempt to retrieve all the events 
	 * from the item user list 
	 */
	@Override
	public void getAllEventsAssociatedCallBack(
			HashMap<String,ArrayList<EventProperties>> allEventsRetrievedFromItemUser, boolean success
			, ArrayList<String> eventsPrimaryWebEventId) {
		if (success && allEventsRetrievedFromItemUser!=null && allEventsRetrievedFromItemUser.size()>0)
		{ 
			if (eventsPrimaryWebEventId!=null && eventsPrimaryWebEventId.size()>0)
			{//// 
				/////retrieve all events invitees from the primary web event id's 
				this.allEventsRetrievedFromItemUser = allEventsRetrievedFromItemUser;
				//retrieveAllEvnetsMembers(eventsPrimaryWebEventId);mhvkjgfkj
				updateAllCalendarEventInvites(allEventsRetrievedFromItemUser);
				
			}
			else
			{
				/// no events 
				eventControllerCallBackListener.getAllUserEventsCallBack(null,allUserItemUsersPropertiesList, false);
			}
		}
		else
		{
			/// no events 
			eventControllerCallBackListener.getAllUserEventsCallBack(null,null, false);
		}
		
	}
	/**
	 * Gets HashMap containing all the events 
	 * (where key - primary event id)
	 * and lists of updated (was_received,received_datetime for the user)
	 * item_users
	 * 
	 *  store all the updated events records on local database
	 * @param allEventsRetrievedFromItemUser
	 * @param allUserItemUsersPropertiesList
	 * @param allInviteesItemUsersPropertiesList
	 */
private void updateAllCalendarEventInvites(
			HashMap<String, ArrayList<EventProperties>> allEventsRetrievedFromItemUser) {
	
	if (allEventsRetrievedFromItemUser!=null && allEventsRetrievedFromItemUser.size()>0
			&& allUserItemUsersPropertiesList!=null && allUserItemUsersPropertiesList.size()>0
			&& allInviteesItemUsersPropertiesList!=null && allInviteesItemUsersPropertiesList.size()>0)
	{
		
		/**
		 * write all the events retrieved from Parse to Local
		 */  
		//ATLEventCalendarModel eventsModel = new ATLEventCalendarModel();
		ATLEventCalendarModel.updateAllEventRecordsLocally(allEventsRetrievedFromItemUser);
		eventControllerCallBackListener.getAllUserEventsCallBack(allEventsRetrievedFromItemUser,allInviteesItemUsersPropertiesList, true);
		
	
	}
	else
	{
		eventControllerCallBackListener.getAllUserEventsCallBack(null,allUserItemUsersPropertiesList, false);

	}
	
		
	}
	//	/**
//	 * Retrieving all the users' events members 
//	 * and get call back from setAllEventMembersCallBack method
//	 * @param eventsWebItemIds
//	 */
//	private void retrieveAllEvnetsMembers(ArrayList<String> eventsWebPrimaries) 
//	{
//		if (eventsWebPrimaries!=null && eventsWebPrimaries.size()>0)
//		{
//			Event event = new Event();
//			event.eventDelagator = this;
//
//			// call back from setAllItemUserMembersCallBack
//			event.setAllEventMembers(eventsWebPrimaries);
//			
//		}
//		else
//		{
//			eventControllerCallBackListener.getAllUserEventsCallBack(null, null, false);
//
//		}
//	}
	/**
	 * Call back from setAllEventMembers
	 * gets an Hash Map :
	 * key - primary web event id
	 * value - array list of all the events' members atlas_id
	 */
	@Override
	public void setAllEventMembersCallBack(
			HashMap<String,ArrayList<String>> allEventMembers, boolean success) 
	{
		if (success && allEventMembers!=null && allEventMembers.size()>0
				&& allEventsRetrievedFromItemUser!=null && allEventsRetrievedFromItemUser.size()>0)
		{
			eventControllerCallBackListener.getAllUserAnEventCallBack(allEventsRetrievedFromItemUser, allEventMembers, true);

		}
		else
		{
			eventControllerCallBackListener.getAllUserAnEventCallBack(null,null, false);
		}
		
	}
	
	@Override
	public void bookEvent(String primaryWebItemId,
			String choosenEventWebEventId) {
	if (primaryWebItemId!=null && !primaryWebItemId.equals("")
			&& choosenEventWebEventId!=null && !choosenEventWebEventId.equals(""))	
	{
		eventToBook = 
	 			ATLEventCalendarModel.getEventByPrimaryWebEventId(primaryWebItemId);
		this.choosenEventWebEventId = choosenEventWebEventId;
		if (eventToBook!=null && choosenEventWebEventId!=null && !choosenEventWebEventId.equals(""))
		{
			//// IF THE EVENT.ATLAS_ID (INVITER)
			/// IS THE USER => CAN BOOK THE EVENT
			if (eventToBook.get(0).atlasId.equals(ATLUser.getParseUserID()))
			{
				/// validate the chosen time as part of the event
				boolean choosenTimeEvent = false;
				ArrayList<String> webItemIdList = new ArrayList<String>();

				for (EventProperties eventAlt:eventToBook)
				{
					if (eventAlt.objectId!=null && !eventAlt.objectId.equals(""))
						webItemIdList.add(eventAlt.objectId);
					choosenTimeEvent = choosenTimeEvent || eventAlt.objectId.equals(choosenEventWebEventId);
				}if (choosenTimeEvent)
				{
					/**
					 * gets all the updated event recs  and itemUser recs 
					**/
					ItemUser itemUser = new ItemUser();
					itemUser.itemUserListener = this;
					/// call back from getItemUserRecordsByWebItemIdCallBack
					itemUser.getItemUserRecordsByWebItemId(webItemIdList);
				
				}
				else
				{
					/// chosen time isnt part of the event options 
					eventControllerCallBackListener.bookEventCallBack(false);

				}
			
			}
			else
			{
				/// ONLY INVITER CAN BOOK AN EVENT
				eventControllerCallBackListener.bookEventCallBack(false);

			}
		}
		else
		{
			/// no event was found on local DB
			eventControllerCallBackListener.bookEventCallBack(false);
		}
	 }else
			eventControllerCallBackListener.bookEventCallBack(false);

	}
	@Override
	public void getItemUserRecordsByWebItemIdCallBack(boolean success,
			ArrayList<ItemUserProperties> itemUserRecordsByWebItemId) {
		boolean userIsInviter = (eventToBook!=null && eventToBook.size()>0 &&
				eventToBook.get(0).atlasId.equals(ATLUser.getParseUserID()));

		if (success)
		{
			
			/**
			 *  choosenEventWebEventId - got the chosen alt event object Id
			 *  itemUserRecordsByWebItemId - 
			 */
			if (itemUserRecordsByWebItemId!=null && itemUserRecordsByWebItemId.size()>0)
			{
						
				
				respondInviteesList = itemUserRecordsByWebItemId;
					/**
					 * Set wasReceived/receivedDatetime to true when the 
					 * inviter is shown the alert that people responded
					 * Inviter selects a time Update the alt events:
					 * 	status = 1 for the chosen time
					 * 	status = 2 for the others
					 * Delete the other times from device Send to parse 
					 **/
				for(ItemUserProperties itemUser:itemUserRecordsByWebItemId)
				{
					if (itemUser!=null && itemUser.atlasId.equals(ATLUser.getParseUserID()))
					{  
						itemUser.wasReceived = true;
						itemUser.receivedDateTime = new Date();
//						if (!userIsInviter)
//						{//// respond to event
							itemUser.priorityOrder = (itemUser.webItemId!=null && choosenEventWebEventId!=null 
													&& !choosenEventWebEventId.equals("") &&itemUser.webItemId.equals(choosenEventWebEventId))?
									ITEM_TYPE_PRIORITY_ORDER.IDEAL:ITEM_TYPE_PRIORITY_ORDER.IDEAL;	
							itemUser.status = (choosenEventWebEventId!=null)? 
									((itemUser.objectId!=null && !choosenEventWebEventId.equals("") && itemUser.webItemId.equals(choosenEventWebEventId))?
									ITEM_USER_TASK_STATUS.ACCEPTED:
									ITEM_USER_TASK_STATUS.DECLINED):ITEM_USER_TASK_STATUS.DECLINED;
							itemUser.statusDateTime = new Date();
						//}
					}
				}
					
				/// the inviter is the user....
//				if (userIsInviter && eventToBook!=null && eventToBook.size()>0)
//				{
				String eventsTitle = "";
				String eventRespond = "";
					eventsToDelete = new ArrayList<String>();
					for (EventProperties eventAlt:eventToBook)
					{
						if (eventAlt!=null && eventAlt.webEventId.equals(choosenEventWebEventId))
						{
							eventAlt.status = EVENT_STATUS.THE_ONE;
							eventsTitle = eventAlt.title;
							eventRespond = eventAlt.startDateTime.toString();
						}
						else
						{
							eventsToDelete.add(eventAlt.webEventId);
							eventAlt.status = EVENT_STATUS.NOT_THE_ONE;
							eventsTitle = eventAlt.title;
						}	
					}    
			//	}
					
					if (eventsTitle!=null && !eventsTitle.equals("") && !eventRespond.equals(""))
					respondMessageBack = ATLUser.getUserNameDisplay()+ " has accepted your invitation to "+eventsTitle+ " on "+eventRespond;
					else
						if (eventsTitle!=null && !eventsTitle.equals("") && eventRespond.equals(""))
							respondMessageBack = ATLUser.getUserNameDisplay()+ "  declined your invitation to "+eventsTitle;
	
					//// SAVE THE UPDATED ITEM_USER RECORDS ON LOCAL
				ATLItemUserModel.updateAllItemUserRecordsLocally(ITEM_TYPE.EVENT, itemUserRecordsByWebItemId);
				/// SAVE UPDATED ITEM_USER RECORDS ON PARSE
				upadteItemUserRecordsFlag = true;
				ItemUser itemUser = new ItemUser();
				/// call back on updateItemUserCallBack
				itemUser.itemUserListener = this;
				itemUser.updateItemUserOnParse(itemUserRecordsByWebItemId);
			}
			else
			{
				if (userIsInviter)
					eventControllerCallBackListener.bookEventCallBack(false);
				else
					eventControllerCallBackListener.respondToEventInviteCallBack(false);
			}
		}
		else
		{
			if (userIsInviter)
				eventControllerCallBackListener.bookEventCallBack(false);
			else
				eventControllerCallBackListener.respondToEventInviteCallBack(false);
		}
	}
	
	@Override
	public void savedNewItemUserCallBack(boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void respondToEventInvite(String primaryWebItemId,
			String choosenEventWebEventId)
	{
		/**
		 * Invitee gets all the event recs and itemUser recs
		 * Update the  itemUser recs for this user.
		 * Set wasReceived/receivedDatetime to true when the 
		 * user is shown the alert
		 * When invitee votes, 
		 * update the status/statusDatetime/priorityOrder 
		 * of each of his itemUser recs.
		 * Save updated recs locally, 
		 * and send updated recs to Parse
		 */
		
		if (primaryWebItemId!=null && !primaryWebItemId.equals("")
				)
		{
			eventToBook = 
		 			ATLEventCalendarModel.getEventByPrimaryWebEventId(primaryWebItemId);
			this.choosenEventWebEventId = choosenEventWebEventId;

			ItemUser itemUser = new ItemUser();
			itemUser.itemUserListener = this;
			ArrayList<String> allWebItemIdsAssociated = ATLEventCalendarModel.getAllEventWebEventIdByPrimaryWebEventId(primaryWebItemId);
		
		
		
			itemUser.getItemUserRecordsByWebItemId(allWebItemIdsAssociated);
		}
		else
		{
			eventControllerCallBackListener.respondToEventInviteCallBack(false);

		}
	}
	public void deleteAllEvents() {
		ATLEventCalendarModel.deleteAllEvents();
	}
	
	
	@Override
	public void registerSuccess(boolean signUpSuccess) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void signInSuccess(boolean success) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void getUserEmailOnParseCallBack(
			HashMap<String, String> loginProperties, boolean success) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getUpateCallBack(boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getFriendEmailOnParse(
			ATLContactModel friendsPropertiesOnParseByEmail, boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getFacebookFriendIdOnParse(ATLContactModel facebookAtlasFriend,
			boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getAllFBAtlasUsersCallBack(
			ArrayList<ATLContactModel> allFacebookAtlasUsers, boolean success) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getAllFBAtlasUsersFriendsCallBack(
			ArrayList<ATLContactModel> allFacebookAtlasUsers, boolean success) {
		// TODO Auto-generated method stub
		
	}
//	@Override
//	public void saveCallBack(boolean saved) {
//		// TODO Auto-generated method stub
//		
//	}
	@Override
	public void getAtlasNewFriendsByEmailCallBack(
			ArrayList<ATLContactModel> allCommonAtlasUsers) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void resetPasswordCallBack(boolean emailSuccessfullySent,
			String parseMessage) {
		// TODO Auto-generated method stub
		
	}
	private String eventRespondPushNotificationMessage="";
	private boolean bookEventFlag = false;
	private ArrayList<EventProperties> eventUpdatedRecords;

	@Override
	public void bookEvent(ArrayList<EventProperties> event,
			ArrayList<ItemUserProperties> itemUserRecords,
			int timePicked,Context ctx) {
		Log.v("EVENT CONTROLLER",
				"BOOK EVENT " );
		if (event!=null && event.size()>0 && itemUserRecords!=null
				&& itemUserRecords.size()>0 && timePicked>=0 && timePicked<=event.size())
		{
			this.ctx = ctx;
			bookEventFlag  = true;
			eventUpdatedRecords = event;
			String webEventIdPicked = getWebEventIdPicked(timePicked,eventUpdatedRecords);
			eventRespondPushNotificationMessage = "";
			Event eventUpdate = new Event();
			eventUpdate.eventDelagator = this;
			ItemUser itemUserUpdate = new ItemUser();
			itemUserUpdate.itemUserListener = this;

			if (eventUpdatedRecords.get(0).atlasId!=null && 
					eventUpdatedRecords.get(0).atlasId.equals(ATLUser.getParseUserID()))
			{
				//// user is the inviter
				eventUpdatedRecords = setEventRecords(eventUpdatedRecords,webEventIdPicked);
//				eventUpdatedRecords = bookEventRecords(eventUpdatedRecords);
				eventRespondPushNotificationMessage = "Event "+eventUpdatedRecords.get(0).title+" was booked by "+ATLUser.getUserFirstName();
				/// update inviter item user records
				updatedItemUserRecords = updateItemUserRecords(itemUserRecords,webEventIdPicked);
				
			
				
				ATLItemUserModel.updateAllItemUserRecordsLocally(ITEM_TYPE.EVENT, updatedItemUserRecords);
				ArrayList<String> webEventIds = new ArrayList<String>();
				for (EventProperties eventA:eventUpdatedRecords)
					if (eventA.webEventId!=null && !eventA.webEventId.equals(""))
						webEventIds.add(eventA.webEventId);
				ArrayList<ItemUserProperties> invitees = ATLItemUserModel.getAllItemUserRecordsByWebItemIds(webEventIds);;	
				  if (invitees!=null && invitees.size()>0)
					  updatedItemUserRecords=invitees;
				ATLEventCalendarModel.WriteEventProperties(eventUpdatedRecords);
				eventUpdate.updateEventOnParse(eventUpdatedRecords);

			}
			else
			{
				String  inviteeWebEventIdPicked = getWebEventIdPicked(timePicked,eventUpdatedRecords);
				if (itemUserRecords.size()/eventUpdatedRecords.size()==2)
				{
					eventRespondPushNotificationMessage = ATLUser.getUserFirstName()+" has responded to your "+eventUpdatedRecords.get(0).title+" event";
					eventUpdatedRecords = setEventRecords(eventUpdatedRecords,webEventIdPicked);
					/// update invitee item user records
					updatedItemUserRecords = updateItemUserRecords(itemUserRecords,inviteeWebEventIdPicked);
					ATLItemUserModel.updateAllItemUserRecordsLocally(ITEM_TYPE.EVENT, updatedItemUserRecords);
					ATLEventCalendarModel.WriteEventProperties(eventUpdatedRecords);
					/// call back on updateEventOnParseCallBack
					eventUpdate.updateEventOnParse(eventUpdatedRecords);

				}else
				{
					eventRespondPushNotificationMessage = ATLUser.getUserFirstName()+" has responded to your "+eventUpdatedRecords.get(0).title+" event";

					//// group schedualing
					updatedItemUserRecords = updateItemUserRecords(itemUserRecords,inviteeWebEventIdPicked);
					ATLItemUserModel.updateAllItemUserRecordsLocally(ITEM_TYPE.EVENT, updatedItemUserRecords);
					/// call back on updateBookedItemUserCallBack
					itemUserUpdate.updateRespondItemUserOnParse(updatedItemUserRecords);
				}

   

			}	

		}
		else
		{
			eventControllerCallBackListener.bookEventCallBack(false);
		}



	}
	
	
	
	
	
	public void bookEvent(int timePicked, ArrayList<EventProperties> event,
			ArrayList<ItemUserProperties> itemUserRecords,
			Context ctx) {

		if (event!=null && event.size()>0 && itemUserRecords!=null
				&& itemUserRecords.size()>0)
		{
			this.ctx = ctx;
			bookEventFlag  = true;
			eventUpdatedRecords = event;
			String webEventIdPicked = getWebEventIdPicked(timePicked,eventUpdatedRecords);
			eventRespondPushNotificationMessage = "";
			Event eventUpdate = new Event();
			eventUpdate.eventDelagator = this;
			ItemUser itemUserUpdate = new ItemUser();
			itemUserUpdate.itemUserListener = this;
			updatedItemUserRecords = itemUserRecords;
			if (eventUpdatedRecords.get(0).atlasId!=null && 
					eventUpdatedRecords.get(0).atlasId.equals(ATLUser.getParseUserID()))
			{
				//// user is the inviter
				eventUpdatedRecords = setEventRecords(eventUpdatedRecords,webEventIdPicked);
//				eventUpdatedRecords = bookEventRecords(eventUpdatedRecords);
				eventRespondPushNotificationMessage = "Event "+eventUpdatedRecords.get(0).title+" was booked by "+ATLUser.getUserFirstName();
				/// update inviter item user records
//				updatedItemUserRecords = updateItemUserRecords(itemUserRecords,webEventIdPicked);
				
			
				
				ATLItemUserModel.updateAllItemUserRecordsLocally(ITEM_TYPE.EVENT, updatedItemUserRecords);
				ArrayList<String> webEventIds = new ArrayList<String>();
				for (EventProperties eventA:eventUpdatedRecords)
					if (eventA.webEventId!=null && !eventA.webEventId.equals(""))
						webEventIds.add(eventA.webEventId);
				ArrayList<ItemUserProperties> invitees = ATLItemUserModel.getAllItemUserRecordsByWebItemIds(webEventIds);;	
				  if (invitees!=null && invitees.size()>0)
					  updatedItemUserRecords=invitees;
				ATLEventCalendarModel.WriteEventProperties(eventUpdatedRecords);
				eventUpdate.updateEventOnParse(eventUpdatedRecords);

			}
			else
			{
				String  inviteeWebEventIdPicked = getWebEventIdPicked(timePicked,eventUpdatedRecords);
				if (itemUserRecords.size()/eventUpdatedRecords.size()==2)
				{
					eventRespondPushNotificationMessage = ATLUser.getUserFirstName()+" has responded to your "+eventUpdatedRecords.get(0).title+" event";
					eventUpdatedRecords = setEventRecords(eventUpdatedRecords,webEventIdPicked);
					/// update invitee item user records
					updatedItemUserRecords = updateItemUserRecords(itemUserRecords,inviteeWebEventIdPicked);
					ATLItemUserModel.updateAllItemUserRecordsLocally(ITEM_TYPE.EVENT, updatedItemUserRecords);
					ATLEventCalendarModel.WriteEventProperties(eventUpdatedRecords);
					/// call back on updateEventOnParseCallBack
					eventUpdate.updateEventOnParse(eventUpdatedRecords);

				}else
				{
					eventRespondPushNotificationMessage = ATLUser.getUserFirstName()+" has responded to your "+eventUpdatedRecords.get(0).title+" event";

					//// group schedualing
//					updatedItemUserRecords = updateItemUserRecords(itemUserRecords,inviteeWebEventIdPicked);
					ATLItemUserModel.updateAllItemUserRecordsLocally(ITEM_TYPE.EVENT, updatedItemUserRecords);
					/// call back on updateBookedItemUserCallBack
					itemUserUpdate.updateRespondItemUserOnParse(updatedItemUserRecords);
				}

   

			}	

		}
		else
		{
			eventControllerCallBackListener.bookEventCallBack(false);
		}



	}
	
	
	
	
	
	
//	private String getInviteeWebEventIdPicked(int timePicked,
//			ArrayList<ItemUserProperties> updatedItemUserRecords, ArrayList<EventProperties> eventUpdatedRecords2)
//	{
//		String inviteeWebEventIdPicked = "";
//		
//		
//
//		if (updatedItemUserRecords!=null && updatedItemUserRecords.size()>0)
//		{
//			for (ItemUserProperties itemUserProp:updatedItemUserRecords)
//				inviteeWebEventIdPicked = (itemUserProp.rProp.getEventAssociated().displayOrder==(timePicked-1))?
//						itemUserProp.webItemId : inviteeWebEventIdPicked;
//		}
//
//
//		return inviteeWebEventIdPicked;
//	}
	private Context ctx;
	private ArrayList<ItemUserProperties> updatedItemUserRecords;
	@Override
	public void updateEventOnParseCallBack(boolean success) 
	{
		if (success)
		{
			if (updatedItemUserRecords!=null && updatedItemUserRecords.size()>0)
			{
				ItemUser itemUserUpdate = new ItemUser();
				itemUserUpdate.itemUserListener = this;
				//call back on updateBookedItemUserCallBack method
				itemUserUpdate.updateRespondItemUserOnParse(updatedItemUserRecords);
			}
			else
			{
				ATLCalendarCellController.updateCalendarAlarmCell(eventUpdatedRecords,  ctx);
//			ATLCalendarCellController.saveCalendarCellWithEvent(eventUpdatedRecords,  ctx);
				////////
//				Toast.makeText(ctx,"Hello!!!", 
//		                Toast.LENGTH_SHORT).show();
				//////////
				eventControllerCallBackListener.bookEventCallBack(true);
			}
			
		}
		else
		{
			eventControllerCallBackListener.bookEventCallBack(false);
		}
		
	}
	private ArrayList<ItemUserProperties> updateItemUserRecords(
			ArrayList<ItemUserProperties> itemUserRecords,String webEventIdPicked) {
		ArrayList<ItemUserProperties> userUpdatedRecords= new ArrayList<ItemUserProperties>();
		if (itemUserRecords!=null && itemUserRecords.size()>0 )
		{
			
			for (ItemUserProperties itemUser:itemUserRecords)
			{
				if (itemUser!=null)
				{
					if (itemUser.atlasId.equals(ATLUser.getParseUserID()))
					{
						if (itemUser.webItemId!=null  && webEventIdPicked!=null 
								&& !webEventIdPicked.equals("")  && itemUser.webItemId.equals(webEventIdPicked))
						{/// time picked
							itemUser.status = ITEM_USER_TASK_STATUS.ACCEPTED;
							itemUser.priorityOrder = ITEM_TYPE_PRIORITY_ORDER.IDEAL;
							itemUser.wasReceived = true;
							
						}
						else
						{
							itemUser.status = ITEM_USER_TASK_STATUS.DECLINED;
							itemUser.priorityOrder = ITEM_TYPE_PRIORITY_ORDER.DECLINED;
							
							
						}
						
						itemUser.receivedDateTime = new Date();
						itemUser.modifiedDatetime = new Date();
						itemUser.wasReceived = true;
						
						
						userUpdatedRecords.add(itemUser);
					}
					
					
				}
			}
//			ATLItemUserModel.UpdateAllItemUserRecordsLocallyInBackground(ITEM_TYPE.EVENT, userUpdatedRecords);
			ATLItemUserModel.updateAllItemUserRecordsLocally(ITEM_TYPE.EVENT, userUpdatedRecords);
		}
		return userUpdatedRecords;
		
	}
	/**
	 * Update the event record status to complited and update the 
	 * local DB
	 * @param event
	 */
	private ArrayList<EventProperties> bookEventRecords(ArrayList<EventProperties> event) {
		if (event!=null && event.size()>0)
		{
			for (EventProperties eventProp:event)
			{
				eventProp.status = EVENT_STATUS.COMPLITED;
				eventProp.modifiedDatetime = new Date();	
			}
			ATLEventCalendarModel.WriteEventProperties(event);
		}
		return event;
	}
	/**
	 * update events records status according to the webEventIdPicked
	 * and update them locally
	 * @param event
	 * @param webEventIdPicked
	 */
	private ArrayList<EventProperties> setEventRecords(ArrayList<EventProperties> event,
			String webEventIdPicked)
	{
		if (event!=null && event.size()>0)
		{
			for (EventProperties eventProp:event)
			{
				if (eventProp.webEventId.equals(webEventIdPicked))
				{
					eventProp.status = EVENT_STATUS.THE_ONE;
					
				}
				else
					eventProp.status = EVENT_STATUS.NOT_THE_ONE;
				eventProp.modifiedDatetime = new Date();	
			}
			ATLEventCalendarModel.WriteEventProperties(event);
			
			
		}
		return event;
	}
	/** 
	 * Return the web event id of the chosen time or empty string case declined
	 * @param timePicked
	 * @param itemUserRecords
	 * @return
	 */
	private String getWebEventIdPicked(int timePicked,
			ArrayList<EventProperties> eventRecords) {

		String webEventIdPicked = "";
		
		if (eventRecords!=null && eventRecords.size()>0)
		{
			for (EventProperties eventProp:eventRecords)
				webEventIdPicked = (eventProp.displayOrder==(timePicked-1))?
						eventProp.webEventId : webEventIdPicked;
		}
		
		
		return webEventIdPicked;
	}

	
//	private static ATLItemUserModelCallBackInterface atlItemUserModelListener;
//	private static ATLEventCalendarModelCallBackInterface eventModellistener;
//	public  void refreshEventsInBackground(ATLEventCalendarModelCallBackInterface listener)
//	{
//		eventModellistener = listener;
////		atlItemUserModelListener = itemUserModelListener;
//		RefreshAllEventRecordsFromParseInBackground refreshAllEventRecordsFromParseInBackground = new RefreshAllEventRecordsFromParseInBackground();
////		RefreshAllItemUserRecordsFromParseInBackground refreshAllItemUserRecordsFromParseInBackground = new RefreshAllItemUserRecordsFromParseInBackground();
////		refreshAllEventRecordsFromParseInBackground.execute();
//		ArrayList<String> webItemIdsList = ATLEventCalendarModel.getAllEventWebEventsIds();
//		if(webItemIdsList==null || webItemIdsList.size()==0)
//			return ;
//		
//		ParseQuery parseQuery = new ParseQuery(TABLES_NAME.EVENT.getTableName());
//		
//		parseQuery.whereContainedIn("web_event_id", webItemIdsList);
//		
//		parseQuery.findInBackground(new FindCallback() {   
//			public void done(List<ParseObject> eventList, ParseException e)
//			{
//				if (e==null && eventList!=null && eventList.size()>0)
//				{
//					HashMap<String, ArrayList<EventProperties>> eventsUpdated = new HashMap<String, ArrayList<EventProperties>>();
//					EventProperties eventProp;
//					ArrayList<EventProperties> eventPropList;
//					ArrayList<EventProperties> allEventPropList = new ArrayList<EventProperties>();
//					for (ParseObject event:eventList)
//					{
//						eventProp = new EventProperties(event);
//						
//						String primaryId = (eventProp.isPrimary)? eventProp.webEventId:eventProp.primaryWebEventId;
//						if (primaryId!=null && !primaryId.equals(""))
//						{
//							if (eventsUpdated.containsKey(primaryId))
//							{
//								eventPropList = eventsUpdated.get(primaryId);
//								if (eventPropList==null)
//									eventPropList = new ArrayList<EventProperties>();
//							}
//							else
//								eventPropList = new ArrayList<EventProperties>();
//							eventPropList.add(eventProp);
//							allEventPropList.add(eventProp);
//							eventsUpdated.put(primaryId, eventPropList);
//						}
//					}
//					// call back on eventCalendarUpdatedCallBack
//					ATLEventCalendarModel.eventCalendarModelListener = eventModellistener;
//					///  TO DO ;;;;; update local db...
//					ATLEventCalendarModel.WriteEventProperties(allEventPropList);
////					currentSessionEvents.setAllEventsPropertiesRefreshedList(eventsUpdated);
//				}
//			
//			}
//		});
//		
////	}
////		
////		
////		
////		
////	public  void refreshItemUserInBackground(ATLItemUserModelCallBackInterface itemUserModelListener)
////	{
//////		eventModellistener = listener;
////		atlItemUserModelListener = itemUserModelListener;
////		RefreshAllEventRecordsFromParseInBackground refreshAllEventRecordsFromParseInBackground = new RefreshAllEventRecordsFromParseInBackground();
////		RefreshAllItemUserRecordsFromParseInBackground refreshAllItemUserRecordsFromParseInBackground = new RefreshAllItemUserRecordsFromParseInBackground();
////		ArrayList<String> webItemIdsList = ATLItemUserModel.getAllCurrentWebItemIdsOnLocal(ITEM_TYPE.EVENT);
////		if(webItemIdsList==null || webItemIdsList.size()==0)
////			return ;
////		
//		
//		
////		return null;
//		
//	}
	
	
	
	public static void GetNewEvents()
	{
		final ArrayList<String> webItemIdsList = ATLEventCalendarModel.getAllEventWebEventsIds();
		ParseQuery parseQuery = new ParseQuery(TABLES_NAME.ITEM_USER.getTableName());
		parseQuery.whereEqualTo("item_type", ITEM_TYPE.EVENT.getItemName());
		parseQuery.whereEqualTo("atlas_id", ATLUser.getParseUserID());
		
		if (webItemIdsList!=null && webItemIdsList.size()>0)
		{
			parseQuery.whereNotContainedIn("web_item_id", webItemIdsList);
			
		}
		parseQuery.findInBackground(new FindCallback() {   
			public void done(List<ParseObject> itemUserList, ParseException e)
			{
				final ArrayList<String> webItemIds = new ArrayList<String>();
				ArrayList<ItemUserProperties> userItemUserRecords = new ArrayList<ItemUserProperties>();
				if (e==null && itemUserList!=null && itemUserList.size()>0)
				{
					ItemUserProperties itemProp ;
					String webItemId = "";
					final ArrayList<ItemUserProperties> itemPropList = new ArrayList<ItemUserProperties>();
					for (ParseObject itemUserObject:itemUserList)
					{
						itemProp = ItemUser.getItemUserPropertiesFromParse(itemUserObject);
						webItemId = itemProp.webItemId;
						if (itemProp!=null && webItemId!=null && !webItemId.equals("") )
						{
							itemPropList.add(itemProp);
							webItemIds.add(webItemId);
						}
						if (webItemIds!=null && webItemIds.size()>0)
						{
							/// fetch the other event invitee...
							ParseQuery parseQuery = new ParseQuery(TABLES_NAME.ITEM_USER.getTableName());
							parseQuery.whereEqualTo("item_type", ITEM_TYPE.EVENT.getItemName());
							parseQuery.whereNotEqualTo("atlas_id", ATLUser.getParseUserID());
							parseQuery.whereContainedIn("web_item_id", webItemIds);
							parseQuery.findInBackground(new FindCallback() {   
								public void done(List<ParseObject> itemUserList, ParseException e)
								{
									ArrayList<ItemUserProperties> userItemUserRecords = new ArrayList<ItemUserProperties>();
									ArrayList<String> inviteeAtlasIds = new ArrayList<String>();
									if (e==null && itemUserList!=null && itemUserList.size()>0)
									{
										ItemUserProperties itemProp ;
										String webItemId = "";
										for (ParseObject itemUserObject:itemUserList)
										{
											itemProp = ItemUser.getItemUserPropertiesFromParse(itemUserObject);
											if (itemProp!=null )
											{
												itemPropList.add(itemProp);
												inviteeAtlasIds.add(itemProp.atlasId);
											}
										}
										
										if (inviteeAtlasIds!=null && inviteeAtlasIds.size()>0)
											ATLFriendLocalTable.retrieveNewFriendsByAtlasIds(inviteeAtlasIds);
										
										//// Fetch Event Records
										ParseQuery parseQuery = new ParseQuery(TABLES_NAME.EVENT.getTableName());
										parseQuery.whereContainedIn("web_event_id", webItemIds);
										parseQuery.findInBackground(new FindCallback() {   
											public void done(List<ParseObject> eventList, ParseException e)
											{
												HashMap<String,ArrayList<EventProperties>> newEventsByPrimary = new HashMap<String,ArrayList<EventProperties>>();
												ArrayList<EventProperties> eventRecords = new ArrayList<EventProperties>();
												if (e==null && eventList!=null && eventList.size()>0)
												{
													EventProperties eventProp;
													String primaryEventId = "";
													ArrayList<EventProperties> eventPropList = new ArrayList<EventProperties>();
													ArrayList<EventProperties> allEventPropList = new ArrayList<EventProperties>();
													HashMap<String, String> webEventIdByPrimry = new HashMap<String, String>();
													for (ParseObject eventRecord:eventList)
													{
														
														eventProp = Event.getEventPropertiesFromParseObject(eventRecord);
														if (eventProp!=null)
														{
															primaryEventId = (eventProp.primaryWebEventId!=null && !eventProp.primaryWebEventId.equals(""))?
																	eventProp.primaryWebEventId : (eventProp.primaryWebEventId.equals(""))? eventProp.webEventId: "";
															if (primaryEventId!=null && !primaryEventId.equals(""))
															{
																if (!newEventsByPrimary.containsKey(primaryEventId) || newEventsByPrimary.get(primaryEventId)==null)
																	eventPropList = new ArrayList<EventProperties>();
																else
																	eventPropList = newEventsByPrimary.get(primaryEventId);
																eventPropList.add(eventProp);
																webEventIdByPrimry.put(eventProp.webEventId, primaryEventId);
																newEventsByPrimary.put(primaryEventId, eventPropList);
																allEventPropList.add(eventProp);
															}
															
														}
													}
													
													if (eventPropList!=null && eventPropList.size()>0 &&itemPropList!=null && itemPropList.size()>0)
													{
													//// Write to local DB
														ATLEventCalendarModel.WriteEventProperties(allEventPropList);
														ATLItemUserModel.updateAllItemUserRecordsLocally(ITEM_TYPE.EVENT, itemPropList);
//														ATLALertListController.getInstance().onResume() ;
														if (eventControllerCallBackListener!=null)
														{
															ATLALertListController.getInstance().refresh();
//															ATLEventCalendarModel.getYourMoveInBackground();
//															ATLALertListController.getInstance().isRefreshing = false;
															eventControllerCallBackListener.getNewEventReceived(newEventsByPrimary,webItemIds,webEventIdByPrimry);
														}
														
														
//														if (ATLAlertController.eventControllerCallBackListener!=null)
//														{
//															ATLALertListController.getInstance().initAttributes();
//
//															ATLALertListController.getInstance().headerSectionYourMoveView.stateChanged();
//															ATLALertListController.getInstance().headerSectionPendingView.resetState();
//															ATLALertListController.getInstance().headerSectionBookedView.resetState();
//
//															ATLALertListController.getInstance().setListener();
//															ATLALertListController.getInstance().bindingData(); // will be called in onResume();
//
//															
//															ATLALertListController.getInstance().refresh();
//															EventController eventController = new EventController();
//															
//															eventController.eventControllerCallBackListener = ATLAlertController.eventControllerCallBackListener;
//															ATLEventCalendarModel.getPendingInBackground();
//															ATLEventCalendarModel.getYourMoveInBackground();
//															ATLEventCalendarModel.getBookedInBackground();
//															ATLAlertController.eventControllerCallBackListener.getNewEventReceived(newEventsByPrimary,webItemIds,webEventIdByPrimry);
//														}
													}
													
													
												}
											}
										});



										
										
											
									}
								}
							});
							
							
							
						}
						
					}
					
				}else
					ATLALertListController.getInstance().refresh();
//					if (webItemIdsList!=null && webItemIdsList.size()>0)
//					{
//						if (eventControllerCallBackListener!=null && currentContext!=null)
//						{
////							EventController controller = new EventController();
////							controller.eventControllerCallBackListener = eventControllerCallBackListener;
//////							ATLALertListController.getInstance().refresh();
////							controller.RefreshAllEventRecordsFromParseInBackground(currentContext); 
//							
//							
//							ATLALertListController.getInstance().refresh();
//						}
////						eventControllerCallBackListener.refreshAlertsInBackgroundCallBack(false);
//					}
			}
		});
		
		
		
	}
	private static Context currentContext;
	
	/**
	 * Refresh all the events records currently found on the user local table
	 * from Parse DB, in a background process.
	 *  and in the end sets the event_updated flag on the CurrentSessionEvents singleton object to true.
	 *  (From ATLEventCalendarModel ,after writing to the table)
	 * @author sharonnachum
	 *
	 */
	public   void   RefreshAllEventRecordsFromParseInBackground(final Context context) {
//		@Override
//		protected String doInBackground(String... params)
//		{
			
		currentContext = context;
//			final CurrentSessionEvents currentSessionEvents = CurrentSessionEvents.getCurrentSessionEventsSingleton();
//			ArrayList<ItemUserProperties> allUserItemUsersPropertiesList = currentSessionEvents.getAllUserItemUsersPropertiesRefreshedList();
//			ArrayList<String> webItemIdsList =currentSessionEvents.getAllCurrentWebEventsIdsList();
//			
			
			final ArrayList<String> webItemIdsList = ATLEventCalendarModel.getAllEventWebEventsIds();
//			if(webItemIdsList==null || webItemIdsList.size()==0)
//				{
//					GetNewEvents(webItemIdsList);
////					eventControllerCallBackListener.refreshAlertsInBackgroundCallBack(false);
//				
//					return ;
//				}
//			
//			
//			// call back on writeItemUserOnLocalDB on success...
//			GetNewEvents(webItemIdsList);
			
			ParseQuery parseQuery = new ParseQuery(TABLES_NAME.EVENT.getTableName());
			
			parseQuery.whereContainedIn("web_event_id", webItemIdsList);
			
			parseQuery.findInBackground(new FindCallback() {   
				public void done(List<ParseObject> eventList, ParseException e)
				{
					
					ArrayList<String> webItemIdFoundOnParse = new ArrayList<String>();
					if (e==null && eventList!=null && eventList.size()>0)
					{
						
						HashMap<String, ArrayList<EventProperties>> eventsUpdated = new HashMap<String, ArrayList<EventProperties>>();
						final HashMap<String, EventProperties> bookedEvents = new HashMap<String, EventProperties>();
						EventProperties eventProp;
						ArrayList<EventProperties> eventPropList;
						ArrayList<EventProperties> allEventPropList = new ArrayList<EventProperties>();
						for (ParseObject event:eventList)
						{
							eventProp = new EventProperties(event);
							
							String primaryId = (eventProp.isPrimary)? eventProp.webEventId:eventProp.primaryWebEventId;
							if (primaryId!=null && !primaryId.equals(""))
							{
								if (eventsUpdated.containsKey(primaryId))
								{
									
									eventPropList = eventsUpdated.get(primaryId);
									if (eventPropList==null)
										eventPropList = new ArrayList<EventProperties>();
								}
								else
									eventPropList = new ArrayList<EventProperties>();
								webItemIdFoundOnParse.add(eventProp.webEventId);
								eventPropList.add(eventProp);
								allEventPropList.add(eventProp);
								eventsUpdated.put(primaryId, eventPropList);
								if (eventProp.status.equals(EVENT_STATUS.THE_ONE) || eventProp.status.equals(EVENT_STATUS.NOT_THE_ONE) )
										{
											bookedEvents.put(eventProp.webEventId, eventProp);
										}
							}
						}
						// call back on eventCalendarUpdatedCallBack
//						ATLEventCalendarModel.eventCalendarModelListener = eventModellistener;
						///  TO DO ;;;;; update local db...
						ATLEventCalendarModel.WriteEventProperties(allEventPropList);
//						currentSessionEvents.setAllEventsPropertiesRefreshedList(eventsUpdated);
					
						ParseQuery parseQuery = new ParseQuery(TABLES_NAME.ITEM_USER.getTableName());
							parseQuery.whereEqualTo("item_type", ITEM_TYPE.EVENT.getItemName());
							parseQuery.whereContainedIn("web_item_id", webItemIdFoundOnParse);
							
							parseQuery.findInBackground(new FindCallback() {   
								public void done(List<ParseObject> itemUserList, ParseException e)
								{
									ItemUserProperties itemUserProp;
//									HashMap<String, ArrayList<ItemUserProperties>> allItemUserRetrieved = new HashMap<String, ArrayList<ItemUserProperties>>();
									ArrayList<ItemUserProperties> itemUserPropList = new ArrayList<ItemUserProperties>();
									ArrayList<EventProperties> bookedOnCalendar = new ArrayList<EventProperties>();
									String primaryEvent;
									if (e==null && itemUserList!=null && itemUserList.size()>0)
									{
										EventProperties eventBookedProp;
										for (ParseObject itemUserPropObject:itemUserList )
										{
//											primaryEvent = (allCurrentWebEventsIds!=null && allCurrentWebEventsIds.size()>0 
//													&& allCurrentWebEventsIds.containsKey(itemUserPropObject.getString("web_item_id")))?
//															allCurrentWebEventsIds.get(itemUserPropObject.getString("web_item_id")):
//																"";
//													
											itemUserProp = ItemUser.getItemUserPropertiesFromParse(itemUserPropObject);
											if (bookedEvents.containsKey(itemUserProp.webItemId) 
													&& itemUserProp.atlasId.equals(ATLUser.getParseUserID()))
											{
												eventBookedProp = bookedEvents.get(itemUserProp.webItemId);
												if (!itemUserProp.status.equals(ITEM_USER_TASK_STATUS.ACCEPTED))
//												{
													
													eventBookedProp.status = EVENT_STATUS.NOT_THE_ONE;
//													bookedEvents.remove(itemUserProp.webItemId);
//												}else
//												{
//													eventBookedProp = bookedEvents.get(itemUserProp.webItemId);
////													if (eventBookedProp!=null && !eventBookedProp.atlasId.equals(ATLUser.getParseUserID()))
//														bookedOnCalendar.add(eventBookedProp);
//														
//												}
												bookedOnCalendar.add(eventBookedProp);
											}
											
											
											itemUserPropList.add(itemUserProp);
											
										}
//										ATLItemUserModel.atlItemUserModelListener = atlItemUserModelListener;
										ATLItemUserModel.UpdateAllItemUserRecordsLocallyInBackground(ITEM_TYPE.EVENT, itemUserPropList);
										////
										/// update local calendar 
										
//										ATLCalendarCellController.saveCalendarCellWithEvent(bookedOnCalendar, context);
										ATLCalendarCellController.updateCalendarAlarmCell(bookedOnCalendar, context);
										eventControllerCallBackListener.refreshAlertsInBackgroundCallBack(true);
										/////
									}else
										eventControllerCallBackListener.refreshAlertsInBackgroundCallBack(false);
//									currentSessionEvents.setAllUserItemUsersPropertiesRefreshedList(itemUserPropList);
								}
							});
							
					}else
					{  
//					if (webItemIdFoundOnParse!=null && webItemIdFoundOnParse.size()>0)
//						webItemIdsList.removeAll(webItemIdFoundOnParse);
//						if (webItemIdsList!=null && webItemIdsList.size()>0)
//						{
//							ATLItemUserModel.deleteItemUserByWebItemId(webItemIdsList);
//							ATLEventCalendarModel.deleteEventsByEventWebId(webItemIdsList);
//						}
					eventControllerCallBackListener.refreshAlertsInBackgroundCallBack(false);
				}
				}
			});
//			return null;
			
		}
	@Override
	public void saveCallBack(boolean saved) {
		// TODO Auto-generated method stub
		
	}
	
	}
	
	/**
	 * Refresh all the item_user records ,currently found on the local DB in A background process
	 * and in the end sets the item_user_updated flag on the CurrentSessionEvents singleton object to true.
	 * (From ATLItemUserModel ,after writing to the table)
	 * @author sharonnachum
	 *
	 */
//	public static class   RefreshAllItemUserRecordsFromParseInBackground extends AsyncTask<String, Integer, String> {
//		@Override
//		protected String doInBackground(String... params)
//		{
//			
////			
////			CurrentSessionEvents currentSessionEvents = CurrentSessionEvents.getCurrentSessionEventsSingleton();
////			ArrayList<ItemUserProperties> allUserItemUsersPropertiesList = currentSessionEvents.getAllUserItemUsersPropertiesRefreshedList();
////			ArrayList<String> webItemIdsList =currentSessionEvents.getAllCurrentWebEventsIdsList();
////			HashMap<String,String> allCurrentWebEventsIds = currentSessionEvents.getAllCurrentWebEventsIds();
////			
////			
//			
//			ArrayList<String> webItemIdsList = ATLItemUserModel.getAllCurrentWebItemIdsOnLocal(ITEM_TYPE.EVENT);
//			if(webItemIdsList==null || webItemIdsList.size()==0)
//				return null;
//			
//			
//			ParseQuery parseQuery = new ParseQuery(TABLES_NAME.ITEM_USER.getTableName());
//			parseQuery.whereEqualTo("item_type", ITEM_TYPE.EVENT.getItemName());
//			parseQuery.whereContainedIn("web_item_id", webItemIdsList);
//			
//			parseQuery.findInBackground(new FindCallback() {   
//				public void done(List<ParseObject> itemUserList, ParseException e)
//				{
//					ItemUserProperties itemUserProp;
//					HashMap<String, ArrayList<ItemUserProperties>> allItemUserRetrieved = new HashMap<String, ArrayList<ItemUserProperties>>();
//					ArrayList<ItemUserProperties> itemUserPropList = new ArrayList<ItemUserProperties>();
//					String primaryEvent;
//					if (e==null && itemUserList!=null && itemUserList.size()>0)
//					{
//						for (ParseObject itemUserPropObject:itemUserList )
//						{
////							primaryEvent = (allCurrentWebEventsIds!=null && allCurrentWebEventsIds.size()>0 
////									&& allCurrentWebEventsIds.containsKey(itemUserPropObject.getString("web_item_id")))?
////											allCurrentWebEventsIds.get(itemUserPropObject.getString("web_item_id")):
////												"";
////									
//							itemUserProp = ItemUser.getItemUserPropertiesFromParse(itemUserPropObject);
//							itemUserPropList.add(itemUserProp);
//							
//						}
//						ATLItemUserModel.atlItemUserModelListener = atlItemUserModelListener;
//						ATLItemUserModel.UpdateAllItemUserRecordsLocallyInBackground(ITEM_TYPE.EVENT, itemUserPropList);
//					
//					}
////					currentSessionEvents.setAllUserItemUsersPropertiesRefreshedList(itemUserPropList);
//				}
//			});
//			return null;
//			
//		}
//	}
//	
//	
	

	
	  
	
	
	
	
//}
