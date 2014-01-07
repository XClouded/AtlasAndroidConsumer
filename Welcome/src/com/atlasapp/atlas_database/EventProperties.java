package com.atlasapp.atlas_database;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.atlasapp.atlas_database.DatabaseConstants.ACTION;
import com.atlasapp.atlas_database.DatabaseConstants.EVENT_STATUS;
import com.atlasapp.atlas_database.DatabaseConstants.EVENT_TYPE;
import com.parse.ParseObject;
/*
 * A struct containing all the properties of an indevidual 
 * alt event row 
 */
public class EventProperties {
	
	public int tablePrimaryId = -1;
	public  String objectId = "";
	public  String title="";
	public  String location="";
	public  String notes="";
	public  Date startDateTime = null;
	public  int duration =0;
	// the order we display the alternative events 
	public  int displayOrder = 0;
	//the inviter id
	public  String atlasId = "";
	// objectId of primary web event id 
	public  String primaryWebEventId = "";
	public  EVENT_STATUS status= EVENT_STATUS.PENDING;
	public  ACTION action = ACTION.SAVE;
	public  boolean isPrimary = false;
	public String deviceId;
	public int primaryEventId = -1;
	public Date endDateTime;
	public Date modifiedDatetime;
	public int localTableEventId = -1;/// initialize (not in table)
	public int localTablePrimaryEventId=-1;// intitialize, default set to primary
	public EVENT_TYPE eventType = EVENT_TYPE.COFFEE;// default
	public String webEventId="";
	/**
	 * Creates an empty(default) event
	 * getBaseContext()
	 */
	public EventProperties()
	{
		
	}
	public EventProperties(String setWebEventId,
			String objectId,String title,String location,String notes,
			Date startDateTime,Date endDateTime,int duration,int displayOrder,String atlasId ,
	        String primaryWebEventId,EVENT_STATUS status, ACTION action
	        ,boolean isPrimary,EVENT_TYPE setEventType)
	{
		this.webEventId = (setWebEventId!=null)?setWebEventId:"";
		this.objectId = (objectId!=null)? objectId :this.objectId;
		this.title=(title!=null)? title:this.title;
		this.location=(location!=null)? location:this.location;
		this.notes=(notes!=null)?notes:this.notes;
		this.startDateTime = (startDateTime!=null)?startDateTime:this.startDateTime;
		this.endDateTime = (endDateTime!=null)?endDateTime:this.endDateTime;

		this.duration =duration;
		this.displayOrder = displayOrder;
		//the inviter id
		this.atlasId = (atlasId!=null)?atlasId:this.atlasId;
		this.primaryWebEventId = (primaryWebEventId!=null)?primaryWebEventId:this.primaryWebEventId;
		this.status= (status!=null)? status :EVENT_STATUS.PENDING;
		this.action = (action!=null)? action :ACTION.SAVE;
		this.isPrimary = isPrimary;
		this.eventType = (setEventType!=null)? setEventType : EVENT_TYPE.COFFEE;
	}
	public EventProperties(ParseObject eventParseObject)
	{
		if (eventParseObject!=null && eventParseObject.getClassName().equals("event"))
		{
			this.objectId = eventParseObject.getObjectId();
			this.title=(eventParseObject.getString("title")!=null)? eventParseObject.getString("title"):this.title;
			this.location=(eventParseObject.getString("location")!=null)? eventParseObject.getString("location"):this.location;
			this.notes=(eventParseObject.getString("notes")!=null)?eventParseObject.getString("notes"):this.notes;
			this.startDateTime = (eventParseObject.getDate("start_datetime")!=null)?eventParseObject.getDate("start_datetime"):this.startDateTime;
			this.duration =((eventParseObject.getNumber("duration")!=null)? (Integer) eventParseObject.getNumber("duration"):duration);
			
			
			String eventTypeFromParse = eventParseObject.getString("event_type");
			if (eventTypeFromParse==null || eventTypeFromParse.equals(""))
			{
				eventTypeFromParse = 	"coffee";
			}
			EVENT_TYPE setEventType = (eventTypeFromParse.equals("call"))? EVENT_TYPE.CALL:
				 (eventTypeFromParse.equals("dinner"))? EVENT_TYPE.DINNER:
					 (eventTypeFromParse.equals("drinks"))? EVENT_TYPE.DRINKS:
						 (eventTypeFromParse.equals("lunch"))? EVENT_TYPE.LUNCH:
							 (eventTypeFromParse.equals("meeting"))? EVENT_TYPE.MEETING:EVENT_TYPE.COFFEE;
				
				
			this.eventType =setEventType;	      
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDateTime);
			duration = (duration>0)? duration:0;    
			cal.add(Calendar.MINUTE, duration);
			endDateTime =cal.getTime();
				;
			modifiedDatetime = eventParseObject.getUpdatedAt();

			
			
			this.displayOrder = ((eventParseObject.getNumber("display_order")!=null)? (Integer) eventParseObject.getNumber("display_order"):duration);
			//the inviter id
			this.atlasId =(eventParseObject.getString("atlas_id")!=null)?eventParseObject.getString("atlas_id"):this.atlasId;
			this.primaryWebEventId =(eventParseObject.getString("primary_web_event_id")!=null)?eventParseObject.getString("primary_web_event_id"):this.primaryWebEventId;
			EVENT_STATUS statusInt = (eventParseObject.getNumber("status")!=null)?
						((Integer)eventParseObject.getNumber("status")==1)? EVENT_STATUS.THE_ONE:
							((Integer)eventParseObject.getNumber("status")==2)?EVENT_STATUS.NOT_THE_ONE:
								EVENT_STATUS.PENDING:EVENT_STATUS.PENDING;
			this.status= statusInt;
			this.action = (eventParseObject.getString("action")!=null)? (eventParseObject.getString("action").equals("del"))?
					ACTION.DELETE:ACTION.SAVE:ACTION.SAVE;
			this.isPrimary = primaryWebEventId.equals(objectId) || primaryWebEventId.equals("");
			
			this.webEventId = (eventParseObject.getString("web_event_id")!=null)?eventParseObject.getString("web_event_id"):"";
			deNull(this);
		
		}
	}
	
	
	public static EventProperties deNull(EventProperties eventProperties)
	{
		
		if (eventProperties!=null)
		{
			eventProperties.webEventId = (eventProperties.webEventId !=null)?eventProperties.webEventId : "";
			//eventProperties.tablePrimaryId = -1;
			eventProperties.objectId = (eventProperties.objectId!=null)?eventProperties.objectId: "";
			eventProperties.title=(eventProperties.title!=null)?eventProperties.title:"";
			eventProperties.location=(eventProperties.location!=null)?eventProperties.location:"";
			eventProperties.notes=(eventProperties.notes!=null)?eventProperties.notes:"";
			eventProperties.startDateTime = (eventProperties.startDateTime!=null)?eventProperties.startDateTime:new Date();
		//	eventProperties.duration =0;
			// the order we display the alternative events 
			//eventProperties.displayOrder = 0;
			//the inviter id
			eventProperties.atlasId = (eventProperties.atlasId!=null)?eventProperties.atlasId:"";
			// objectId of primary web event id 
			eventProperties.primaryWebEventId = (eventProperties.primaryWebEventId!=null)?eventProperties.primaryWebEventId:"";
			eventProperties.status= (eventProperties.status!=null)?eventProperties.status:EVENT_STATUS.PENDING;
			eventProperties.action = (eventProperties.action!=null)?eventProperties.action:ACTION.SAVE;
			//eventProperties.isPrimary = false;
			eventProperties.deviceId = (eventProperties.deviceId!=null)?eventProperties.deviceId:"";
			//public int primaryEventId = -1;
			Calendar cal = Calendar.getInstance();
			cal.setTime(eventProperties.startDateTime);
			eventProperties.duration = (eventProperties.duration>0)? eventProperties.duration:0;
			cal.add(Calendar.MINUTE, eventProperties.duration);
			eventProperties.endDateTime = (eventProperties.endDateTime!=null)?eventProperties.endDateTime:cal.getTime();
				;
			eventProperties.modifiedDatetime = (eventProperties.modifiedDatetime!=null)?eventProperties.modifiedDatetime:new Date();
			//public int localTableEventId = -1;/// initialize (not in table)
			//public int localTablePrimaryEventId=-1;
			eventProperties.eventType = (eventProperties.eventType!=null)?eventProperties.eventType:EVENT_TYPE.COFFEE;
		}
		else
		{
			eventProperties = new EventProperties();
		}
		
		
		
		return eventProperties;
	}

}
