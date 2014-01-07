package atlasapp.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Context;
import android.widget.Toast;

import atlasapp.database.DatabaseConstants.EVENT_TYPE;
import atlasapp.database.EventController;
import atlasapp.database.EventControllerCallBackInterface;
import atlasapp.database.EventProperties;
import atlasapp.database.ItemUserProperties;
import atlasapp.database.DatabaseConstants.ACTION;
import atlasapp.database.DatabaseConstants.EVENT_STATUS;
import atlasapp.model.ATLContactModel;
import atlasapp.model.ATLEventCalendarModel;
import atlasapp.model.ATLFriendModel;

public class ATLMockDB implements EventControllerCallBackInterface{
	
	private EventController eventController;
	private Context ctx;
	
	public ATLMockDB(Context ctx)
	{
		this.ctx = ctx;
		eventController = new EventController();
		/// initialize ATLMockDB to listen to call back methods
		/// from event controller....
		eventController.eventControllerCallBackListener = this;
	}

	
	
	
	//////////////////////////////////////////
	
	public void createClendarEvent()
	{
		ArrayList<EventProperties> eventsProperties= new ArrayList<EventProperties>();
		EventProperties eventProperties;
		String objectId="";
		String title = "Coffee Event";
		String location= "New Database";
		String notes="Testing";
		Date startDateTime;
		Date endDateTime;
		int duration = 15;
		int displayOrder;
		String atlasId = ATLUser.getParseUserID();
		String primaryWebEventId = "";
		EVENT_STATUS status = EVENT_STATUS.PENDING;
		ACTION action = ACTION.SAVE;
		boolean isPrimary;
		for (int eventAlt=0; eventAlt<3; eventAlt++)
		{
			startDateTime = new Date();
			startDateTime.setHours(9+eventAlt);
			startDateTime.setMinutes(eventAlt*10);
			endDateTime =startDateTime;
			endDateTime.setMinutes(endDateTime.getMinutes()+duration);
			displayOrder = eventAlt;
			isPrimary = (eventAlt==0);
			eventProperties = new EventProperties("",objectId, title, location,
					 notes, startDateTime, endDateTime, duration,
					 displayOrder, atlasId , primaryWebEventId,
					  status,  action, isPrimary, EVENT_TYPE.LUNCH,"","");
			
			eventsProperties.add(eventProperties);
		}
		ArrayList<ATLContactModel> invitees = new ArrayList<ATLContactModel>();
		ATLContactModel invitee;
		ATLFriendModel friend;
		ArrayList<HashMap<String,String>> inviteesHash = getMockHashMap();
		
		for (int contact=0; contact<inviteesHash.size(); contact++)
		{
			
			friend = new ATLFriendModel();
			
			friend.fromHashMap(inviteesHash.get(contact));
			invitee = new ATLContactModel(friend);
			invitees.add(invitee);
		}
		
		createCalendarEventMock(eventsProperties,invitees);
		
		
	}
	
	
	private ArrayList<HashMap<String,String>> getMockHashMap()
	{
		ArrayList<HashMap<String,String>> invitess = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> h = new HashMap<String,String>();
		h.put("FRIEND_ID", "0");
		h.put("ATLAS_ID", "8TYJRhpfth");//"8TYJRhpfth"(Tan),"BiyWaF9mDY" Nghia,"WGRUBN8JJC"(Sharon)
		h.put("FIRSTNAME", "Tan");
		h.put("LASTNAME", "Ryan");
		h.put("COMPANY", "");
		h.put("PHONE_NUMBER", "");
		h.put("EMAIL_ADDRESS", "tan@antkingbiz.com");
		h.put("PIC_PATH", "8TYJRhpfth");
		h.put("FROM_FACEBOOK", "0");
		h.put("FACEBOOK_ID", "");
		h.put("FROM_TWITTER", "0");
		h.put("FROM_LINKEDIN", "0");
		h.put("LINKEDIN_ID", "");
		h.put("FROM_ADDRESS_BOOK", "0");
		h.put("ADDRESS_BOOK_ID", "");
		invitess.add(h);
		h = new HashMap<String,String>();

		h.put("FRIEND_ID", "1");
		h.put("ATLAS_ID", "XwUx9Csu4N");//"CyAdwqTj1x"(Tan),"BiyWaF9mDY" Nghia,"WGRUBN8JJC"(Sharon)
		h.put("FIRSTNAME", "Nghia");
		h.put("LASTNAME", "Truong");
		h.put("COMPANY", "");
		h.put("PHONE_NUMBER", "");
		h.put("EMAIL_ADDRESS", "nghia@antkingbiz.com");
		h.put("PIC_PATH", "XwUx9Csu4N");
		h.put("FROM_FACEBOOK", "1");
		h.put("FACEBOOK_ID", "1177009171");
		h.put("FROM_TWITTER", "0");
		h.put("FROM_LINKEDIN", "0");
		h.put("LINKEDIN_ID", "");
		h.put("FROM_ADDRESS_BOOK", "0");
		h.put("ADDRESS_BOOK_ID", "");
		invitess.add(h);
		return invitess;
	}
	public void createCalendarEventMock(ArrayList<EventProperties> eventProperties,
			ArrayList<ATLContactModel> invitees)
	{
//		eventController.createCalendarEvent(eventProperties, invitees);
	}
	@Override
	public void createCalendarEventCallBack(boolean success) {
		if (success)
		{
			Toast.makeText(ctx.getApplicationContext(), "Event successfully updated", Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(ctx.getApplicationContext(), "Event failed to update", Toast.LENGTH_LONG).show();

		}
		
	}

	  
	
	///////////////////
	public HashMap<String, ATLEventCalendarModel> getAllEvents()
	{
		HashMap<String, ATLEventCalendarModel> allEvents = eventController.getAllUserCalendarInvites();
		if (allEvents!=null && allEvents.size()>0){
			Toast.makeText(ctx.getApplicationContext(), "Events number:"+allEvents.size(), Toast.LENGTH_LONG).show();

		  Iterator iterator = allEvents.keySet().iterator();
		  String  keyColumn;
		  ATLEventCalendarModel valueColumn;
		  while(iterator.hasNext()) 
		  {
			  Toast.makeText(ctx.getApplicationContext(), "Event :", Toast.LENGTH_SHORT).show();

			  keyColumn=(String)iterator.next();
			  valueColumn = allEvents.get(keyColumn);
			  Toast.makeText(ctx.getApplicationContext(), "Event Titel:"+valueColumn.title, Toast.LENGTH_SHORT).show();
			  Toast.makeText(ctx.getApplicationContext(), "Event webEventId:"+valueColumn.webEventId, Toast.LENGTH_SHORT).show();

		  }    
			  
		}else
			  Toast.makeText(ctx.getApplicationContext(), "No Events found ", Toast.LENGTH_LONG).show();

		return allEvents;
	}
	
	
	//////////////////////////
	
	
	public void getEventByPrimaryWebEventId(String webItemId)
	{
		eventController.getEventByPrimaryWebEventId(webItemId);
	}
	@Override
	public void getEventByPrimaryWebEventIdCallBack(boolean success,
			ArrayList<EventProperties> event,
			ArrayList<ItemUserProperties> invitees) {

		if (success && event!=null && event.size()>0 && invitees!=null && invitees.size()>0)
		{
			 Toast.makeText(ctx.getApplicationContext(), 
					  "Event Found!", Toast.LENGTH_LONG).show();
			 Toast.makeText(ctx.getApplicationContext(), 
					  "Event Title: "+event.get(0).title, Toast.LENGTH_LONG).show();
			for (EventProperties properties:event)
			{
				  Toast.makeText(ctx.getApplicationContext(), 
						  "Event ObjectId:"+properties.objectId, Toast.LENGTH_SHORT).show();

			}
			Toast.makeText(ctx.getApplicationContext(), 
					"Invitee number : "+invitees.size(), Toast.LENGTH_SHORT).show();	
			
			
			for (ItemUserProperties invitee:invitees)
			{
				
				
//				if (invitee!=null && !invitee.atlasId.equals(AtlasAndroidUser.getParseUserID()))
////				  Toast.makeText(ctx.getApplicationContext(), 
////						  "User item user record STATUS :"+invitee.status, Toast.LENGTH_SHORT).show();
				//else{
//					Toast.makeText(ctx.getApplicationContext(), 
//						"Invitee : "+invitee.atlasId+" item user record STATUS :"+invitee.status, Toast.LENGTH_SHORT).show();	
//				
					if (invitee.getItemUserContact()!=null && invitee.getItemUserContact().displayName()!=null)
						Toast.makeText(ctx.getApplicationContext(), 
								"Invitee NAME : "+invitee.getItemUserContact().displayName(), Toast.LENGTH_SHORT).show();	
						
				
			//	}
			}
		}else
		{
			  Toast.makeText(ctx.getApplicationContext(), "No Event found by that primary key:", Toast.LENGTH_LONG).show();

		}
		
	}
	
	///////////////////////////////////
	
	
	public void refreshUserCalendarEvents()
	{
		eventController.refreshUserCalendarEvents();
	}
	@Override
	public void getAllUserEventsCallBack(
			HashMap<String, ArrayList<EventProperties>> allEventsRetrievedFromItemUser,
			ArrayList<ItemUserProperties> allUserItemUsersPropertiesRefreshedList,
			boolean success) {
		
		if (success)
		{
			if (allEventsRetrievedFromItemUser!=null && allEventsRetrievedFromItemUser.size()>0 &&
					allUserItemUsersPropertiesRefreshedList!=null && allUserItemUsersPropertiesRefreshedList.size()>0)
			{
				  Toast.makeText(ctx.getApplicationContext(), "number of new Events found"+allEventsRetrievedFromItemUser.size(), Toast.LENGTH_LONG).show();
				  Toast.makeText(ctx.getApplicationContext(), "number of new item user found"+allUserItemUsersPropertiesRefreshedList.size(), Toast.LENGTH_LONG).show();
				  String eventId = "";
				  String title = "";
				  for (int i=0; i<allUserItemUsersPropertiesRefreshedList.size(); i++)
				  {
					   eventId = allUserItemUsersPropertiesRefreshedList.get(0).webItemId;
					  if (eventId!=null && !eventId.equals(""))
					  {
						  title = ATLEventCalendarModel.getEventTitleByWebEventId(eventId).title;
					  }
					  if (title!=null &&
							  allUserItemUsersPropertiesRefreshedList!=null && allUserItemUsersPropertiesRefreshedList.size()>0
							  && allUserItemUsersPropertiesRefreshedList.get(2)!=null &&
							  allUserItemUsersPropertiesRefreshedList.get(i).getItemUserContact()!=null
							  && allUserItemUsersPropertiesRefreshedList.get(i).getItemUserContact().displayName()!=null)
						    
					  Toast.makeText(ctx.getApplicationContext(), "invitee "+allUserItemUsersPropertiesRefreshedList.get(2).getItemUserContact().displayName()+" to "+title, Toast.LENGTH_SHORT).show();
					  else
						  Toast.makeText(ctx.getApplicationContext(), "invitee NULL"+allUserItemUsersPropertiesRefreshedList.size(), Toast.LENGTH_SHORT).show();

				  }
				  
				  
				  
			}
			else
			{
				  Toast.makeText(ctx.getApplicationContext(), "No NEW Event found", Toast.LENGTH_LONG).show();

			}
		}
		else
		{
			  Toast.makeText(ctx.getApplicationContext(), "FAILED No Event found ", Toast.LENGTH_LONG).show();

		}
		
	}
	
	
	///////////////////////////////////////////
	
	
	
	public void respondToEvent(String primaryWebItemId,String choosenEventWebEventId)
	{
		
		eventController.respondToEventInvite(primaryWebItemId, choosenEventWebEventId);
	}

	@Override
	public void respondToEventInviteCallBack(boolean success) {

		if (success)
		{
			  Toast.makeText(ctx.getApplicationContext(), "Respons to event successfully updated", Toast.LENGTH_LONG).show();

		}
		else
		{
			  Toast.makeText(ctx.getApplicationContext(), "Respons to event failed to updated", Toast.LENGTH_LONG).show();

		}
	}
	
	
	////////////////////////////////////////////////////
	
	
	
	
	public void bookAnEvent( String primaryWebItemId,
							String choosenEventWebEventId)
	{
		eventController.bookEvent(primaryWebItemId, choosenEventWebEventId);
	}

	@Override
	public void bookEventCallBack(boolean success) {
		if (success)
		{
			  Toast.makeText(ctx.getApplicationContext(), "Booked event successfully ", Toast.LENGTH_LONG).show();

		}
		else
		{
			  Toast.makeText(ctx.getApplicationContext(), "FAILED to book event", Toast.LENGTH_LONG).show();

		}
		
	}
	
	
	
	////////////////////////////////////////////
	/// this is an inner call back method ,for Event.java class only
	@Override
	public void getAllUserAnEventCallBack(
			HashMap<String, ArrayList<EventProperties>> userEvents,
			HashMap<String, ArrayList<String>> eventsMembers, boolean success) {
		// TODO Auto-generated method stub
		
	}




	public void deleteAllEvents() {
		eventController.deleteAllEvents();
		getAllEvents();
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
