package atlasapp.section_calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Toast;
import atlasapp.common.ATLAlarmCellModel;
import atlasapp.common.ATLAlarmCellModelProperties;
import atlasapp.common.ATLUser;
import atlasapp.common.UtilitiesProject;
import atlasapp.common.ATLConstants.AlertType;
import atlasapp.common.ATLConstants.EventResponseType;
import atlasapp.database.DatabaseConstants.EVENT_STATUS;
import atlasapp.database.DatabaseConstants.ITEM_TYPE;
import atlasapp.database.DatabaseConstants.ITEM_USER_TASK_STATUS;
import atlasapp.database.EventProperties;
import atlasapp.database.ItemUserProperties;
import atlasapp.model.ATLContactModel;
import atlasapp.section_alerts.AlertCellData;

public class ATLCalendarCellController
{
	
	
	private static ContentResolver cr;

	public static ATLCalCellData setCellDataFromEvent(ArrayList<EventProperties> event,
			ArrayList<ATLContactModel> invitee)
	{
		ATLCalCellData cellData = new ATLCalCellData();
		EventResponseType responseType;
		
		if (event!=null && event.size()>0)
		{
			Date alt1,alt2,alt3;
			String title = event.get(0).title;
			EVENT_STATUS status= null ;
			for (EventProperties eventAlt:event)
			{
				if (eventAlt.displayOrder==0)
					alt1 = eventAlt.startDateTime;
				else
					if (eventAlt.displayOrder==1)
						alt2 = eventAlt.startDateTime;
					else
						if (eventAlt.displayOrder==2)
							alt3 = eventAlt.startDateTime;
				
				
				status = (status==null)? eventAlt.status:
						(!status.equals(EVENT_STATUS.NOT_THE_ONE))? eventAlt.status:status;
				
			}
			responseType = 
					 (status.equals(EVENT_STATUS.PENDING))?EventResponseType.Pending:
					 (status.equals(EVENT_STATUS.NOT_THE_ONE))?EventResponseType.Deleted:
					 (status.equals(EVENT_STATUS.THE_ONE))?EventResponseType.Accepted:
						 EventResponseType.None;

			
		}
			
		
		
		
		
		return cellData;
	}
	public static void saveCalendarCellWithItemUser(
			ArrayList<ItemUserProperties> itemUserRecords, ArrayList<EventProperties> eventUpdatedRecords, Context mContext) {
			
		
		if (itemUserRecords==null || itemUserRecords.size()==0 || 
				eventUpdatedRecords==null || eventUpdatedRecords.size()==0 )
				return;
			long startMillis = 0;
			long endMillis = 0;
			long duration = 0;
			EVENT_STATUS status= null ;
			Date preferDay = null ;
			Date alt1,alt2,alt3;
			HashMap<String,EventProperties> eventsByWebEventId = new HashMap<String,EventProperties>();
			HashMap<String,EventProperties> eventPicked = new HashMap<String,EventProperties>();
			for (EventProperties eventAlt:eventUpdatedRecords)
			{
				eventsByWebEventId.put(eventAlt.webEventId, eventAlt);
				if (eventAlt.status.equals(EVENT_STATUS.THE_ONE))
				{
					preferDay = eventAlt.startDateTime;
					eventPicked.put(eventAlt.webEventId, eventAlt);
				}
				if (eventAlt.status.equals(EVENT_STATUS.PENDING))
					preferDay = null;
//				if (eventAlt.status.equals(EVENT_STATUS.NOT_THE_ONE) && preferDay == null)
//				{
//					preferDay = eventAlt.startDateTime;
//					eventPicked.put(eventAlt.webEventId, eventAlt);
//				}
			
			}
//				if (eventAlt.displayOrder==0)
//				{
//					alt1 = eventAlt.startDateTime;
//					
//						preferDay = eventAlt.startDateTime;
//				}
//				else
//					if (eventAlt.displayOrder==1)
//						alt2 = eventAlt.startDateTime;
//					else
//						if (eventAlt.displayOrder==2)
//							alt3 = eventAlt.startDateTime;
//			
			EventProperties eventAlt=null;
			ATLAlarmCellModelProperties modelProp = null;// = new ATLAlarmCellModelProperties();

			for (ItemUserProperties itemUser:itemUserRecords)
			{
				if (eventPicked.size()>0)
				{

					/// event already booked by the inviter
					/// check to see if match the user pick...
					if (
							(eventPicked.containsKey(itemUser.webItemId) &&
									itemUser.status.equals(ITEM_USER_TASK_STATUS.ACCEPTED)))
					{
						modelProp = new ATLAlarmCellModelProperties();
						eventAlt = eventPicked.get(itemUser.webItemId);
						modelProp.respondStatus =  EventResponseType.Accepted;

					}
					else
					{
						long id = ATLAlarmCellModel.getAlarmIdByWebEventId(itemUser.webItemId);
						if (id!=-1)
						{
							deleteAlarmCellFromCalendarByID(id,mContext);
							ATLAlarmCellModel.updateAlarmStatusByWebEventId(itemUser.webItemId, EventResponseType.Decline);

						}
					}
				}
				else
				{
					if (itemUser.status.equals(ITEM_USER_TASK_STATUS.ACCEPTED))
					{
						modelProp = new ATLAlarmCellModelProperties();
						eventAlt = eventsByWebEventId.get(itemUser.webItemId);
						modelProp.respondStatus =  EventResponseType.Pending;
					}
					if (itemUser.status.equals(ITEM_USER_TASK_STATUS.DECLINED))
					{
						
						long id = ATLAlarmCellModel.getAlarmIdByWebEventId(itemUser.webItemId);
						if (id!=-1)
						{
							deleteAlarmCellFromCalendarByID(id,mContext);
							ATLAlarmCellModel.updateAlarmStatusByWebEventId(itemUser.webItemId, EventResponseType.Decline);

						}
					}

				}
				if (modelProp!=null && eventAlt!=null)
				{
					modelProp.alarmDatetime = eventAlt.startDateTime;
					modelProp.itemType = ITEM_TYPE.EVENT;
					modelProp.minutes = eventAlt.duration;
					modelProp.modifiedDatetime = eventAlt.modifiedDatetime;
					modelProp.title = eventAlt.title;
					modelProp.webItemId = eventAlt.webEventId;
					modelProp.atlasId = eventAlt.atlasId;
					long id = ATLAlarmCellModel.getAlarmIdByWebEventId(eventAlt.webEventId);
					if (id==-1)
						id = saveEventToCalendarCell(UtilitiesProject.ctx,eventAlt,modelProp);
					saveEventToLocalDB(id,modelProp);

				}
			}
			
			// Insert to event group
}
	public static void saveSelfCalendarCellWithEvent(ArrayList<EventProperties>  cellData, Context ctx){
		
		if (ctx==null)
			return;
//		ArrayList<String> allWebEventIdFromAlarm  = ATLAlarmCellModel.getAllIDRec();

		long startMillis = 0;
		long endMillis = 0;
		long duration = 0;
		EVENT_STATUS status= null ;
		Date preferDay = null ;
		Date alt1,alt2,alt3;
		for (EventProperties eventAlt:cellData)
		{
			
				if (eventAlt.status.equals(EVENT_STATUS.THE_ONE))
					preferDay = eventAlt.startDateTime;

				if (eventAlt.displayOrder==0)
				{
					alt1 = eventAlt.startDateTime;
					if (eventAlt.status.equals(EVENT_STATUS.PENDING))
						preferDay = eventAlt.startDateTime;
				}
				else
					if (eventAlt.displayOrder==1)
						alt2 = eventAlt.startDateTime;
					else
						if (eventAlt.displayOrder==2)
							alt3 = eventAlt.startDateTime;




				ATLAlarmCellModelProperties modelProp = new ATLAlarmCellModelProperties();
				modelProp.alarmDatetime = eventAlt.startDateTime;
				modelProp.itemType = ITEM_TYPE.EVENT;
				modelProp.minutes = eventAlt.duration;
				modelProp.modifiedDatetime = eventAlt.modifiedDatetime;
				modelProp.title = eventAlt.title;
				modelProp.webItemId = eventAlt.webEventId;
				modelProp.atlasId = eventAlt.atlasId;
				modelProp.respondStatus =  
						(eventAlt.status.equals(EVENT_STATUS.PENDING))?EventResponseType.Pending:
							(eventAlt.status.equals(EVENT_STATUS.NOT_THE_ONE))?EventResponseType.Deleted:
								(eventAlt.status.equals(EVENT_STATUS.THE_ONE))?EventResponseType.Accepted:
									EventResponseType.None;
//				if(allWebEventIdFromAlarm==null || allWebEventIdFromAlarm.size()==0 || !allWebEventIdFromAlarm.contains(eventAlt.webEventId) || eventAlt.webEventId.equals(""))
//				{
					
				long id = saveEventToCalendarCell(ctx,eventAlt,modelProp);
				saveEventToLocalDB(id,modelProp);
//			}else
//				ATLAlarmCellModel.updateAlarmStatusByWebEventId(eventAlt.webEventId, modelProp.respondStatus);

				
				// Insert to event group
		}
	}

/**
 * Gets array list of EventProperties objects and save them
 * to the calendar (according to the date and status)
 * @param cellData
 * @param ctx
 */
public static void saveCalendarCellWithEvent(ArrayList<EventProperties>  cellData, Context ctx){
	
	if (ctx==null)
		return;
	ArrayList<String> allWebEventIdFromAlarm  = ATLAlarmCellModel.getAllWebEventIdFromAlarm();
	
	long startMillis = 0;
	long endMillis = 0;
	long duration = 0;
	EVENT_STATUS status= null ;
	Date preferDay = null ;
	Date alt1,alt2,alt3;
	for (EventProperties eventAlt:cellData)
	{
		
			if (eventAlt.status.equals(EVENT_STATUS.THE_ONE))
				preferDay = eventAlt.startDateTime;

			if (eventAlt.displayOrder==0)
			{
				alt1 = eventAlt.startDateTime;
				if (eventAlt.status.equals(EVENT_STATUS.PENDING))
					preferDay = eventAlt.startDateTime;
			}
			else
				if (eventAlt.displayOrder==1)
					alt2 = eventAlt.startDateTime;
				else
					if (eventAlt.displayOrder==2)
						alt3 = eventAlt.startDateTime;




			ATLAlarmCellModelProperties modelProp = new ATLAlarmCellModelProperties();
			modelProp.alarmDatetime = eventAlt.startDateTime;
			modelProp.itemType = ITEM_TYPE.EVENT;
			modelProp.minutes = eventAlt.duration;
			modelProp.modifiedDatetime = eventAlt.modifiedDatetime;
			modelProp.title = eventAlt.title;
			modelProp.webItemId = eventAlt.webEventId;
			modelProp.atlasId = eventAlt.atlasId;
			modelProp.respondStatus =  
					(eventAlt.status.equals(EVENT_STATUS.PENDING))?EventResponseType.Pending:
						(eventAlt.status.equals(EVENT_STATUS.NOT_THE_ONE))?EventResponseType.Deleted:
							(eventAlt.status.equals(EVENT_STATUS.THE_ONE))?EventResponseType.Accepted:
								EventResponseType.None;
			if(allWebEventIdFromAlarm==null || allWebEventIdFromAlarm.size()==0 || !allWebEventIdFromAlarm.contains(eventAlt.webEventId) || eventAlt.webEventId.equals(""))
			{
				
			long id = saveEventToCalendarCell(ctx,eventAlt,modelProp);
			saveEventToLocalDB(id,modelProp);
		}else
			ATLAlarmCellModel.updateAlarmStatusByWebEventId(eventAlt.webEventId, modelProp.respondStatus);

			
			// Insert to event group
	}
}




private static long saveEventToCalendarCell(Context ctx, EventProperties eventAlt,ATLAlarmCellModelProperties modelProp)
{
	Calendar beginTime = Calendar.getInstance();
	long startMillis;
	long endMillis;
	int id = -1;
	if (eventAlt.startDateTime!=null)
	{
		beginTime.setTimeInMillis(eventAlt.startDateTime.getTime());
		startMillis = beginTime.getTimeInMillis();

		int duration = eventAlt.duration *60*1000;
		endMillis = startMillis + duration;// To long value

		ContentResolver cr = UtilitiesProject.cr;//ctx.getContentResolver();
		ContentValues values = new ContentValues();

		values.put(CalendarContract.Events.DTSTART, startMillis);
		values.put(CalendarContract.Events.DTEND, endMillis);
		values.put(CalendarContract.Events.TITLE, eventAlt.title);
		values.put(CalendarContract.Events.DESCRIPTION,
				eventAlt.notes);
		values.put(CalendarContract.Events.EVENT_LOCATION,
				eventAlt.location);
		if (ATLCalendarManager.getCalendarList(UtilitiesProject.ctx).size() > 0)
		{
			values.put(CalendarContract.Events.CALENDAR_ID, ATLCalendarManager.getCalendarList(UtilitiesProject.ctx).get(0).id);//default calendar events
			values.put(CalendarContract.Events.EVENT_COLOR,
					ATLCalendarManager.getCalendarList(UtilitiesProject.ctx).get(0).color);
			
//			values.put(CalendarContract.Events.EVENT_COLOR,
//						Color.YELLOW);
		}
		values.put(CalendarContract.Events.EVENT_TIMEZONE, "eventTimezone");

		Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
		// Retrieve ID for new event
		 id = Integer.parseInt(uri.getLastPathSegment());
		//Log.v("CalendarUtilities", "URI  :" + uri.toString());
		
	}
	
	return id;
}

private static void saveEventToLocalDB(long id,ATLAlarmCellModelProperties modelProp)
{
	Calendar beginTime = Calendar.getInstance();
	Calendar gmt0Cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
	gmt0Cal.setTime(beginTime.getTime());
	
	SimpleDateFormat lv_formatter;
	lv_formatter = new SimpleDateFormat("yyyy:DD:MM HH:mm:ss");
	lv_formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
	 
//	ATLEventGroupModel model = new ATLEventGroupModel();
	
	
	
	
//	ATLAlarmCellModelProperties modelProp = new ATLAlarmCellModelProperties();

//	modelProp.respondStatus =  
//			(status.equals(EVENT_STATUS.PENDING))?EventResponseType.Pending:
//			(status.equals(EVENT_STATUS.NOT_THE_ONE))?EventResponseType.Decline:
//	        (status.equals(EVENT_STATUS.THE_ONE))?EventResponseType.Accepted:
//	        EventResponseType.None;

	
//	modelProp.calendarId = id + ":" + gmt0Cal.getTimeInMillis();
	
	// Insert Group model
//	ATLEventGroupDatabaseAdapter.insertCalendarModel(model);
	modelProp.calendarId = id+"";
	
	ATLAlarmCellModel.write(modelProp);
}

public static void updateCalendarAlarmCell(ArrayList<EventProperties>  cellData, Context ctx)
{
//	if (ctx==null)
//		return ;
	ContentResolver cr =UtilitiesProject.cr;// ctx.getContentResolver();
	if (cellData!=null && cr!=null)
	{
		HashMap<String,String> allCurrentAlarmCellsIds = ATLAlarmCellModel.getAllWebEventIdAndCalendarIDSFromAlarm();
		for (EventProperties eventAlt:cellData)
		{
			if (eventAlt!=null && eventAlt.webEventId!=null && !eventAlt.webEventId.equals(""))
			{
				if (allCurrentAlarmCellsIds!=null && allCurrentAlarmCellsIds.containsKey(eventAlt.webEventId))
				{
					long id = Long.valueOf(allCurrentAlarmCellsIds.get(eventAlt.webEventId));
					EventResponseType respondStatus =  
							(eventAlt.status.equals(EVENT_STATUS.PENDING))?EventResponseType.Pending:
							(eventAlt.status.equals(EVENT_STATUS.NOT_THE_ONE))?EventResponseType.Deleted:
					        (eventAlt.status.equals(EVENT_STATUS.THE_ONE))?EventResponseType.Accepted:
					        EventResponseType.None;
					if (respondStatus.equals(EventResponseType.Deleted))
					{
						Uri eventUri = ContentUris.withAppendedId(
								CalendarContract.Events.CONTENT_URI, id);
						int rowDeleted2 = cr.delete(eventUri, null, null);
					}
//					Toast.makeText(ctx, "Respond  : " + respondStatus.ordinal()  , Toast.LENGTH_SHORT).show();
					ATLAlarmCellModel.updateAlarmStatusByWebEventId(eventAlt.webEventId, respondStatus);
				}
				
			}
		}
	}   
	
	
}
       

public static int deleteAlarmCellFromCalendarByID(long id, Context context)
{
	int deletedRows=0;
	ContentResolver cr = context.getContentResolver();
	Uri eventUri = ContentUris.withAppendedId(
			CalendarContract.Events.CONTENT_URI, id);
	deletedRows   =cr.delete(eventUri, null, null);
	return deletedRows;
}

public static boolean deleteGroupEventByDate(Date date, Context ctx){
	
	ATLEventGroupModel groupEvent = ATLEventGroupDatabaseAdapter.getEventGroupOfEventDate(date.getTime());
	
	ATLEventGroupModel newGroup = new ATLEventGroupModel();
	
	if(groupEvent != null){
		String preferTimeString = ATLEventGroupDatabaseAdapter.getDateStringFromIDString(groupEvent.calCellEventIdentifier);
		String id1 = ATLEventGroupDatabaseAdapter.getEventIDFromIDString(groupEvent.calCellEventIdentifier);
		
		String alt2TimeString = ATLEventGroupDatabaseAdapter.getDateStringFromIDString(groupEvent.calCellAlt2EventIdentifier);
		String id2 = ATLEventGroupDatabaseAdapter.getEventIDFromIDString(groupEvent.calCellAlt2EventIdentifier);
		
		String alt3TimeString = ATLEventGroupDatabaseAdapter.getDateStringFromIDString(groupEvent.calCellAlt3EventIdentifier);
		String id3 = ATLEventGroupDatabaseAdapter.getEventIDFromIDString(groupEvent.calCellAlt3EventIdentifier);
		
		ContentResolver cr = ctx.getContentResolver();
		String dateString = date.getTime() +"";
		if(preferTimeString != null && dateString.equals(preferTimeString)){
			if(groupEvent.calCellAlt2EventIdentifier != null){
				if(groupEvent.calCellAlt2EventIdentifier.length()>0){
					long id = Long.valueOf(id2);
					if (id>=0){
						Uri eventUri2 = ContentUris.withAppendedId(
								CalendarContract.Events.CONTENT_URI, id);
						int rowDeleted2 = cr.delete(eventUri2, null, null);
						Log.v("CalendarUtilities", "Delete event 2:  " + eventUri2.toString() + "  "+ rowDeleted2);
					}
				}
			}
			
			if(groupEvent.calCellAlt3EventIdentifier != null){
				if(groupEvent.calCellAlt3EventIdentifier.length()>0){
					long id = Long.valueOf(id3);
					if (id>=0){
						Uri eventUri3 = ContentUris.withAppendedId(
								CalendarContract.Events.CONTENT_URI, id);
						int rowDeleted3 = cr.delete(eventUri3, null, null);
						Log.v("CalendarUtilities", "Delete event 3:  " + eventUri3.toString()+ "  "+ rowDeleted3);
					}
				}
			}
			newGroup.calCellEventIdentifier = groupEvent.calCellEventIdentifier;
		}
		else if(alt2TimeString != null && dateString.equals(alt2TimeString)){
			Uri eventUri = ContentUris.withAppendedId(
					CalendarContract.Events.CONTENT_URI, Long.valueOf(id1));

			int rowDeleted = cr.delete(eventUri, null, null);
			// Retrieve ID for new event
			Log.v("CalendarUtilities", "Delete event:  " + eventUri.toString()+ "  "+ rowDeleted);
			
			if(groupEvent.calCellAlt3EventIdentifier != null){
				if(groupEvent.calCellAlt3EventIdentifier.length()>0){
					Uri eventUri3 = ContentUris.withAppendedId(
							CalendarContract.Events.CONTENT_URI, Long.valueOf(id3));
					int rowDeleted3 = cr.delete(eventUri3, null, null);
					Log.v("CalendarUtilities", "Delete event 3:  " + eventUri3.toString()+ "  "+ rowDeleted3);
				}
			}
			newGroup.calCellEventIdentifier = groupEvent.calCellAlt2EventIdentifier;
		}
		else if(alt3TimeString != null && dateString.equals(alt3TimeString)){
			Uri eventUri = ContentUris.withAppendedId(
					CalendarContract.Events.CONTENT_URI, Long.valueOf(id1));

			int rowDeleted = cr.delete(eventUri, null, null);
			// Retrieve ID for new event
			Log.v("CalendarUtilities", "Delete event:  " + eventUri.toString()+ "  "+ rowDeleted);
			
			if(groupEvent.calCellAlt2EventIdentifier != null){
				if(groupEvent.calCellAlt2EventIdentifier.length()>0){
					Uri eventUri2 = ContentUris.withAppendedId(
							CalendarContract.Events.CONTENT_URI, Long.valueOf(id2));
					int rowDeleted2 = cr.delete(eventUri2, null, null);
					Log.v("CalendarUtilities", "Delete event 2:  " + eventUri2.toString() + "  "+ rowDeleted2);
				}
			}
			newGroup.calCellEventIdentifier = groupEvent.calCellAlt3EventIdentifier;
		}
		ATLEventGroupDatabaseAdapter.deleteEventGroupID(groupEvent.calCellGroupEventID);
		newGroup.eventRespondStatus = EventResponseType.Decline;
		ATLEventGroupDatabaseAdapter.insertCalendarModel(newGroup);
	}
	
	return true;
}

}
