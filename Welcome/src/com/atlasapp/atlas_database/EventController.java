package com.atlasapp.atlas_database;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.atlasapp.atlas_database.DatabaseConstants.EVENT_STATUS;
import com.atlasapp.atlas_database.DatabaseConstants.ITEM_TYPE;
import com.atlasapp.atlas_database.DatabaseConstants.ITEM_TYPE_PRIORITY_ORDER;
import com.atlasapp.atlas_database.DatabaseConstants.ITEM_USER_TASK_STATUS;
import com.atlasapp.atlas_database.DatabaseConstants.SERVER_MESSAGE;
import com.atlasapp.atlas_database.DatabaseConstants.SIGN_NEW_USERS_CALLER;
import com.atlasapp.common.ATLEventCalendarModel;
import com.atlasapp.common.ATLItemUserModel;
import com.atlasapp.common.AtlasAndroidUser;
import com.atlasapp.common.DB;
import com.atlasapp.model.ATLContactModel;
import com.atlasapp.model.ATLFriendModel;
import com.parse.ParseObject;
/**
 * Creates an event calendar invite
 * @author sharon nachum
 *
 */
public class EventController implements ItemUserDelegateInterface,EventCallBackInterface,EventControllerInterface,UserDelagateInterface{
	
	
	public EventControllerCallBackInterface eventControllerCallBackListener;
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
				ArrayList<String> webEventIds= ATLEventCalendarModel.getAllEventWebEventIdByPrimaryWebEventId(webItemId);
				
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

			eventControllerCallBackListener.respondToEventInviteCallBack(true);
		}

		
	}
	
	
	private void sendPushNotificationAlert(ArrayList<ItemUserProperties> inviteesEventInvites,
			String message)
	{
		
		if (message!=null && !message.equals("") && inviteesEventInvites!=null && inviteesEventInvites.size()>0){
		LinkedList<String> inviteesChannels = new LinkedList<String>();
		JSONObject data = new JSONObject();
		try {  
			data = new JSONObject("{\"alert\": \""+message+"\",\"badge\": \"Increment\",\"sound\": \"Incoming_Atlas_Push.mp3\"}");
		} catch (JSONException e) {
			// TODO Auto-generated catch block     
			e.printStackTrace();
		}
		for (ItemUserProperties invitee:inviteesEventInvites)
		{
			if (invitee!=null && invitee.atlasId!=null && !invitee.atlasId.equals("")
				&& 	!invitee.atlasId.equals(AtlasAndroidUser.getParseUserID()))
				inviteesChannels.add("ID"+invitee.atlasId);
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
	/**
	 * Gets the list of the event's members in array list type ATLContactModel &
	 * the EventProperties as a list of all date time alt ,type Event each.
	 * and sends an invites to all the members on Parse DB 
	 * @param event
	 * @param eventsMembers
	 * @return FAIL or SUCCESS message from Parse
	 */
	@Override
	public void createCalendarEvent(ArrayList<EventProperties> eventProperties, ArrayList<ATLContactModel> invitees )
	{
		if (eventProperties!=null && eventProperties.size()>0 && invitees!=null && invitees.size()>0 )
		{
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
			h.put("ATLAS_ID", AtlasAndroidUser.getParseUserID());//"CyAdwqTj1x"(Tan),"BiyWaF9mDY" Nghia,"WGRUBN8JJC"(Sharon)
			h.put("FIRSTNAME", AtlasAndroidUser.getUserNameDisplay());
			h.put("LASTNAME", "");
			h.put("COMPANY", "");
			h.put("PHONE_NUMBER", "");
			h.put("EMAIL_ADDRESS", AtlasAndroidUser.getEmail());
			h.put("PIC_PATH", AtlasAndroidUser.getParseUserID());
			String fromFacebook = (AtlasAndroidUser.getfbID()!=null && !AtlasAndroidUser.getfbID().equals(""))?"1":"0";
			h.put("FROM_FACEBOOK", fromFacebook);
			String fbID = (AtlasAndroidUser.getfbID()!=null)? AtlasAndroidUser.getfbID():"";
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
		if (eventInvitess!=null && eventInvitess.size()>0 
				&& eventPropertiesUpdated!=null && eventPropertiesUpdated.size()>0)
		{
			this.eventPropertiesUpdated = eventPropertiesUpdated;
			ItemUser itemUser = new ItemUser();
			// sign as a listener to call back response
			itemUser.itemUserListener = this;
			itemUser.setItemUsers(eventInvitess,eventPropertiesUpdated);
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
			AtlasServerConnect parseConnect =   AtlasServerConnect.getSingletonObject(null);
			//// call back from getSignNonAtlasUsersCallBack
			parseConnect.signNonAtlasUsers(SIGN_NEW_USERS_CALLER.EVENT_INVITE,newNonAtlasUserToSign,this);
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
					//// update the local database with the object id's 
					if (nonAtlasUsersByEmail!=null && 
							nonAtlasUsersByEmail.size()!=0)
					{
					  Iterator iterator = nonAtlasUsersByEmail.keySet().iterator();
					  String  keyColumnEmail;
					  ATLContactModel valueObjectId;
				      HashMap<String,String> atlasIdColumn  = new HashMap<String,String>();
					  while(iterator.hasNext()) 
					  {
						  keyColumnEmail=(String)iterator.next();
						  valueObjectId=(ATLContactModel)nonAtlasUsersByEmail.get(keyColumnEmail);
						  if (keyColumnEmail!=null && !keyColumnEmail.equals("")&& valueObjectId!=null &&
								  valueObjectId.getAtlasId()!=null && !valueObjectId.getAtlasId().equals(""))
						  {
							  atlasIdColumn.put("ATLAS_ID",valueObjectId.getAtlasId());
							  DB.update("ATL_FRIEND", atlasIdColumn, "EMAIL_ADDRESS", new String[]{keyColumnEmail});
							  atlasIdColumn  = new HashMap<String,String>();
							  atlasFriends.add(valueObjectId);
						  }
					  }
					}
					eventInvitess = atlasFriends;
					
					event = new ArrayList<Event>();
					String title = (createEventProperties.get(0)!=null)?createEventProperties.get(0).title:"";
					if (title!=null && !title.equals(""))
						calendarEventInviteMessage =  AtlasAndroidUser.getUserNameDisplay()+" has invited you to "+title+"";
					else
						calendarEventInviteMessage =  AtlasAndroidUser.getUserNameDisplay()+" has invited you to a new event ";

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

			///////  EVENT WAS SUCCESSFULLY SAVED AND UPDATED ON PARSE 
			/////// Send push notifications to all invitees....
			sendPushNotificationAlert(inviteesEventInvites,calendarEventInviteMessage);
		
			/// Success call back....
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
			if (eventToBook.get(0).atlasId.equals(AtlasAndroidUser.getParseUserID()))
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
				eventToBook.get(0).atlasId.equals(AtlasAndroidUser.getParseUserID()));

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
					if (itemUser!=null && itemUser.atlasId.equals(AtlasAndroidUser.getParseUserID()))
					{  
						itemUser.wasReceived = true;
						itemUser.receivedDateTime = new Date();
//						if (!userIsInviter)
//						{//// respond to event
							itemUser.priorityOrder = (itemUser.webItemId!=null && choosenEventWebEventId!=null 
													&& !choosenEventWebEventId.equals("") &&itemUser.webItemId.equals(choosenEventWebEventId))?
									ITEM_TYPE_PRIORITY_ORDER.OKAY:ITEM_TYPE_PRIORITY_ORDER.IDEAL;	
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
					respondMessageBack = AtlasAndroidUser.getUserNameDisplay()+ " has accepted your invitation to "+eventsTitle+ " on "+eventRespond;
					else
						if (eventsTitle!=null && !eventsTitle.equals("") && eventRespond.equals(""))
							respondMessageBack = AtlasAndroidUser.getUserNameDisplay()+ "  declined your invitation to "+eventsTitle;
	
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
	@Override
	public void saveCallBack(boolean saved) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getAtlasNewFriendsByEmailCallBack(
			ArrayList<ATLContactModel> allCommonAtlasUsers) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	

	
	  
	
	
	
	
}
